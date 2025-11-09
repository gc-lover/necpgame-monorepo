package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioInstance {

    @NotNull
    @JsonProperty("id")
    private UUID id;

    @NotNull
    @JsonProperty("blueprintId")
    private UUID blueprintId;

    @NotNull
    @JsonProperty("npcId")
    private UUID npcId;

    @NotNull
    @JsonProperty("ownerId")
    private UUID ownerId;

    @NotNull
    @JsonProperty("status")
    private ScenarioInstanceStatus status;

    @Min(0)
    @JsonProperty("currentStep")
    private Integer currentStep;

    @JsonProperty("parameters")
    private Map<String, Object> parameters = new LinkedHashMap<>();

    @Valid
    @JsonProperty("kpi")
    private ScenarioKPI kpi;

    @JsonProperty("startedAt")
    private OffsetDateTime startedAt;

    @JsonProperty("completedAt")
    private OffsetDateTime completedAt;

    @JsonProperty("scheduledAt")
    private OffsetDateTime scheduledAt;

    @JsonProperty("duration")
    private BigDecimal duration;

    @JsonProperty("result")
    private Map<String, Object> result = new LinkedHashMap<>();
}


