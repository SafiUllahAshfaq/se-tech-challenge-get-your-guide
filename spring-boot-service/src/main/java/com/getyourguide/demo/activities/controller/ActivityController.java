package com.getyourguide.demo.activities.controller;

import com.getyourguide.demo.activities.dto.ActivityResponseDto;
import com.getyourguide.demo.activities.dto.ActivitySearchCriteria;
import com.getyourguide.demo.activities.service.ActivityService;
import com.getyourguide.demo.activities.validation.SearchParameterValidator;
import com.getyourguide.demo.common.dto.ErrorResponse;
import com.getyourguide.demo.common.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final SearchParameterValidator searchValidator;

    public ActivityController(
            ActivityService activityService,
            SearchParameterValidator searchValidator) {
        this.activityService = activityService;
        this.searchValidator = searchValidator;
    }

    /**
     * Retrieves activities based on an optional title search parameter.
     *
     * @param title Optional search term to filter activities by title
     * @return List of activities matching the search criteria
     *
     * @example GET /activities - Returns all activities
     * @example GET /activities?title=Paris - Returns activities containing "Paris" in title
     */
    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getActivities(
            @RequestParam(required = false) String title) {
        // Validate and sanitize the search title parameter
        String sanitizedTitle = searchValidator.validateAndSanitizeTitle(title);

        ActivitySearchCriteria searchCriteria = ActivitySearchCriteria.builder()
                .title(sanitizedTitle)
                .build();

        return ResponseEntity.ok(activityService.searchActivities(searchCriteria));
    }

    /**
     * Global exception handler for validation errors.
     * Converts ValidationException to a proper error response with BAD_REQUEST status.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}