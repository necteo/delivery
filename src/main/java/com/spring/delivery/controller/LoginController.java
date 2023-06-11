package com.spring.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {
    private final SimpMessageSendingOperations messagingTemplate;

//    @PostMapping("/login")
//    public void login(@Validated @RequestBody LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request){
//        User loginMember = loginService.login(loginForm.getLoginId(), loginForm.getLoginPw());
//        if(loginMember == null){
//            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
//        }
//
//        HttpSession session = request.getSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//    }
//
//    @GetMapping("/logout")
//    public void logout(HttpServletRequest request){
//        HttpSession session = request.getSession(false);
//        if(session!=null){
//            session.invalidate();
//        }
//    }
}