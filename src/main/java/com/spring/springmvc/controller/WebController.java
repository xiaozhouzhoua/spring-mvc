package com.spring.springmvc.controller;

import com.spring.springmvc.async.AsyncConfig;
import com.spring.springmvc.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.concurrent.TimeUnit;

import static com.spring.springmvc.Constants.TEST_USER_EMAIL;

@Controller
public class WebController {

    @Autowired
    private AsyncConfig asyncConfig;

    @Autowired
    private StockPriceService stockPriceService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("email", TEST_USER_EMAIL);
        model.addAttribute("stockPrices", stockPriceService.getPrice(TEST_USER_EMAIL));
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/securityContext")
    public DeferredResult<String> securityContext(Model model) {
        DeferredResult<String> result = new DeferredResult<>();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        asyncConfig.getAsyncExecutor().execute(() -> {
                    try {
                        // 利用委派模式的DelegatingSecurityContextExecutor，可以在自定义线程池中异步线程中获取安全上下文
                        SecurityContextHolder.getContext();
                        model.addAttribute("email", name);
                        model.addAttribute("stockPrices", stockPriceService.getPriceAsync(name).get(5, TimeUnit.SECONDS));
                        result.setResult("index");
                    } catch (Exception e) {
                        result.setErrorResult(e);
                    }
                }
        );
        return result;
    }
}
