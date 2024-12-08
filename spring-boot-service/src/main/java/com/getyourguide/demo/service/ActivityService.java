package com.getyourguide.demo.service;

import com.getyourguide.demo.model.Activity;
import com.getyourguide.demo.dto.ActivityResponseDto;
import com.getyourguide.demo.repository.ActivityRepository;
import com.getyourguide.demo.model.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository repository;
    private final SupplierService supplierService;

    public ActivityService(ActivityRepository repository, SupplierService supplierService) {
        this.repository = repository;
        this.supplierService = supplierService;
    }

    public List<ActivityResponseDto> getAllActivities() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityResponseDto> searchByTitle(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllActivities();
        }
        return repository.findByTitleContainingIgnoreCase(searchTerm.trim()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ActivityResponseDto mapToDto(Activity activity) {
        ActivityResponseDto.SupplierInfo supplierInfo;

        if (activity.getSupplierId() != null) {
            Supplier supplier = supplierService.getSupplierById(activity.getSupplierId())
                    .orElse(null);

            supplierInfo = supplier != null ? ActivityResponseDto.SupplierInfo.builder()
                    .name(supplier.getName())
                    .location(supplier.getLocation())
                    .build() : null;
        } else {
            supplierInfo = null;
        }

        return ActivityResponseDto.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .price(activity.getPrice())
                .currency(activity.getCurrency())
                .rating(activity.getRating())
                .specialOffer(activity.isSpecialOffer())
                .supplier(supplierInfo)
                .build();
    }
}