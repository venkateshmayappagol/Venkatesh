package com.example.loanService.service;

import com.example.loanService.dto.UserDTO;
import com.example.loanService.entity.User;
import com.example.loanService.exception.ResourceNotFoundException;
import com.example.loanService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(UserDTO userDTO) {
        if (userDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("password must contain atleast 8 characters");
        }
        User userEmail = userRepository.findByEmail(userDTO.getEmail());
        if (userEmail != null) {
            throw new IllegalArgumentException("email already registered");
        }
        User user = User.builder()
                .userName(userDTO.getUserName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .totalLateFee(BigDecimal.ZERO)
                .build();
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
