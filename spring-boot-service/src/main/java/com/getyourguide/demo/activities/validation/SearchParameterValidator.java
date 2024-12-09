package com.getyourguide.demo.activities.validation;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import com.getyourguide.demo.common.exception.ValidationException;

import java.util.regex.Pattern;

/**
 * Validator component for search parameters.
 * Implements security measures and validation rules for search inputs.
 */
@Component
public class SearchParameterValidator {
    // Maximum allowed length for title search parameter
    private static final int MAX_TITLE_LENGTH = 100;

    // Pattern to detect potentially malicious content
    private static final Pattern DANGEROUS_PATTERN = Pattern.compile("[<>]|javascript:|onerror=|onload=");

    // Pattern defining allowed characters in search
    private static final Pattern VALID_CHARS = Pattern.compile("^[a-zA-Z0-9\\s.,!?\"':-]*$");

    /**
     * Validates and sanitizes the title search parameter.
     * Performs the following checks:
     * 1. Length validation (max 100 chars)
     * 2. Dangerous pattern detection (XSS prevention)
     * 3. Character set validation
     * 4. HTML escaping
     *
     * @param title The title to validate and sanitize
     * @return Sanitized title string, or null if input was null
     * @throws ValidationException if the input fails any validation check
     */
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

        // Security validation
        if (DANGEROUS_PATTERN.matcher(trimmed).find()) {
            throw new ValidationException("Title contains potentially dangerous characters");
        }

        // Character set validation
        if (!VALID_CHARS.matcher(trimmed).matches()) {
            throw new ValidationException(
                    "Title can only contain letters, numbers, spaces, and basic punctuation (.,!?'\"-)");
        }

        // Final sanitization step
        return HtmlUtils.htmlEscape(trimmed);
    }
}