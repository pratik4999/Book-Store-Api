package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.bookstore.exception.AuthorNotFoundException;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.repository.AuthorRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepo;

//	public Author addAuthor(Author author ,Book book) {
//		return authorRepo.save(author);
//	}

	public void addAuthor(Book book) {

		if (book.getAuthor() == null) {
			throw new IllegalArgumentException("Book must have an author.");
		}

		Author author;
		try {
			// Step 1: Search for author by ID
			author = serachById(book.getAuthor().getId());

			// Step 2: Check if the existing author's name matches the new author's name
			if (author.getName().equals(book.getAuthor().getName())) {
				throw new IllegalArgumentException(
						"Author ID already exists  with a same name choose different author name");
			}

		} catch (AuthorNotFoundException e) { // Step 2: If not found, handle the exception

			// Step 3: If author not found by ID, check if an author with the same name
			// exists
			if (authorRepo.existsByName(book.getAuthor().getName())) {
				throw new IllegalArgumentException("An author with the same name already exists.");
			}

			// Step 3: Create a new author if not found
			author = new Author();
			author.setId(book.getAuthor().getId());
			author.setName(book.getAuthor().getName());
		}

		authorRepo.save(author);
		book.setAuthor(author);

	}

	public void removeAuthor(long id) {
		authorRepo.deleteById(id);
	}

	public Author serachById(long id) {
		return authorRepo.findById(id)
				.orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
	}

}
