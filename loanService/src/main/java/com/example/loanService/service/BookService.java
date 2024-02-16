package com.example.loanService.service;

import com.example.loanService.dto.BookDTO;
import com.example.loanService.entity.Book;
import com.example.loanService.entity.BookStatus;
import com.example.loanService.exception.BookNotFoundException;
import com.example.loanService.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book saveBook(BookDTO bookDTO) {
        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .ISBN(bookDTO.getISBN())
                .status(BookStatus.AVAILABLE)
                .build();
        return bookRepository.save(book);
    }

    public Book findBookById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book Record not found "));
    }
}
