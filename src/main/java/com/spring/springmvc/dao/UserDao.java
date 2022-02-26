package com.spring.springmvc.dao;

import com.spring.springmvc.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserDO, Long> {
    UserDO findByEmail(String email);
}
