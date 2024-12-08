package com.getyourguide.demo.service;

import com.getyourguide.demo.model.Activity;
import com.getyourguide.demo.dto.ActivityResponseDto;
import com.getyourguide.demo.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository repository;

    public ActivityService(ActivityRepository repository) {
        this.repository = repository;
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
        // For now, supplier info is null since we don't have supplier data
        ActivityResponseDto.SupplierInfo supplierInfo = ActivityResponseDto.SupplierInfo.builder()
                .name(null)
                .location(null)
                .build();

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