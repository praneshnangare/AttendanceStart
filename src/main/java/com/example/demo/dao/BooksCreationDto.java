package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entities.Book;

public class BooksCreationDto {
    private List<Book> books;

    // default and parameterized constructor

    public void addBook(Book book) {
        this.books.add(book);
    }

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public BooksCreationDto() {
		super();
		this.books = new ArrayList<Book>();
	}
	
    // getter and setter
    
}
