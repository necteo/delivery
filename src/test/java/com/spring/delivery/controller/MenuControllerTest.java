package com.spring.delivery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.delivery.WithMockCustomUser;
import com.spring.delivery.domain.MenuType;
import com.spring.delivery.dto.MenuDiscountPolicyDTO;
import com.spring.delivery.dto.MenuRegisterDTO;
import com.spring.delivery.dto.MenuUpdateDTO;
import com.spring.delivery.oauth.token.AuthToken;
import com.spring.delivery.oauth.token.AuthTokenProvider;
import com.spring.delivery.service.MenuService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
class MenuControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuService menuService;

    @MockBean
    AuthTokenProvider tokenProvider;

    @Test
    @WithMockCustomUser
    void registerMenu() throws Exception {
        MenuRegisterDTO menuRegisterDTO = new MenuRegisterDTO(
                "싸이플렉스버거",
                MenuType.MAIN.toString().toLowerCase(),
                7000,
                "이 햄버거는 무척 맛있다",
                "image001",
                1L
        );
        String accessToken = "";

        given(menuService.create(menuRegisterDTO)).willReturn(1L);
        String content = new ObjectMapper().writeValueAsString(menuRegisterDTO);

        mockMvc.perform(
                        post("/api/menu/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateMenu() throws Exception {
        MenuUpdateDTO menuUpdateDTO = new MenuUpdateDTO(
                "싸이플렉스버거",
                "싸이플렉스버거",
                8000,
                "이 햄버거는 무척 맛있다 하지만 비싸다"
        );
//        given(menuService.updateMenu(menuUpdateDTO)).willReturn(3L);
        String content = new ObjectMapper().writeValueAsString(menuUpdateDTO);
        String accessToken = "";

        mockMvc.perform(
                        post("/api/menu/update")
                                .header("Authorization", "Bearer " + accessToken)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void applyMenuPolicy() throws Exception {
        MenuDiscountPolicyDTO menuDiscountPolicyDTO = new MenuDiscountPolicyDTO(
                "싸이플렉스버거",
                "percentage"
        );
//        given(menuService.applyMenuPolicy(menuDiscountPolicyDTO)).willReturn(3L);
        String content = new ObjectMapper().writeValueAsString(menuDiscountPolicyDTO);
        String accessToken = "";

        mockMvc.perform(
                        post("/api/menu/discount/apply")
                                .header("Authorization", "Bearer " + accessToken)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}