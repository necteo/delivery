package com.spring.delivery.service;

import com.spring.delivery.domain.*;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.OrderItemDTO;
import com.spring.delivery.dto.OrderListDTO;
import com.spring.delivery.dto.SocketMessageForm;
import com.spring.delivery.exception.*;
import com.spring.delivery.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void init() {
        executorService.scheduleAtFixedRate(this::checkUnacceptedOrders, 1, 1, TimeUnit.MINUTES);
    }

    private void checkUnacceptedOrders(){
        LocalDateTime currentTime = LocalDateTime.now();
        List<Order> orders = new ArrayList<>();
        if (orderRepository.findAllByStatus(OrderStatus.ORDER).isPresent())
            orders = orderRepository.findAllByStatus(OrderStatus.ORDER).get();

        for (Order order : orders){
            if (order.getOrderTime().plusMinutes(1).isBefore(currentTime)){
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);

                SocketMessageForm messageForm = new SocketMessageForm(true);
                User user = userRepository.findById(order.getUser().getId())
                        .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));
                String userMail = user.getEmail();
                messageForm.setUserId(order.getUser().getId());
                messageForm.setUserEmail(userMail);
                messageForm.setMessage("주문이 자동으로 취소되었습니다.");

                // Send the message
                messagingTemplate.convertAndSend("/topic/orders", messageForm);
            }
        }
    }

    public SocketMessageForm create(OrderDTO orderDTO){
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));
        Long userId = user.getId();
        String userEmail = user.getEmail();

        SocketMessageForm messageForm = new SocketMessageForm(true);

        messageForm.setUserId(userId);
        messageForm.setUserEmail(userEmail);

        if (orderDTO.getTotalPrice() < 6000){
//            throw new InvalidOrderException("최소 주문 금액 6000원을 넘어야 주문이 가능합니다.", userId);
            messageForm.setState(false);
            messageForm.setMessage("최소 주문 금액 6000원을 넘어야 주문이 가능합니다.");
            return messageForm;
        }

        Store store = storeRepository.findById(orderDTO.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("store doesn't exist"));
        int storeOpen = store.getOpenTime();
        int storeClosed = store.getClosedTime();
        if (orderDTO.getCurrentHour() < storeOpen || orderDTO.getCurrentHour() > storeClosed) {
//            throw new InvalidOrderException("가게 운영 시간이 아닙니다.", userId);
            messageForm.setState(false);
            messageForm.setMessage("가게 운영 시간이 아닙니다.");
            return messageForm;
        }

        boolean haveMainMenu = false;
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItem()){
            if (menuRepository.findById(orderItemDTO.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("menu doesn't exist"))
                    .getMenuType().equals(MenuType.MAIN))
                haveMainMenu = true;
        }
        if (!haveMainMenu) {
//            throw new InvalidOrderException("사이드 메뉴 만으로는 주문이 불가능합니다.", userId);
            messageForm.setMessage("사이드 메뉴 만으로는 주문이 불가능합니다.");
            messageForm.setState(false);
            return messageForm;
        }


        Order order = new Order();

        order.setUser(userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist")));
        order.setStatus(OrderStatus.ORDER);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalPrice(orderDTO.getTotalPrice());

        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItem()){
            OrderItem item = new OrderItem();

            item.setMenu(menuRepository.findOneById(orderItemDTO.getMenuId()));
            item.setQuantity(orderItemDTO.getQuantity());
            order.addOrderItem(item);
        }

        orderRepository.save(order);

        messageForm.setMessage("주문 접수가 완료되었습니다.");
        return messageForm;
    }

    public SocketMessageForm cancel(OrderDTO orderDTO){
        Long orderId = orderDTO.getOrderId();
        Order order1 = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order doesn't exist"));;
        User user = order1.getUser();
        Long userId = orderDTO.getUserId();
        String userEmail = user.getEmail();

        SocketMessageForm messageForm = new SocketMessageForm(true);

        messageForm.setUserId(userId);
        messageForm.setUserEmail(userEmail);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order doesn't exist"));;
        if (order.getStatus().equals(OrderStatus.DELIVERY)) {
//            throw new InvalidOrderException("배달중인 주문은 취소가 불가능합니다.", userMail);
            messageForm.setMessage("배달중인 주문은 취소가 불가능합니다.");
            messageForm.setState(false);
            return messageForm;
        }
        else if (order.getStatus().equals(OrderStatus.COMPLETED)) {
//            throw new InvalidOrderException("이미 배달이 완료된 주문은 취소가 불가능합니다.", userMail);
            messageForm.setMessage("이미 배달이 완료된 주문은 취소가 불가능합니다.");
            messageForm.setState(false);
            return messageForm;
        }
        else if (order.getStatus().equals(OrderStatus.CANCELLED)) {
//            throw new InvalidOrderException("이미 취소된 주문은 취소가 불가능합니다.", userMail);
            messageForm.setMessage("이미 취소된 주문은 취소가 불가능합니다.");
            messageForm.setState(false);
            return messageForm;
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        messageForm.setMessage("주문 취소가 완료되었습니다.");
        return messageForm;
    }

    public List<OrderDTO> findAllOrdersByUserId(Long userId){
        List<OrderDTO> orderListDTO = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        if (orderRepository.findAllByUserId(userId).isPresent())
            orders = orderRepository.findAllByUserId(userId).get();
        for (Order order : orders){
            String orderState = "주문";
            if (order.getStatus().equals(OrderStatus.CANCELLED))
                orderState = "취소";
            else if (order.getStatus().equals(OrderStatus.DELIVERY))
                orderState = "배달중";
            else if (order.getStatus().equals(OrderStatus.COMPLETED))
                orderState = "완료";
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setState(orderState);
            orderDTO.setOrderId(order.getId());
            orderDTO.setUserId(userId);
            orderDTO.setStoreId(1L);
            orderDTO.setCurrentHour(order.getOrderTime().getHour());
            orderDTO.setTotalPrice(order.getTotalPrice());
            if (!order.getOrderItems().isEmpty()){
                List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
                for (OrderItem orderItem : order.getOrderItems()){
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setMenuId(orderItem.getMenu().getId());
                    orderItemDTO.setStoreId(1L);
                    orderItemDTO.setQuantity(orderItem.getQuantity());
                    orderItemDTOS.add(orderItemDTO);
                }
                orderDTO.setOrderItem(orderItemDTOS);
            }
            orderListDTO.add(orderDTO);
        }
        return orderListDTO;
    }
    public List<OrderDTO> findAllOrders(){
        List<OrderDTO> orderListDTO = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders){
            String orderState = "주문";
            if (order.getStatus().equals(OrderStatus.CANCELLED))
                orderState = "취소";
            else if (order.getStatus().equals(OrderStatus.DELIVERY))
                orderState = "배달중";
            else if (order.getStatus().equals(OrderStatus.COMPLETED))
                orderState = "완료";
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setState(orderState);
            orderDTO.setOrderId(order.getId());
            orderDTO.setUserId(order.getUser().getId());
            orderDTO.setStoreId(1L);
            orderDTO.setCurrentHour(order.getOrderTime().getHour());
            orderDTO.setTotalPrice(order.getTotalPrice());
            if (!order.getOrderItems().isEmpty()){
                List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
                for (OrderItem orderItem : order.getOrderItems()){
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setMenuId(orderItem.getMenu().getId());
                    orderItemDTO.setStoreId(1L);
                    orderItemDTO.setQuantity(orderItem.getQuantity());
                    orderItemDTOS.add(orderItemDTO);
                }
                orderDTO.setOrderItem(orderItemDTOS);
            }
            orderListDTO.add(orderDTO);
        }
        return orderListDTO;
    }

    public SocketMessageForm acceptOrder(OrderDTO orderDTO){
        Long orderId = orderDTO.getOrderId();
        Order order1 = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order doesn't exist"));
        User user = order1.getUser();
        Long userId = orderDTO.getUserId();
        String userEmail = user.getEmail();

        SocketMessageForm messageForm = new SocketMessageForm(true);

        messageForm.setUserId(userId);
        messageForm.setUserEmail(userEmail);

        Order order = orderRepository.findById(orderId).get();
        if (!order.getStatus().equals(OrderStatus.ORDER)) {
//            throw new InvalidOrderException("주문 상태의 주문만 수락할 수 있습니다.", userMail);
            messageForm.setMessage("주문 상태의 주문만 수락할 수 있습니다.");
            messageForm.setState(false);
            return messageForm;
        }
        order.setStatus(OrderStatus.DELIVERY);
        orderRepository.save(order);

        messageForm.setMessage("주문을 수락하였습니다.");
        return messageForm;
    }

    public SocketMessageForm denyOrder(OrderDTO orderDTO){
        Long orderId = orderDTO.getOrderId();
        Order order1 = orderRepository.findById(orderId).get();
        User user = order1.getUser();
        Long userId = orderDTO.getUserId();
        String userEmail = user.getEmail();

        SocketMessageForm messageForm = new SocketMessageForm(true);
        messageForm.setUserId(userId);
        messageForm.setUserEmail(userEmail);

        Order order = orderRepository.findById(orderId).get();
        if (!order.getStatus().equals(OrderStatus.ORDER)) {
//            throw new InvalidOrderException("접수된 주문만 수락할 수 있습니다.", userMail);
            messageForm.setMessage("접수된 주문만 수락할 수 있습니다.");
            messageForm.setState(false);
            return messageForm;
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        messageForm.setMessage("주문을 거절하였습니다.");
        return messageForm;
    }

    public SocketMessageForm setOrderDelivered(OrderDTO orderDTO){
        Long orderId = orderDTO.getOrderId();
        Order order1 = orderRepository.findById(orderId).get();
        User user = order1.getUser();
        Long userId = orderDTO.getUserId();
        String userEmail = user.getEmail();

        SocketMessageForm messageForm = new SocketMessageForm(true);
        messageForm.setUserId(userId);
        messageForm.setUserEmail(userEmail);


        Order order = orderRepository.findById(orderId).get();

        if (!order.getStatus().equals(OrderStatus.DELIVERY)) {
//            throw new InvalidOrderException("배달 상태의 주문만 완료 처리할 수 있습니다.", userMail);
            messageForm.setMessage("배달 상태의 주문만 완료 처리할 수 있습니다.");
            messageForm.setState(false);
            messageForm.setUserEmail(userEmail);
            messageForm.setUserId(userId);
            return messageForm;
        }
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        messageForm.setMessage("배달이 완료되었습니다.");
        return messageForm;
    }
}
