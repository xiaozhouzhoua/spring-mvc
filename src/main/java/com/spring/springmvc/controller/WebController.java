package com.spring.springmvc.controller;

import com.spring.springmvc.service.StockPriceService;
import com.spring.springmvc.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import static com.spring.springmvc.Constants.TEST_USER_EMAIL;

@Controller
public class WebController {

    @Autowired
    private SubscriptionService subscriptionServiceImpl;

    @Autowired
    private StockPriceService stockPriceService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("email", TEST_USER_EMAIL);
        model.addAttribute("stockPrices", stockPriceService.getPrice(TEST_USER_EMAIL));
        return "index";
    }
}
