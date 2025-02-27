package com.bookstore.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Author")

public class Author {

	@Id
	@Column(nullable = false)
	private long id;
	@Column(nullable = false)
	private String name;
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Book> books;

}
