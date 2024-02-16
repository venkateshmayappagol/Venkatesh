package com.example.loanService.dto;

import com.example.loanService.entity.Book;
import com.example.loanService.entity.BookStatus;
import com.example.loanService.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
public class LoanDTO {

    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long bookId;


    /*
    private LocalDate issueDate;
    private LocalDate dueDate;
    */

}
