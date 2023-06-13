package com.spring.delivery.service;

import com.spring.delivery.domain.DiscountPolicy;
import com.spring.delivery.domain.MenuType;
import com.spring.delivery.dto.MenuDiscountPolicyDTO;
import com.spring.delivery.dto.MenuInfoDTO;
import com.spring.delivery.dto.MenuRegisterDTO;
import com.spring.delivery.dto.MenuUpdateDTO;
import com.spring.delivery.exception.DuplicatedMenuException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    MenuService menuService;

    @Test
    @DisplayName("메뉴 등록 테스트")
    void create() {
        MenuRegisterDTO menuRegisterDTO = new MenuRegisterDTO(
                "싸이플렉스버거",
                MenuType.MAIN.name().toLowerCase(),
                7000,
                "이 햄버거는 무척 맛있다",
                "image001",
                1L
        );
        Long newMenuId = menuService.create(menuRegisterDTO);

        assertThat(menuService.findMenuInfo(newMenuId).getName())
                .as("등록된 메뉴와 조회한 메뉴의 이름이 같아야 함")
                .isEqualTo("싸이플렉스버거");
    }

    @Test
//    @DisplayName("동일한 메뉴명 예외 처리 테스트")
    void createAfter() {
        MenuRegisterDTO menuRegisterDTO = new MenuRegisterDTO(
                "싸이플렉스버거",
                MenuType.MAIN.name().toLowerCase(),
                7000,
                "이 햄버거는 무척 맛있다",
                "image001",
                1L
        );

        assertThatThrownBy(() -> menuService.create(menuRegisterDTO))
                .as("이미 등록된 메뉴를 등록하면 중복 예외가 발생해야 함")
                .isInstanceOf(DuplicatedMenuException.class);
    }

    @Test
//    @DisplayName("메뉴 이름,가격,설명 수정 테스트")
    @Transactional
    void updateMenu() {
        MenuUpdateDTO menuUpdateDTO = MenuUpdateDTO.builder()
                .name("싸이플렉스버거")
                .updatedName("싸이흠뻑플렉스버거")
                .price(8000)
                .description("이 햄버거는 물 300L를 사용해서 만들었습니다")
                .build();
        Long updateMenuId = menuService.updateMenu(menuUpdateDTO);
        MenuInfoDTO afterMenu = menuService.findMenuInfo(updateMenuId);

        assertThat(List.of(menuUpdateDTO.getUpdatedName(), menuUpdateDTO.getPrice(), menuUpdateDTO.getDescription()))
                .as("수정할 메뉴와 수정된 메뉴의 이름, 가격, 설명이 같아야 함")
                .isEqualTo(List.of(afterMenu.getName(), afterMenu.getPrice(), afterMenu.getDescription()));
    }

    @Test
//    @DisplayName("할인 정책 설정 테스트")
    @Transactional
    void applyMenuPolicy() {
        MenuRegisterDTO menuRegisterDTO = new MenuRegisterDTO(
                "마라싸이버거",
                MenuType.MAIN.name(),
                7000,
                "이 햄버거는 무척 맛있다",
                "image001",
                15L
        );
        menuService.create(menuRegisterDTO);
        MenuDiscountPolicyDTO menuDiscountPolicyDTO =
                new MenuDiscountPolicyDTO("마라싸이버거", DiscountPolicy.PERCENTAGE.name());
        Long menuId = menuService.applyMenuPolicy(menuDiscountPolicyDTO);

        assertThat(menuService.findMenuInfo(menuId).getDiscountPolicy())
                .as("정률로 수정된 버거의 정책이 정률이어야 함")
                .isEqualTo(DiscountPolicy.PERCENTAGE.name().toLowerCase());
    }
}