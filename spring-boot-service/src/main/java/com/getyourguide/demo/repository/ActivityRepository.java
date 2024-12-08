package com.getyourguide.demo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.demo.model.Activity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ActivityRepository {
    private List<Activity> activities;
    private final ObjectMapper objectMapper;

    public ActivityRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        // Load activities from JSON file
        activities = objectMapper.readValue(
                new ClassPathResource("static/activities.json").getInputStream(),
                new TypeReference<List<Activity>>() {
                });
    }

    public List<Activity> findAll() {
        return activities;
    }

    public List<Activity> findByTitleContainingIgnoreCase(String title) {
        return activities.stream()
                .filter(activity -> activity.getTitle().toLowerCase()
                        .contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
}