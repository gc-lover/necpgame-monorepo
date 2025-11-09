package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ScenarioInstanceStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("running")
    RUNNING,
    @JsonProperty("paused")
    PAUSED,
    @JsonProperty("completed")
    COMPLETED,
    @JsonProperty("failed")
    FAILED,
    @JsonProperty("cancelled")
    CANCELLED
}


