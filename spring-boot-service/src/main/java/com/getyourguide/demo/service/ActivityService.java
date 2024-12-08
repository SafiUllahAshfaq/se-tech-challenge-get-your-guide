package com.getyourguide.demo.service;

import com.getyourguide.demo.model.Activity;
import com.getyourguide.demo.dto.ActivityResponseDto;
import com.getyourguide.demo.model.Supplier;
import com.getyourguide.demo.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<ActivityResponseDto> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ActivityResponseDto mapToDto(Activity activity) {
        Optional<Supplier> supplier = activityRepository.findSupplierById(activity.getSupplierId());

        ActivityResponseDto.SupplierInfo supplierInfo = supplier.map(s -> ActivityResponseDto.SupplierInfo.builder()
                .name(s.getName())
                .location(String.format("%s, %s", s.getCity(), s.getCountry()))
                .build()).orElse(null);

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