package com.getyourguide.demo.activities.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Activity {
    private Long id;
    private String title;
    private BigDecimal price;
    private String currency;
    private double rating;
    private boolean specialOffer;
    private Long supplierId;
}