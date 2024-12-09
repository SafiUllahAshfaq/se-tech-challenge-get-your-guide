package com.getyourguide.demo.activities.repository;

import com.getyourguide.demo.activities.model.Activity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository handling activity data access.
 * Currently implements in-memory storage loaded from a JSON file.
 */
@Repository
public class ActivityRepository {
    private List<Activity> activities;
    private final ObjectMapper objectMapper;

    public ActivityRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Initializes the repository by loading activities from a JSON file.
     * Called automatically after bean construction.
     */
    @PostConstruct
    public void init() throws IOException {
        activities = objectMapper.readValue(
                new ClassPathResource("static/activities.json").getInputStream(),
                new TypeReference<List<Activity>>() {});
    }

    /**
     * Retrieves all activities from the repository.
     * @return Unfiltered list of all activities
     */
    public List<Activity> findAll() {
        return activities;
    }

    /**
     * Searches for activities whose titles contain the given search term (case-insensitive).
     *
     * @param title The search term to match against activity titles
     * @return List of activities matching the search criteria
     */
    public List<Activity> findByTitleContainingIgnoreCase(String title) {
        return activities.stream()
                .filter(activity -> activity.getTitle().toLowerCase()
                        .contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
}