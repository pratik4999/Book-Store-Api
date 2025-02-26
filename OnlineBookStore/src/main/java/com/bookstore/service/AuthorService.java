package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.repository.AuthorRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepo;

	public Author addAuthor(Author author) {
		return authorRepo.save(author);
	}

	public void removeAuthor(long id) {
		authorRepo.deleteById(id);
	}


	public Author serachById(long id) {
		return authorRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
	}
	


}
