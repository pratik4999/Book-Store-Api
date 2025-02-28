package com.bookstore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.bookstore.controller.OAuth2LoginSuccessHandler;
import com.bookstore.filters.JwtAuthenticationFilter;
import com.bookstore.filters.LoggingFilter;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.CustomOAuth2UserService;
import com.bookstore.utill.JwtUtil;

@Configuration
public class SecurityConfig {

	private final LoggingFilter loggingFilter;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(LoggingFilter loggingFilter, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.loggingFilter = loggingFilter;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
																												// session
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/register", "/auth/login").permitAll() // Public
																													// endpoints
						.requestMatchers("/oauth2/**").permitAll().requestMatchers("/books/add", "/books/delete")
						.hasAuthority("ROLE_ADMIN") // Admin-only endpoints
						.requestMatchers("/books/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // Users & Admins
																									// access
						.anyRequest().authenticated()) // All other requests require authentication
														// service// Handle successful OAuth2 login
				.addFilterBefore(jwtAuthenticationFilter,
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class) // JWT
																													// filter
																													// before
																													// authentication
				.addFilterBefore(loggingFilter, JwtAuthenticationFilter.class) // Logging filter before JWT filter
				.httpBasic(httpBasic -> {
				}); // Keep HTTP Basic enabled (optional)

		return http.build();
	}

//	@Bean
//	CustomOAuth2UserService customOAuth2UserService() {
//		return new CustomOAuth2UserService();
//	}
//
//	@Bean
//	AuthenticationSuccessHandler oAuth2LoginSuccessHandler(UserRepository userRepository, JwtUtil jwtUtil) {
//		return new OAuth2LoginSuccessHandler(userRepository, jwtUtil);
//	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
