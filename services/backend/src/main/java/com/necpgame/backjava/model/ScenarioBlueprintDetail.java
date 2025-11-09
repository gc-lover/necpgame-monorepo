package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioBlueprintDetail {

    @NotNull
    @Valid
    @JsonProperty("summary")
    private ScenarioBlueprintSummary summary;

    @NotNull
    @Size(min = 1)
    @Valid
    @JsonProperty("steps")
    private List<ScenarioStep> steps = new ArrayList<>();

    @JsonProperty("conditions")
    private Map<String, Object> conditions = new LinkedHashMap<>();

    @Valid
    @JsonProperty("rewards")
    private ScenarioReward rewards;

    @Valid
    @JsonProperty("costs")
    private ScenarioCost costs;

    @JsonProperty("automationHints")
    private List<String> automationHints = new ArrayList<>();

    @Size(max = 2000)
    @JsonProperty("verificationNotes")
    private String verificationNotes;
}


