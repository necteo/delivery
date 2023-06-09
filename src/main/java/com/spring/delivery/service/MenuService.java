package com.spring.delivery.service;

import com.spring.delivery.domain.Menu;
import com.spring.delivery.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public Menu create(Menu menu){
        return menuRepository.save(menu);
    }
}
