package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioBlueprintUpdateRequest {

    @JsonProperty("name")
    private JsonNullable<@Size(max = 150) String> name = JsonNullable.undefined();

    @JsonProperty("description")
    private JsonNullable<@Size(max = 2000) String> description = JsonNullable.undefined();

    @JsonProperty("version")
    private JsonNullable<@Size(max = 32) String> version = JsonNullable.undefined();

    @JsonProperty("requiredRoles")
    private JsonNullable<List<String>> requiredRoles = JsonNullable.undefined();

    @JsonProperty("parameters")
    private JsonNullable<Map<String, Object>> parameters = JsonNullable.undefined();

    @JsonProperty("conditions")
    private JsonNullable<Map<String, Object>> conditions = JsonNullable.undefined();

    @JsonProperty("steps")
    private JsonNullable<List<@Valid ScenarioStep>> steps = JsonNullable.undefined();

    @JsonProperty("rewards")
    private JsonNullable<@Valid ScenarioReward> rewards = JsonNullable.undefined();

    @JsonProperty("costs")
    private JsonNullable<@Valid ScenarioCost> costs = JsonNullable.undefined();

    @JsonProperty("price")
    private JsonNullable<BigDecimal> price = JsonNullable.undefined();

    @JsonProperty("isPublic")
    private JsonNullable<Boolean> isPublic = JsonNullable.undefined();

    @JsonProperty("isVerified")
    private JsonNullable<Boolean> isVerified = JsonNullable.undefined();
}


