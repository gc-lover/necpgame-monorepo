package com.necpgame.workqueue.service.model;

public record ContentSearchCriteria(
        String typeCode,
        String statusCode,
        String categoryCode,
        String visibilityCode,
        String search
) {
    public boolean hasSearch() {
        return search != null && !search.isBlank();
    }

    public boolean hasType() {
        return typeCode != null && !typeCode.isBlank();
    }

    public boolean hasStatus() {
        return statusCode != null && !statusCode.isBlank();
    }

    public boolean hasCategory() {
        return categoryCode != null && !categoryCode.isBlank();
    }

    public boolean hasVisibility() {
        return visibilityCode != null && !visibilityCode.isBlank();
    }
}


