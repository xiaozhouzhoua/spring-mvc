package com.spring.springmvc.mock;

import com.spring.springmvc.controller.ApiController;
import com.spring.springmvc.controller.SubscriptionController;
import com.spring.springmvc.model.StockSubscription;
import com.spring.springmvc.service.SubscriptionService;
import com.spring.springmvc.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * WebMvcTest注解主要用于controller层测试，只覆盖应用程序的controller层，HTTP请求和响应是Mock出来的，
 * 因此不会创建真正的连接也包括数据库。因此需要创建MockMvc bean进行模拟接口调用。
 * 如果Controller层对Service层中的其他bean有依赖关系，那么需要使用Mock提供所需的依赖项。
 * WebMvcTest要快得多，因为我们只加载了应用程序的一小部分。
 *
 * SpringBootTest注解告诉SpringBoot去寻找一个主配置类(例如带有@SpringBootApplication的配置类)，
 * 并使用它来启动Spring应用程序上下文。SpringBootTest加载完整的应用程序并注入所有可能的bean，因此速度会很慢。
 */
@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
    * Security相关bean需要进行加载
    */
    @MockBean
    private UserService userService;

    @Test
    public void checkReturn() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/price"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.APPL").value("USD101.00"));
    }
}
