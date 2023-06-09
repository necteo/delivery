package com.spring.delivery.service;

import com.spring.delivery.domain.Store;
import com.spring.delivery.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public Store createStore(Store store){
        return storeRepository.save(store);
    }
}
