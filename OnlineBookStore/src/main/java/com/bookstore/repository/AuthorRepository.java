package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{

}
