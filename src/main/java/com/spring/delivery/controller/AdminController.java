package com.spring.delivery.controller;

import com.spring.delivery.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final StoreService storeService;

    @PostMapping("/create/store")
    public void createStore(@RequestParam Long userId){
        storeService.createStore();
    }
}
