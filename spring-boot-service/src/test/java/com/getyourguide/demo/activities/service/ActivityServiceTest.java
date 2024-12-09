package com.getyourguide.demo.activities.service;

import com.getyourguide.demo.activities.dto.ActivityResponseDto;
import com.getyourguide.demo.activities.dto.ActivitySearchCriteria;
import com.getyourguide.demo.activities.model.Activity;
import com.getyourguide.demo.activities.repository.ActivityRepository;
import com.getyourguide.demo.supplier.model.Supplier;
import com.getyourguide.demo.supplier.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private SupplierService supplierService;

    private ActivityService activityService;
    private Activity testActivity1;
    private Activity testActivity2;
    private Supplier testSupplier;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(activityRepository, supplierService);

        // Setup test data
        testActivity1 = Activity.builder()
                .id(1L)
                .title("Paris City Tour")
                .price(new BigDecimal("99.99"))
                .currency("EUR")
                .rating(4.5)
                .specialOffer(true)
                .supplierId(1L)
                .build();

        testActivity2 = Activity.builder()
                .id(2L)
                .title("London Walking Tour")
                .price(new BigDecimal("49.99"))
                .currency("GBP")
                .rating(4.8)
                .specialOffer(false)
                .build();

        testSupplier = Supplier.builder()
                .id(1L)
                .name("Paris Tours Inc")
                .address("123 Rue de Paris")
                .city("Paris")
                .zip("75001")
                .country("France")
                .build();
    }

    /**
     *
     * Group 1: Basic Search Functionality Tests
     *
     */
    @Test
    void searchActivities_WhenNoTitleProvided_ReturnsAllActivities() {
        // Arrange
        List<Activity> activities = Arrays.asList(testActivity1, testActivity2);
        when(activityRepository.findAll()).thenReturn(activities);
        when(supplierService.getSupplierById(1L)).thenReturn(Optional.of(testSupplier));

        ActivitySearchCriteria criteria = ActivitySearchCriteria.builder().build();

        // Act
        List<ActivityResponseDto> result = activityService.searchActivities(criteria);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(testActivity1.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(testActivity1.getTitle());
    }

    @Test
    void searchActivities_WhenTitleProvided_ReturnsFilteredActivities() {
        // Arrange
        when(activityRepository.findByTitleContainingIgnoreCase("Paris"))
                .thenReturn(Collections.singletonList(testActivity1));
        when(supplierService.getSupplierById(1L)).thenReturn(Optional.of(testSupplier));

        ActivitySearchCriteria criteria = ActivitySearchCriteria.builder()
                .title("Paris")
                .build();

        // Act
        List<ActivityResponseDto> result = activityService.searchActivities(criteria);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("Paris");
    }

    /**
     *
     * Group 2: Title Search Edge Cases
     *
     */
    @Test
    void searchActivities_WhenTitleIsNull_ReturnsAllActivities() {
        // Arrange
        List<Activity> activities = Arrays.asList(testActivity1, testActivity2);
        when(activityRepository.findAll()).thenReturn(activities);

        ActivitySearchCriteria criteria = ActivitySearchCriteria.builder()
                .title(null)
                .build();

        // Act & Assert
        assertThat(activityService.searchActivities(criteria)).hasSize(2);
    }

    @Test
    void searchActivities_WhenTitleIsEmpty_ReturnsAllActivities() {
        // Arrange
        List<Activity> activities = Arrays.asList(testActivity1, testActivity2);
        when(activityRepository.findAll()).thenReturn(activities);

        ActivitySearchCriteria criteria = ActivitySearchCriteria.builder()
                .title("")
                .build();

        // Act & Assert
        assertThat(activityService.searchActivities(criteria)).hasSize(2);
    }

    @Test
    void searchActivities_WhenNoMatchingTitle_ReturnsEmptyList() {
        // Arrange
        when(activityRepository.findByTitleContainingIgnoreCase(anyString()))
                .thenReturn(Collections.emptyList());

        ActivitySearchCriteria criteria = ActivitySearchCriteria.builder()
                .title("NonExistent")
                .build();

        // Act & Assert
        assertThat(activityService.searchActivities(criteria)).isEmpty();
    }

    @Test
    void searchActivities_WhenTitleHasSpecialCharacters_ReturnsMatchingActivities() {
        // Arrange
        Activity specialActivity = Activity.builder()
                .id(3L)
                .title("Tour & Eiffel Tower!")
                .price(new BigDecimal("99.99"))
                .currency("EUR")
                .build();

        when(activityRepository.findByTitleContainingIgnoreCase("&"))
                .thenReturn(Collections.singletonList(specialActivity));

        // Act & Assert
        List<ActivityResponseDto> result = activityService.searchActivities(
                ActivitySearchCriteria.builder().title("&").build());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("&");
    }

    /**
     *
     * Group 3: DTO Mapping and Supplier Relationship Tests
     *
     */
    @Test
    void mapToDto_MapsAllFieldsCorrectly() {
        // Arrange
        when(activityRepository.findByTitleContainingIgnoreCase(anyString()))
                .thenReturn(Collections.singletonList(testActivity1));
        when(supplierService.getSupplierById(1L)).thenReturn(Optional.of(testSupplier));

        // Act
        ActivityResponseDto result = activityService.searchActivities(
                ActivitySearchCriteria.builder().title("Paris").build())
                .stream()
                .findFirst()
                .orElse(null);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testActivity1.getId());
        assertThat(result.getTitle()).isEqualTo(testActivity1.getTitle());
        assertThat(result.getPrice()).isEqualTo(testActivity1.getPrice());
        assertThat(result.getCurrency()).isEqualTo(testActivity1.getCurrency());
        assertThat(result.getRating()).isEqualTo(testActivity1.getRating());
        assertThat(result.isSpecialOffer()).isEqualTo(testActivity1.isSpecialOffer());
    }

    @Test
    void mapToDto_WhenSupplierIdIsNull_SupplierInfoIsNull() {
        // Arrange
        Activity activityWithoutSupplier = Activity.builder()
                .id(3L)
                .title("Solo Activity")
                .price(new BigDecimal("29.99"))
                .currency("USD")
                .supplierId(null)
                .build();

        when(activityRepository.findByTitleContainingIgnoreCase(anyString()))
                .thenReturn(Collections.singletonList(activityWithoutSupplier));

        // Act & Assert
        List<ActivityResponseDto> result = activityService.searchActivities(
                ActivitySearchCriteria.builder().title("Solo").build());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSupplier()).isNull();
    }

    @Test
    void mapToDto_WhenSupplierServiceReturnsEmpty_SupplierInfoIsNull() {
        // Arrange
        when(activityRepository.findByTitleContainingIgnoreCase(anyString()))
                .thenReturn(Collections.singletonList(testActivity1));
        when(supplierService.getSupplierById(anyLong()))
                .thenReturn(Optional.empty());

        // Act & Assert
        List<ActivityResponseDto> result = activityService.searchActivities(
                ActivitySearchCriteria.builder().title("Paris").build());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSupplier()).isNull();
    }
}