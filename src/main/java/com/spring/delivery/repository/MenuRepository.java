package com.spring.delivery.repository;

import com.spring.delivery.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuRepository extends JpaRepository <Menu, Long> {
    List<Menu> findByName(String name);

    Menu findOneById(Long id);

}
