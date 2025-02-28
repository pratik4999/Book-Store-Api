package com.bookstore.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.utill.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		String name = oauth2User.getAttribute("name"); // Extract name from Google OAuth2

		Optional<User> existingUser = userRepository.findByname(name);
		User user;

		if (existingUser.isPresent()) {
			user = existingUser.get();
		} else {
			// Auto-register user if they don’t exist
			user = new User();
			user.setName(name);
			user.setRole("ROLE_USER"); // Assign default role
			userRepository.save(user);
		}

		// Convert User entity to UserDetails object
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
			user.getName(), "", // No password required for OAuth users
			Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
		);

		// Generate JWT token using UserDetails
		String token = jwtUtil.generateToken(userDetails);

		// Send JWT token in response
		response.setContentType("application/json");
		response.getWriter().write("{ \"token\": \"" + token + "\" }");
		response.getWriter().flush();
	}
}
