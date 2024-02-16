package com.example.loanService.repository;

import com.example.loanService.entity.LoanRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanRecord,Long> {
    List<LoanRecord> findByUserId(Long userId);


    LoanRecord findByBookId(Long bookId);


}
