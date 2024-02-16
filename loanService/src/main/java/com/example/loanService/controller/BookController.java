package com.example.loanService.controller;

import com.example.loanService.dto.BookDTO;
import com.example.loanService.entity.Book;
import com.example.loanService.exception.BookNotFoundException;
import com.example.loanService.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping
    public Book createBook(@RequestBody BookDTO bookDTO) {
        return bookService.saveBook(bookDTO);

    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) throws BookNotFoundException {
        return bookService.findBookById(id);
    }
}
