# 음식 주문 배달 사이트

---
Svelt + Spring Boot

## 프로젝트 소개

---
Spring Boot를 사용한 음식 주문과 배달 모니터링 시스템을 간단하게 구현해보는 프로젝트입니다.
프론트엔드는 Svelt로 간단하게 만들었습니다.

### 개발 기간

---
- 2023.03 ~ 2023.06

### 멤버 구성 및 담당

---
- 신재화 : 
- 박준홍 : ?
- 이 : ?
- 김 : ?

### 📌개발 환경

---
- `Java`
- **IDEA** : IntelliJ
- **Framework** : Spring Boot
- **DB** : H2 DB
- **ORM** : JPA

## 주요 기능

---
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
