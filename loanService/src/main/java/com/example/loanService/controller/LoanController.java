package com.example.loanService.controller;

import com.example.loanService.dto.ClearLateFeeDTO;
import com.example.loanService.dto.LoanDTO;
import com.example.loanService.dto.LoanReturnDTO;
import com.example.loanService.entity.LoanRecord;
import com.example.loanService.exception.LoanNotFoundException;
import com.example.loanService.exception.ResourceNotFoundException;
import com.example.loanService.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping("/docker")
    public String getDockerMessage(){
        return "Welcome to Docker";
    }
    @PostMapping("/issueBook")
    public ResponseEntity<?> issueBook(@RequestBody LoanDTO loanDTO) {
        try {
            return loanService.issue(loanDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users/{userId}")
    public List<LoanRecord> getLoanRecordsForUser(@PathVariable Long userId) {
        return loanService.getLoanRecords(userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLoanRecordForBook(@PathVariable Long userId, @RequestParam(required = true) Long bookId) {
        try {
            return loanService.getLoanRecord(bookId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnLoanedBook(@RequestBody LoanReturnDTO loanReturnDTO) throws ResourceNotFoundException {
        try {
            return loanService.returnBook(loanReturnDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/returnLateFee/{loanId}")
    public ResponseEntity<?> lateFees(@PathVariable Long loanId) throws LoanNotFoundException, ResourceNotFoundException {
        try {
            return loanService.returnBook(loanId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/clearLateFee")
    public ResponseEntity<?> clearLateFee(@RequestBody ClearLateFeeDTO clearlateFeeDTO) throws ResourceNotFoundException {
        try {
            return loanService.clearLateFees(clearlateFeeDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
