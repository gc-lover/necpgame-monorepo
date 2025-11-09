package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScenarioInstanceListResponse {

    @NotNull
    @Size(min = 0)
    @Valid
    @JsonProperty("data")
    private List<ScenarioInstance> data = new ArrayList<>();

    @NotNull
    @Valid
    @JsonProperty("meta")
    private PaginationMeta meta;
}


