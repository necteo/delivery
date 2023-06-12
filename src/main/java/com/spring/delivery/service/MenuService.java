package com.spring.delivery.service;

import com.spring.delivery.domain.*;
import com.spring.delivery.dto.*;
import com.spring.delivery.exception.DuplicatedMenuException;
import com.spring.delivery.repository.MenuRepository;
import com.spring.delivery.repository.StatisticsRepository;
import com.spring.delivery.repository.StoreRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final StatisticsRepository statisticsRepository;


    public Long create(MenuRegisterDTO menuRegisterDTO){

        Store store = storeRepository.findOneById(menuRegisterDTO.getStoreId());

        Menu menu = new Menu(menuRegisterDTO.getName(),
                MenuType.valueOf(menuRegisterDTO.getMenuType().toUpperCase()),
                DiscountPolicy.DEFAULT,
                menuRegisterDTO.getPrice(),
                menuRegisterDTO.getDescription(),
                menuRegisterDTO.getImageName(),
                store
        );

        validateDuplicateMenu(menu);
        return menuRepository.save(menu).getId();
    }
    private void validateDuplicateMenu(Menu menu){
        List<Menu> findMenus = menuRepository.findByName(menu.getName());
        if(!findMenus.isEmpty()){
            throw new DuplicatedMenuException("이미 등록되어 있는 메뉴입니다");
        }
    }
    public List<MenuInfoDTO> findAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuInfoDTO> menuInfoDTOList = new ArrayList<>();
        menus.forEach(menu -> {
            MenuInfoDTO menuInfoDTO = MenuInfoDTO.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .price(menu.getPrice())
                    .description(menu.getDescription())
                    .menuType(menu.getMenuType().toString().toLowerCase())
                    .discountPolicy(menu.getDiscountPolicy().toString().toLowerCase())
                    .imageName(menu.getImageName())
                    .build();
            menuInfoDTOList.add(menuInfoDTO);
        });
        return menuInfoDTOList;
    }

    public MenuInfoDTO findMenuInfo(Long menuId) {
        Menu menu = menuRepository.findOneById(menuId);
        return MenuInfoDTO.builder()
                .id(menuId)
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .menuType(menu.getMenuType().toString().toLowerCase())
                .discountPolicy(menu.getDiscountPolicy().toString().toLowerCase())
                .imageName(menu.getImageName())
                .build();
    }

    public MenuInfoDTO findMenuInfo(String menuName) {
        Menu menu = menuRepository.findByName(menuName).get(0);
        return MenuInfoDTO.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .menuType(menu.getMenuType().toString().toLowerCase())
                .discountPolicy(menu.getDiscountPolicy().toString().toLowerCase())
                .imageName(menu.getImageName())
                .build();
    }

    public Long updateMenu(MenuUpdateDTO menuUpdateDTO) {
        List<Menu> findUpdateMenu = menuRepository.findByName(menuUpdateDTO.getName());
        findUpdateMenu.forEach(menu -> {
            menu.setName(menuUpdateDTO.getUpdatedName());
            menu.setPrice(menuUpdateDTO.getPrice());
            menu.setDescription(menuUpdateDTO.getDescription());
        });
        return menuRepository.findOneById(findUpdateMenu.get(0).getId()).getId();
    }

    public void findAllDiscountPolicy() {
        //findMenuInfo로 재활용
    }

    public Long applyMenuPolicy(MenuDiscountPolicyDTO menuDiscountPolicyDTO) {
        List<Menu> findUpdateMenu = menuRepository.findByName(menuDiscountPolicyDTO.getName());
        findUpdateMenu.forEach( menu ->
                menu.setDiscountPolicy(
                        DiscountPolicy.valueOf(menuDiscountPolicyDTO.getDiscountPolicy().toUpperCase()
                        )
                )
        );
        return menuRepository.findByName(menuDiscountPolicyDTO.getName()).get(0).getId();
    }

    public List<StatisticsDTO> findStatistics() {
        List<Statistics> statistics = statisticsRepository.findAll();
        List<StatisticsDTO> statisticsDTOS = new ArrayList<>();
        statistics.forEach(statistic -> {
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.builder()
                    .id(statistic.getId())
                    .count(statistic.getCount())
                    .menu(statistic.getMenu())
                    .build();
            statisticsDTOS.add(statisticsDTO);
        });
        return statisticsDTOS;
    }
}