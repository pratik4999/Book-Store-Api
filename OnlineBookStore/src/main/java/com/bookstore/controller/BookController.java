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

	@GetMapping
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}

	@GetMapping("/{id}")
	public Book getBookId(@PathVariable Long id) {
		return bookService.searchBookByID(id);
	}

//	@PostMapping()
//	public ResponseEntity<Book> addBook(@RequestBody Book book) {
//		Author author = book.getAuthor();
//		
//		System.out.println(book.getAuthor());
//		
//		if (author == null) {
//			System.out.println("this is null author");
//			author = authorService.addAuthor(new Author());
//			System.out.println("this is null author 1");
//			
//		} else {
//			System.out.println("this is full author");
//			author = authorService.serachById(author.getId());
//			System.out.println("this is full author");
//		}
//		book.setAuthor(author);
//		Book saveBook = bookService.addBook(book);
//		return ResponseEntity.status(HttpStatus.OK).body(saveBook);
//	}

	
	@PostMapping()
	public void  addBook(@RequestBody Book book) {
	    Author author;
	    
	    try {
	        author = authorService.serachById(book.getAuthor().getId());
	    } catch (BookNotFoundException e) {
	        author = new Author();
	        author.setId(book.getAuthor().getId());
	        author.setName(book.getAuthor().getName());
	        author = authorService.addAuthor(author);
	    }
        
	    book.setAuthor(author);
	    bookService.addBook(book);
	}
	
	
	@DeleteMapping(("/{id}"))
	public void deleteBook(@PathVariable Long id) {
		bookService.removeBook(id);
	}

}
