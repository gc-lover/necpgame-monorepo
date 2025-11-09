package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ScenarioStep {

    public enum StepType {
        @JsonProperty("action")
        ACTION,
        @JsonProperty("condition")
        CONDITION,
        @JsonProperty("loop")
        LOOP,
        @JsonProperty("parallel")
        PARALLEL
    }

    @NotBlank
    @JsonProperty("id")
    private String id;

    @Min(1)
    @JsonProperty("order")
    private int order;

    @NotNull
    @JsonProperty("type")
    private StepType type;

    @NotBlank
    @JsonProperty("action")
    private String action;

    @JsonProperty("parameters")
    private Map<String, Object> parameters = new HashMap<>();

    @JsonProperty("conditions")
    private Map<String, Object> conditions = new HashMap<>();

    @JsonProperty("onSuccess")
    private String onSuccess;

    @JsonProperty("onFailure")
    private String onFailure;

    @JsonProperty("timeout")
    private BigDecimal timeout;
}


