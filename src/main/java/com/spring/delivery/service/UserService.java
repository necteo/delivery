package com.spring.delivery.service;

import com.spring.delivery.domain.User;
import com.spring.delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User register(User user){

        return userRepository.save(user);
    }
    public boolean validateCheck(User user){

        return true;
    }
}
