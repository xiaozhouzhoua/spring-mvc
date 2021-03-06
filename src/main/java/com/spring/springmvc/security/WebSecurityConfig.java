package com.spring.springmvc.security;

import com.spring.springmvc.security.auth.filter.CustomAuthenticationFilter;
import com.spring.springmvc.security.auth.handler.CustomFailureHandler;
import com.spring.springmvc.security.auth.handler.CustomSuccessHandler;
import com.spring.springmvc.security.auth.provider.CustomAdminAuthenticationProvider;
import com.spring.springmvc.security.auth.provider.SudoAuthenticationProvider;
import com.spring.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用Spring5推荐的bcrypt加密算法加密
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 使用明文密码
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAdminAuthenticationProvider());
//        auth.authenticationProvider(authenticationProvider())
//                .authenticationProvider(new SudoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/registration**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/")
                .successHandler(new CustomSuccessHandler())
                .failureUrl("/login?error=true")
                .failureHandler(new CustomFailureHandler())
                .and()
                .addFilterBefore(new CustomAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .logout().invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/css/**")
                .antMatchers("/webjars/**")
                .antMatchers("/image/**")
                .antMatchers("/api/**")
                .antMatchers("/h2-console/**");
    }
}
