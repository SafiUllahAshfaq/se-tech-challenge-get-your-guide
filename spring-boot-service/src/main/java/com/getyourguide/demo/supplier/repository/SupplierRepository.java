package com.getyourguide.demo.supplier.repository;

import com.getyourguide.demo.supplier.model.Supplier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class SupplierRepository {
    private List<Supplier> suppliers;
    private final ObjectMapper objectMapper;

    public SupplierRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        suppliers = objectMapper.readValue(
                new ClassPathResource("static/suppliers.json").getInputStream(),
                new TypeReference<List<Supplier>>() {});
    }

    public Optional<Supplier> findById(Long id) {
        return suppliers.stream()
                .filter(supplier -> supplier.getId().equals(id))
                .findFirst();
    }
}