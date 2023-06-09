package com.spring.delivery.controller;

import com.spring.delivery.dto.MenuDTO;
import com.spring.delivery.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/create")
    public void registerMenu(@RequestBody MenuDTO menuDTO){
        //menuService.create(menuDTO);
    }

    @GetMapping("/list")
    public void findAllMenu(){
        //menuService.findAllMenus();
    }

    @GetMapping("/detail")
    public void findMenuInfo(@RequestParam Long menuId){
        //menuService.findOneMenu(menuId);
    }

    @PostMapping("/update")
    public void updateMenu(@RequestBody MenuDTO menuDTO){
        //menuService.update(menuDTO);
    }
    @PostMapping("/discount/list")
    public void findAllDiscountPolicy(){
        //menuService.findAllDiscountPolicy();
    }
    @PostMapping("/discount/apply")
    public void applyMenuPolicy(@RequestBody MenuDTO menuDTO){
        //menuService.updateMenuPolicy();
    }

    @GetMapping("/statistic")
    public void findstatistics(){

    }
}
