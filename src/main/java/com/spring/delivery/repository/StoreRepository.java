package com.spring.delivery.repository;

import com.spring.delivery.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository <Store, Long> {

}
