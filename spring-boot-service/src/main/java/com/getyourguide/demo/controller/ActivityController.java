package com.getyourguide.demo.controller;

import com.getyourguide.demo.dto.ActivityResponseDto;
import com.getyourguide.demo.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ActivityResponseDto>> searchActivities(
            @RequestParam String title) {
        return ResponseEntity.ok(activityService.searchByTitle(title));
    }
}
