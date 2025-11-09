package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
public class ScenarioBlueprintSummary {

    @NotNull
    @JsonProperty("id")
    private UUID id;

    @NotBlank
    @Size(max = 150)
    @JsonProperty("name")
    private String name;

    @Size(max = 2000)
    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("authorId")
    private UUID authorId;

    @NotBlank
    @Size(max = 32)
    @JsonProperty("version")
    private String version;

    @NotNull
    @JsonProperty("category")
    private ScenarioCategory category;

    @NotNull
    @Size(min = 1)
    @JsonProperty("requiredRoles")
    private List<String> requiredRoles = new ArrayList<>();

    @JsonProperty("parameters")
    private Map<String, Object> parameters = new LinkedHashMap<>();

    @NotNull
    @JsonProperty("isPublic")
    private Boolean isPublic;

    @NotNull
    @JsonProperty("isVerified")
    private Boolean isVerified;

    @JsonProperty("price")
    private BigDecimal price;

    @Valid
    @JsonProperty("createdAt")
    private OffsetDateTime createdAt;

    @Valid
    @JsonProperty("updatedAt")
    private OffsetDateTime updatedAt;
}


