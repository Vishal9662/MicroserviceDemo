package com.auth.service;

import com.auth.config.JWTUtil;
import com.auth.exception.AuthenticationException;
import com.auth.model.User;
import com.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public User signup(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new AuthenticationException("User already exists");
        }

        User saveUser = new User();
        saveUser.setUsername(user.getUsername());
        saveUser.setPassword(passwordEncoder.encode(user.getPassword()));
        saveUser.setEmail(user.getEmail());
        saveUser.setRole(user.getRole());
        return userRepository.save(saveUser);
    }

    public String login(User userDTO) {
        User existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser == null || !passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }
        log.info("User Exists.. - {}", existingUser);
        return jwtUtil.generateToken(existingUser.getUsername(), existingUser.getRole());
    }
}
