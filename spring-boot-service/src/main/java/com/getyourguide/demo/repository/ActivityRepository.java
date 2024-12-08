package com.getyourguide.demo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.demo.model.Activity;
import com.getyourguide.demo.model.Supplier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ActivityRepository {
    private List<Activity> activities;
    private Map<Long, Supplier> suppliers;
    private final ObjectMapper objectMapper;

    public ActivityRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        // Load activities
        activities = objectMapper.readValue(
                new ClassPathResource("static/activities.json").getInputStream(),
                new TypeReference<List<Activity>>() {
                });

        // Load suppliers and create a map for efficient lookup
        List<Supplier> supplierList = objectMapper.readValue(
                new ClassPathResource("static/suppliers.json").getInputStream(),
                new TypeReference<List<Supplier>>() {
                });
        suppliers = supplierList.stream()
                .collect(Collectors.toMap(Supplier::getId, supplier -> supplier));
    }

    public List<Activity> findAll() {
        return activities;
    }

    public Optional<Supplier> findSupplierById(Long id) {
        return Optional.ofNullable(suppliers.get(id));
    }
}