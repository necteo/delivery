# OrderController

### CreateOrder

```java
@PostMapping("/create")
public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO){
        orderService.create(orderDTO);
        return new ResponseEntity<>("주문 접수가 완료되었습니다.", HttpStatus.CREATED);
        }
```

# OrderService

### create

```java
int storeOpen = 1;  //store.getRunTime();int storeClosed = 10;   //  추후 store runtime 저장방식 정해지면 수정 필요
        if (LocalDateTime.now().getHour() < storeOpen || LocalDateTime.now().getHour() > storeClosed)
```
### cancel
* 취소했을 때 실시간 반영 되는지 체크 필요