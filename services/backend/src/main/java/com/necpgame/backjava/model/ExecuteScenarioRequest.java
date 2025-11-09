package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecuteScenarioRequest {

    @NotNull
    @JsonProperty("blueprintId")
    private UUID blueprintId;

    @JsonProperty("parameters")
    private Map<String, Object> parameters = new HashMap<>();

    @JsonProperty("scheduledAt")
    private OffsetDateTime scheduledAt;

    @Min(1)
    @Max(10)
    @JsonProperty("priority")
    private Integer priority;

    @Size(max = 255)
    @JsonProperty("automationRuleId")
    private String automationRuleId;
}


