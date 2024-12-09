package com.getyourguide.demo.supplier.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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