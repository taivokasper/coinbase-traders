package com.coinbasetraders.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class Stats {
    private Date date;

    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
}
