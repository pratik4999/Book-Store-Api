package com.bookstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByname(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Convert roles (e.g., "ROLE_ADMIN,ROLE_USER") into authorities
		List<GrantedAuthority> authorities = List.of(user.getRole().split(",")).stream()
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		return org.springframework.security.core.userdetails.User.builder().username(user.getName()) // Ensuring it
																										// matches DB
				.password(user.getPassword()) // Stored in DB (hashed)
				.authorities(authorities) // Add roles/authorities
				.build();
	}

	//Load user by username explicitly (for token validation)
	public User loadUserEntityByUsername(String username) {
		return userRepository.findByname(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}