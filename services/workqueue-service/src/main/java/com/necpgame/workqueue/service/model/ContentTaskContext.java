package com.necpgame.workqueue.service.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ContentTaskContext(
        String eventType,
        String note,
        List<String> restRefs,
        Map<String, Object> attributes,
        int priority
) {
    private static final int DEFAULT_PRIORITY = 3;

    public ContentTaskContext {
        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException("eventType required");
        }
        note = normalize(note);
        restRefs = sanitizeRefs(restRefs);
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        priority = priority > 0 ? priority : DEFAULT_PRIORITY;
    }

    private static List<String> sanitizeRefs(List<String> source) {
        if (source == null || source.isEmpty()) {
            return List.of();
        }
        LinkedHashSet<String> refs = new LinkedHashSet<>();
        source.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(ref -> !ref.isEmpty())
                .forEach(refs::add);
        return List.copyOf(refs);
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

