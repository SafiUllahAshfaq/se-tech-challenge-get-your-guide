package com.getyourguide.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityResponseDto {
    private Long id;
    private String title;
    private int price;
    private String currency;
    private double rating;
    private boolean specialOffer;
    private SupplierInfo supplier;

    @Data
    @Builder
    public static class SupplierInfo {
        private String name;
        private String location;
    }
}