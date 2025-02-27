package com.bookstore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bookstore.filters.LoggingFilter;

@Configuration
public class SecurityConfig {
	
	private final LoggingFilter loggingFilter;
	
	public SecurityConfig(LoggingFilter loggingFilter) {
		this.loggingFilter=loggingFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csfr -> csfr.disable())
				.authorizeHttpRequests((auth) -> auth.requestMatchers("/books/add", "/books/delete").hasAuthority("ROLE_ADMIN")
						.requestMatchers(("/auth/register")).permitAll()
						.requestMatchers("/books/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER").anyRequest().authenticated()
						).addFilterBefore(loggingFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

				.httpBasic();
		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
