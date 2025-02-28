package com.bookstore.service;

import java.util.Optional;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        // Extract user name from Google OAuth response
        String name = oauth2User.getAttribute("name"); 
        
        // Check if the user already exists
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

        // Return an OAuth2User object with user details
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                oauth2User.getAttributes(),
                "name" // Use "name" as the key
        );
    }
}
