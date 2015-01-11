package com.coinbasetraders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "priceStats")
@Data
@AllArgsConstructor
public class Stats {
    private Date date;

    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
}
