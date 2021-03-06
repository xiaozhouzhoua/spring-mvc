package com.spring.springmvc.controller;

import com.spring.springmvc.model.Stock;
import com.spring.springmvc.model.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @PostMapping("/validate/user/v1")
    public Object validateUserV1(@Valid @RequestBody User user) {
        return "cool";
    }

    @PostMapping("/validate/user/v2")
    public Object validateUserV2(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors();
        } else {
            return "cool";
        }
    }

    @PostMapping("/price")
    public Map<String, String> getPrice() {
        Map<String, String> result = new HashMap<>();
        result.put("APPL", "USD101.00");
        result.put("AMZN", "USD3298.75");
        result.put("TSLA", "USD701.98");
        return result;
    }
}
