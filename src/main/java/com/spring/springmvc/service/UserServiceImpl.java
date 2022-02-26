package com.spring.springmvc.service;

import com.spring.springmvc.dao.UserDao;
import com.spring.springmvc.dto.UserRegistration;
import com.spring.springmvc.entity.RoleDO;
import com.spring.springmvc.entity.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDO save(UserRegistration userRegistration) {
        UserDO user = new UserDO(userRegistration.getEmail(), userRegistration.getFirstName(), userRegistration.getLastName(),
                passwordEncoder.encode(userRegistration.getPassword()), Arrays.asList(new RoleDO("ROLE_USER")));
        return userDao.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userDao.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid UserName");
        }
        return new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleDO> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
