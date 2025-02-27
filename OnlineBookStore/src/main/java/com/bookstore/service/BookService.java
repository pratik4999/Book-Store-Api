package com.bookstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository books;
    @Autowired
	private AuthorRepository authorRepo;

    @Transactional(readOnly = true)
    @CacheEvict(value = "booksCache", key = "'allBooks'")  // Cache all books
    public List<Book> getAllBooks() {
        return books.findAll();
    }

    @Cacheable(value = "booksCache", key = "#id")  // Cache individual book by ID
    public Book searchBookByID(long id) {
        return books.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = "booksCache", allEntries = true)  // Clear cache when adding a new book
    public Book addBook(Book book) {
    	
    	
        return books.save(book);
    }


    @Transactional
    @CacheEvict(value = "booksCache", allEntries = true)  // Clear cache when removing a book
    public void removeBook(long id) {
        if (!books.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        books.deleteById(id);
    }
}
