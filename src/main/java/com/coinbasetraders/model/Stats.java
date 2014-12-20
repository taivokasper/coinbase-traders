package com.coinbasetraders.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Stats {
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
}
