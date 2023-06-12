package com.spring.delivery.service;

import com.spring.delivery.domain.Address;
import com.spring.delivery.domain.Store;
import com.spring.delivery.repository.StoreRepository;
import com.spring.delivery.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    public void createStore(){
        Store store = new Store();
        Address address = new Address();
        address.setCity("구미시");
        address.setStreet("대학로");
        address.setZipcode("111");
        store.setName("맘터 1호점");
//        store.setAddress(address);
        store.setPhoneNum("12345678");
        store.setOpenTime(9);
        store.setClosedTime(18);
        store.setId(10L);
        storeRepository.save(store);
    }
}
