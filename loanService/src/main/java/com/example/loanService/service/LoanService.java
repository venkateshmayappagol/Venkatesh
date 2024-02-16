package com.example.loanService.service;

import com.example.loanService.dto.ClearLateFeeDTO;
import com.example.loanService.dto.LoanDTO;
import com.example.loanService.dto.LoanReturnDTO;
import com.example.loanService.entity.Book;
import com.example.loanService.entity.BookStatus;
import com.example.loanService.entity.LoanRecord;
import com.example.loanService.entity.User;
import com.example.loanService.exception.BookNotFoundException;
import com.example.loanService.exception.LoanNotFoundException;
import com.example.loanService.exception.ResourceNotFoundException;
import com.example.loanService.exception.UserNotFoundException;
import com.example.loanService.repository.BookRepository;
import com.example.loanService.repository.LoanRepository;
import com.example.loanService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.loanService.entity.BookStatus.AVAILABLE;
import static com.example.loanService.entity.BookStatus.LOANED;
import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    // private BookStatus status;


    public List<LoanRecord> getLoanRecords(Long userId) {
        List<LoanRecord> loanRecords = loanRepository.findByUserId(userId);
        return loanRecords;
    }

    public ResponseEntity<?> getLoanRecord(Long bookId) {
        LoanRecord loanRecord = loanRepository.findByBookId(bookId);
        return new ResponseEntity<>(loanRecord.getDueDate(), HttpStatus.OK);
    }

    public ResponseEntity<?> issue(LoanDTO loanDTO) throws UserNotFoundException, BookNotFoundException {
        User user = userRepository.findById(loanDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User Record not found "));
        List<LoanRecord> loanRecords = loanRepository.findByUserId(user.getId());
        int limit = 4;
        Book book = bookRepository.findById(loanDTO.getBookId()).orElseThrow(() -> new BookNotFoundException("Book Record not found "));
        if (loanRecords.size() < limit && AVAILABLE == book.getStatus()) {
            LoanRecord loanRecord = LoanRecord.builder()
                    .userId(user.getId())
                    .bookId(loanDTO.getBookId())
                    .issueDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(14))
                    .build();
            book.setTitle(book.getTitle());
            book.setISBN(book.getISBN());
            book.setStatus(LOANED);
            bookRepository.save(book);
            return ResponseEntity.ok(loanRepository.save(loanRecord));
        }
        return new ResponseEntity<>(new IllegalArgumentException("Book is not available for Loan or Exceeded the maximum loan limit"), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> returnBook(LoanReturnDTO loanReturnDTO) throws ResourceNotFoundException {
        LoanRecord loanRecord = loanRepository.findById(loanReturnDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("LoanRecord Not Found"));
        Book book = bookRepository.findById(loanRecord.getBookId()).orElse(null);
        if (loanRecord != null && LOANED == book.getStatus()) {
            loanRecord.setReturnDate(LocalDate.now());
            book.setStatus(AVAILABLE);
            return new ResponseEntity<>(loanRepository.save(loanRecord), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new ResourceNotFoundException("Loan Record not found"), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> returnBook(Long loanId) throws LoanNotFoundException, ResourceNotFoundException {
        LoanRecord loanRecord = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        BigDecimal lateFee = BigDecimal.ZERO;
        if (LocalDate.now().isAfter(loanRecord.getDueDate())) {
            Long daysLate = DAYS.between(loanRecord.getDueDate(), LocalDate.now());
            lateFee = new BigDecimal(daysLate).multiply(BigDecimal.valueOf(5));
            loanRecord.setLateFee(lateFee);
            loanRepository.save(loanRecord);

            User user = userRepository.findById(loanRecord.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setTotalLateFee(user.getTotalLateFee().add(lateFee));
            userRepository.save(user);
            return new ResponseEntity<>(loanRecord, HttpStatus.CREATED);

        }
        return new ResponseEntity(loanRecord, HttpStatus.CREATED);

    }

    public ResponseEntity<?> clearLateFees(ClearLateFeeDTO clearlateFeeDTO) throws ResourceNotFoundException, UserNotFoundException {
        LoanRecord loanRecord = loanRepository.findById(clearlateFeeDTO.getLoanId()).orElseThrow(() -> new ResourceNotFoundException("Loan Record not found "));

        if (loanRecord.getLateFee().compareTo(BigDecimal.ZERO) > 0) {

            loanRecord.setLateFee(loanRecord.getLateFee().subtract(clearlateFeeDTO.getLateFee()));
            User user = userRepository.findById(loanRecord.getUserId()).orElseThrow(() -> new UserNotFoundException("User Record not found "));
            user.setTotalLateFee(user.getTotalLateFee().subtract(clearlateFeeDTO.getLateFee()));
            loanRepository.save(loanRecord);
            userRepository.save(user);
        }
        return new ResponseEntity<>(loanRecord, HttpStatus.ACCEPTED);
    }
}
