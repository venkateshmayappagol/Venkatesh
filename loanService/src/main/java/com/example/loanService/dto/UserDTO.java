package com.example.loanService.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserDTO {
    private String userName;
    private String password;
    private String email;
    private BigDecimal totalLateFee = BigDecimal.ZERO;
}
