package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioCost {

    public enum RiskLevel {
        @JsonProperty("low")
        LOW,
        @JsonProperty("medium")
        MEDIUM,
        @JsonProperty("high")
        HIGH
    }

    @JsonProperty("costs")
    private Map<String, BigDecimal> costs = new HashMap<>();

    @JsonProperty("riskLevel")
    private RiskLevel riskLevel;

    @JsonProperty("insuranceRequired")
    private Boolean insuranceRequired;
}


