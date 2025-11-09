package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioKPI {

    @NotNull
    @Min(0)
    @Max(100)
    @JsonProperty("successRate")
    private BigDecimal successRate;

    @NotNull
    @Min(0)
    @Max(200)
    @JsonProperty("efficiency")
    private BigDecimal efficiency;

    @JsonProperty("timeToComplete")
    private BigDecimal timeToComplete;

    @JsonProperty("resourcesUsed")
    private Map<String, BigDecimal> resourcesUsed = new HashMap<>();

    @JsonProperty("rewardsEarned")
    private Map<String, BigDecimal> rewardsEarned = new HashMap<>();

    @JsonProperty("costsIncurred")
    private Map<String, BigDecimal> costsIncurred = new HashMap<>();

    @JsonProperty("issues")
    private List<String> issues = new ArrayList<>();
}


