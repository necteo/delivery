package com.spring.delivery.controller;

import com.spring.delivery.domain.Menu;
import com.spring.delivery.domain.Statistics;
import com.spring.delivery.dto.*;
import com.spring.delivery.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    @PostMapping("/create") //메뉴등록
    public void registerMenu(@RequestBody MenuRegisterDTO menuRegisterDTO){
        menuService.create(menuRegisterDTO);
    }

    @GetMapping("/list") // 모든 메뉴 검색
    public List<MenuInfoDTO> findAllMenu(){
        return menuService.findAllMenus();
    }

    @GetMapping("/detail") // 특정 메뉴 정보찾기
    public MenuInfoDTO findMenuInfo(@RequestParam Long menuId){
        return menuService.findMenuInfo(menuId);
    }

    @PostMapping("/update") // 특정 메뉴 수정
    public Long updateMenu(@RequestBody MenuUpdateDTO menuUpdateDTO){
        return menuService.updateMenu(menuUpdateDTO);
    }
    @PostMapping("/discount/list") // 할인 정책 찾기
    public void findAllDiscountPolicy(){
        menuService.findAllDiscountPolicy();
    }
    @PostMapping("/discount/apply") // 할인정책 적용
    public void applyMenuPolicy(@RequestBody MenuDiscountPolicyDTO menuDiscountPolicyDTO){
        menuService.applyMenuPolicy(menuDiscountPolicyDTO);
    }
    @GetMapping("/statistic") //통계 찾기
    public List<StatisticsDTO> findStatistics(){
        return menuService.findStatistics();
    }
}
