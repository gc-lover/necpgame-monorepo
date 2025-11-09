package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioBlueprintCreateRequest {

    @NotBlank
    @Size(max = 150)
    @JsonProperty("name")
    private String name;

    @NotBlank
    @Size(max = 2000)
    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("authorId")
    private UUID authorId;

    @NotNull
    @JsonProperty("category")
    private ScenarioCategory category;

    @NotNull
    @Size(min = 1)
    @JsonProperty("requiredRoles")
    private List<String> requiredRoles = new ArrayList<>();

    @Size(max = 32)
    @JsonProperty("version")
    private String version;

    @JsonProperty("parameters")
    private Map<String, Object> parameters = new LinkedHashMap<>();

    @JsonProperty("conditions")
    private Map<String, Object> conditions = new LinkedHashMap<>();

    @NotNull
    @Size(min = 1)
    @Valid
    @JsonProperty("steps")
    private List<ScenarioStep> steps = new ArrayList<>();

    @Valid
    @JsonProperty("rewards")
    private ScenarioReward rewards;

    @Valid
    @JsonProperty("costs")
    private ScenarioCost costs;

    @JsonProperty("isPublic")
    private Boolean isPublic;

    @JsonProperty("price")
    private BigDecimal price;
}


