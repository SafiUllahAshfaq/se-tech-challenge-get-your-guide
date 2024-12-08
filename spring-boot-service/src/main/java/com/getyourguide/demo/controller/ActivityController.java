package com.getyourguide.demo.controller;

import com.getyourguide.demo.dto.ActivityResponseDto;
import com.getyourguide.demo.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/activities")
    public ResponseEntity<List<ActivityResponseDto>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/debug")
    public ResponseEntity<Void> debug(
            @RequestParam(name = "title", required = false, defaultValue = "NONE") String title,
            Model model) {
        try {
            model.addAttribute("title", title);
            var activities = activityService.getAllActivities();
            model.addAttribute("activities", activities);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}