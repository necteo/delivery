package com.spring.delivery.service;

import com.spring.delivery.domain.User;
import com.spring.delivery.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(User user){

        return userRepository.save(user);
    }
    public boolean validateCheck(User user){

        return true;
    }
}
