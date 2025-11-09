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
public class ScenarioReward {

    @JsonProperty("rewards")
    private Map<String, BigDecimal> rewards = new HashMap<>();

    @JsonProperty("reputationChange")
    private Map<String, Integer> reputationChange = new HashMap<>();

    @JsonProperty("experienceGain")
    private BigDecimal experienceGain;
}


