package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	 Optional<User> findByname(String name);
}
