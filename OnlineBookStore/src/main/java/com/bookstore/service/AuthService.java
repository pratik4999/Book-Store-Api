package com.bookstore.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;

@Service
public class AuthService {

	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;

	    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public void registerUser(String username, String password, String role) {
	        User user = new User();
	        user.setName(username);
	        user.setPassword(passwordEncoder.encode(password)); // Hash password
	        user.setRole(role);
	        userRepository.save(user);
	    }
}
