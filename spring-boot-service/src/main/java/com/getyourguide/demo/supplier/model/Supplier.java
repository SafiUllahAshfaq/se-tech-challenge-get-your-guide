package com.getyourguide.demo.supplier.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Supplier {
    private Long id;
    private String name;
    private String address;
    private String zip;
    private String city;
    private String country;

    public String getLocation() {
        return String.format("%s, %s %s, %s", address, zip, city, country);
    }
}