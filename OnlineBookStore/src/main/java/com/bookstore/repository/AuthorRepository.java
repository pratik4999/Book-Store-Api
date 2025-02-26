package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
	
	boolean existsByName(String name);  // Check if an author exists by name

    Author findByName(String name);  // Retrieve an author by name

}
