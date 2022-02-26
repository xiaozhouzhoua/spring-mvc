package com.spring.springmvc.dto;

import lombok.Data;

@Data
public class UserRegistration {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
