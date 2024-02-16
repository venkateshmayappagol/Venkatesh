package com.example.loanService.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ClearLateFeeDTO {
    private Long loanId;
    private BigDecimal lateFee;
}
