package com.getyourguide.demo.activities.validation;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.getyourguide.demo.common.exception.ValidationException;

import java.util.regex.Pattern;

@Component
public class SearchParameterValidator {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final Pattern DANGEROUS_PATTERN = Pattern.compile("[<>]|javascript:|onerror=|onload=");
    private static final Pattern VALID_CHARS = Pattern.compile("^[a-zA-Z0-9\\s.,!?\"':-]*$");

    public String validateAndSanitizeTitle(String title) {
        if (title == null) {
            return null;
        }

        String trimmed = title.trim();

        // Length validation
        if (trimmed.length() > MAX_TITLE_LENGTH) {
            throw new ValidationException(
                    String.format("Title length must not exceed %d characters", MAX_TITLE_LENGTH));
        }

        // Check for dangerous patterns first
        if (DANGEROUS_PATTERN.matcher(trimmed).find()) {
            throw new ValidationException("Title contains potentially dangerous characters");
        }

        // Validate allowed characters
        if (!VALID_CHARS.matcher(trimmed).matches()) {
            throw new ValidationException(
                    "Title can only contain letters, numbers, spaces, and basic punctuation (.,!?'\"-)");
        }

        return HtmlUtils.htmlEscape(trimmed);
    }
}