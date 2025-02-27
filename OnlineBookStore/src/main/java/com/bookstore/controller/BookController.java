package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private AuthorService authorService;

	@GetMapping("/all")
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}

	@GetMapping("/{id}")
	public Book getBookId(@PathVariable Long id) {
		return bookService.searchBookByID(id);
	}

	@PostMapping("/add")
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		authorService.addAuthor(book);
		Book savedBook = bookService.addBook(book);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
	}
	

	@DeleteMapping(("/delete/{id}"))
	public void deleteBook(@PathVariable Long id) {
		authorService.removeAuthor(id);

	}

}
