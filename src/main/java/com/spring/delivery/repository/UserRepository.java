package com.spring.delivery.repository;

import com.spring.delivery.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, String>{
}
