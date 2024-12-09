package com.getyourguide.demo.activities.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
@Builder
public class ActivitySearchCriteria {
    @Nullable
    String title;

    public boolean hasTitle() {
        return title != null && !title.trim().isEmpty();
    }

    public String getTitleTrimmed() {
        return title != null ? title.trim() : null;
    }
}