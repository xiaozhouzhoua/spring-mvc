package com.spring.springmvc.controller;

import com.spring.springmvc.model.StockSubscription;
import com.spring.springmvc.model.StockSymbol;
import com.spring.springmvc.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static com.spring.springmvc.Constants.TEST_USER_EMAIL;

@Controller
@RequestMapping("/subscriptions/async")
public class SubscriptionControllerAsync {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public String addSubscription(@ModelAttribute(value = "stockSymbol") StockSymbol symbol) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        subscriptionService.addSubscriptionAsync(name, symbol.getSymbol());
        return "redirect:/subscriptions?added=" + symbol.getSymbol();
    }

    @GetMapping
    public String subscription(Model model) throws ExecutionException, InterruptedException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // get()方法阻塞
        List<StockSubscription> subscriptions = subscriptionService.findByEmailAsync(name).get();
        model.addAttribute("email", name);
        model.addAttribute("subscriptions", subscriptions);

        return "subscription";
    }

    /**
     * 使用DeferredResult来尽快返回，不会因为get阻塞
     * 后台线程去执行
     */
    @GetMapping("subscriptionToDef")
    public DeferredResult<String> subscriptionToDef(Model model) {
        DeferredResult<String> result = new DeferredResult<>();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        ForkJoinPool.commonPool().submit(() -> {
            List<StockSubscription> subscriptions = null;
            try {
                subscriptions = subscriptionService.findByEmailAsync(name).get();
                model.addAttribute("email", name);
                model.addAttribute("subscriptions", subscriptions);
                result.setResult("subscription");
            } catch (Exception e) {
                result.setErrorResult(e);
            }
        });
        return result;
    }

    /**
     * 使用Callable返回，不会因为get阻塞
     * 后台线程去执行
     */
    @GetMapping("subscriptionToCall")
    public Callable<String> subscriptionToCall(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Callable<String> callable = () -> {
            List<StockSubscription> subscriptions = subscriptionService.findByEmailAsync(name).get();
            model.addAttribute("email", name);
            model.addAttribute("subscriptions", subscriptions);
            return "subscription";
        };
        return callable;
    }

    /**
     * 使用Callable返回，不会因为get阻塞
     * 后台线程去执行
     */
    @GetMapping("subscriptionToWeb")
    public WebAsyncTask<String> subscriptionToWeb(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Callable<String> callable = () -> {
            List<StockSubscription> subscriptions = subscriptionService.findByEmailAsync(name).get();
            model.addAttribute("email", name);
            model.addAttribute("subscriptions", subscriptions);
            return "subscription";
        };
        return new WebAsyncTask<>(callable);
    }

    @GetMapping("subscriptionByCf")
    public CompletableFuture<String> subscriptionByCf(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        CompletableFuture<String> getSubscriptionModelFuture = subscriptionService.findByEmailByCf(name)
                .thenApplyAsync((subscriptions) -> {
                    model.addAttribute("email", name);
                    model.addAttribute("subscriptions", subscriptions);
                    return "subscription";
                });
        return getSubscriptionModelFuture;
    }

    @GetMapping("subscriptionByReactor")
    public Mono<String> subscriptionByReactor(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        final Mono<String> mono = subscriptionService.findByEmailReactor(name).reduceWith(ArrayList::new, (result, item) -> {
            result.add(item);
            return result;
        }).map((subscriptions) -> {
            model.addAttribute("email", name);
            model.addAttribute("subscriptions", subscriptions);
            return "subscription";
        });
        return mono;
    }
}
