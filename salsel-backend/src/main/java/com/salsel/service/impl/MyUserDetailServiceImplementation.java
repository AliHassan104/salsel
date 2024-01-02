package com.salsel.service.impl;

import com.salsel.dto.CustomUserDetail;
import com.salsel.model.User;
import com.salsel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailServiceImplementation implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndStatusIsTrue(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetail(user);
    }
}
