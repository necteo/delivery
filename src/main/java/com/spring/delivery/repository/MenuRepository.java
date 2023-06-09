package com.spring.delivery.repository;

import com.spring.delivery.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository <Menu, Long> {

}
