package com.spring.delivery.repository;

import com.spring.delivery.domain.Menu;
import com.spring.delivery.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository <Store, Long> {
    List<Store> findByName(String name);
    Store findOneById(Long id);
}
