package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioBlueprintPublishRequest {

    public enum VisibilityScope {
        @JsonProperty("private")
        PRIVATE,
        @JsonProperty("faction")
        FACTION,
        @JsonProperty("marketplace")
        MARKETPLACE
    }

    @NotNull
    @JsonProperty("publish")
    private Boolean publish;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("visibilityScope")
    private VisibilityScope visibilityScope;
}


