package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioExecutionResponse {

    public enum ExecutionStatus {
        @JsonProperty("pending")
        PENDING,
        @JsonProperty("scheduled")
        SCHEDULED,
        @JsonProperty("running")
        RUNNING
    }

    @NotNull
    @JsonProperty("instanceId")
    private UUID instanceId;

    @NotNull
    @JsonProperty("status")
    private ExecutionStatus status;

    @JsonProperty("scheduledAt")
    private OffsetDateTime scheduledAt;

    @Min(0)
    @JsonProperty("queuePosition")
    private Integer queuePosition;
}


