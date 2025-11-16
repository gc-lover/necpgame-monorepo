package com.necpgame.workqueue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "workqueue.ingestion")
public class WorkqueueIngestionProperties {
    private String systemRole = "concept-director";
    private String creationSegment = "concept";
    private List<String> allowedSegments = new ArrayList<>(List.of(
            "concept",
            "vision",
            "api",
            "backend",
            "frontend",
            "qa",
            "release",
            "analytics",
            "community",
            "security",
            "data",
            "ux",
            "refactor"
    ));

    public String getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public List<String> getAllowedSegments() {
        return allowedSegments;
    }

    public void setAllowedSegments(List<String> allowedSegments) {
        this.allowedSegments = allowedSegments;
    }

    public String getCreationSegment() {
        return creationSegment;
    }

    public void setCreationSegment(String creationSegment) {
        this.creationSegment = creationSegment;
    }
}

