package com.bookstore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.model.User;
import com.bookstore.service.AuthService;
import com.bookstore.utill.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private AuthenticationManager authenticationManager;
	private JwtUtil jwtUtil;
	private UserDetailsService userDetailsService;

	  public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
	        this.authenticationManager = authenticationManager;
	        this.jwtUtil = jwtUtil;
	        this.userDetailsService = userDetailsService;
	    }
	   @PostMapping("/login")
	    public String login(@RequestBody User authRequest) {
	        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
	        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getName());
	        return jwtUtil.generateToken(userDetails);
	    }
	  
	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public String registerUser(@RequestParam String name, @RequestParam String password, @RequestParam String role) {
		authService.registerUser(name, password, role);
		return "registerd User";
	}

}
