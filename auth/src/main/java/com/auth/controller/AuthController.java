package com.auth.controller;

import com.auth.config.JWTUtil;
import com.auth.model.User;
import com.auth.service.AuthenticationService;
import com.auth.util.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationService authService;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ResponseModel> signup(@RequestBody User userDTO) {
        User user = authService.signup(userDTO);
        return ResponseEntity.ok(new ResponseModel(HttpStatus.OK, "Signup Successfully", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> login(@RequestBody User userDTO) {
        String token = authService.login(userDTO);
        return ResponseEntity.ok(new ResponseModel(HttpStatus.OK, "Login Successfully", token));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ResponseModel> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("validating the token : {}", authorizationHeader);

        // Check if the Authorization header is provided
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization is null or not starts with Bearer");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(HttpStatus.BAD_REQUEST, "Invalid Authorization Header", null));
        }

        // Extract token from the Authorization header
        String token = authorizationHeader.substring(7);

        // Validate the token
        if (jwtUtil.isTokenExpired(token)) {
            log.info("Token is expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseModel(HttpStatus.UNAUTHORIZED, "Token is expired", null));
        }

        // Extract the username from the token
        String username = jwtUtil.extractUsername(token);
        if (username == null) {
            log.info("can not extract the username from token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseModel(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }

        // Optionally, you can also check if the token is valid for a specific user (e.g., check if user exists in your database)
        boolean isValid = jwtUtil.validateToken(token, username);

        if (isValid) {
            log.info("Token is validated successfully...");
            return ResponseEntity.ok(new ResponseModel(HttpStatus.OK, "Token is valid", token));
        } else {
            log.info("Token is not valid at last and sending final response");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseModel(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }
}
