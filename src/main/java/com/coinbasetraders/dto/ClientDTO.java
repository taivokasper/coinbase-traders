package com.coinbasetraders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private String randomId;
    private String apiKey;
    private String apiSecret;
    private BigDecimal limit;
    private BigDecimal amount;
    private String type;
}