package com.getyourguide.demo.supplier.service;

import com.getyourguide.demo.supplier.model.Supplier;
import com.getyourguide.demo.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }
}