package com.getyourguide.demo.activities.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActivityResponseDto {
    Long id;
    String title;
    BigDecimal price;
    String currency;
    double rating;
    boolean specialOffer;
    SupplierInfo supplier;

    @Value
    @Builder
    public static class SupplierInfo {
        String name;
        String location;
    }
}