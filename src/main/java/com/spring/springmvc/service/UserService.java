package com.spring.springmvc.service;

import com.spring.springmvc.dto.UserRegistration;
import com.spring.springmvc.entity.UserDO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDO save(UserRegistration userRegistration);
}
