package com.getyourguide.demo.activities.service;

import com.getyourguide.demo.activities.dto.ActivityResponseDto;
import com.getyourguide.demo.activities.dto.ActivitySearchCriteria;
import com.getyourguide.demo.activities.model.Activity;
import com.getyourguide.demo.activities.repository.ActivityRepository;
import com.getyourguide.demo.supplier.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer handling business logic for activities.
 * Coordinates between repository and controller, handles data transformation.
 */
@Service
public class ActivityService {
    private final ActivityRepository repository;
    private final SupplierService supplierService;

    public ActivityService(
            ActivityRepository repository,
            SupplierService supplierService) {
        this.repository = repository;
        this.supplierService = supplierService;
    }

    /**
     * Searches for activities based on provided criteria and transforms them to DTOs.
     * If no title is provided, returns all activities.
     *
     * @param criteria Search criteria containing optional title filter
     * @return List of activities matching the criteria, transformed to DTOs
     */
    @NonNull
    public List<ActivityResponseDto> searchActivities(@NonNull ActivitySearchCriteria criteria) {
        if (!criteria.hasTitle()) {
            return repository.findAll().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        }

        return repository.findByTitleContainingIgnoreCase(criteria.getTitleTrimmed()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Maps an Activity entity to its DTO representation.
     * Includes supplier information if available.
     *
     * @param activity The activity entity to transform
     * @return DTO containing activity information and associated supplier details
     */
    @NonNull
    private ActivityResponseDto mapToDto(@NonNull Activity activity) {
        // Get supplier information if supplier ID exists
        ActivityResponseDto.SupplierInfo supplierInfo = null;
        if (activity.getSupplierId() != null) {
            supplierInfo = supplierService.getSupplierById(activity.getSupplierId())
                    .map(supplier -> ActivityResponseDto.SupplierInfo.builder()
                            .name(supplier.getName())
                            .location(supplier.getLocation())
                            .build())
                    .orElse(null);
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