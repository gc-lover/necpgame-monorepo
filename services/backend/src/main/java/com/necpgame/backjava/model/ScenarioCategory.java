package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ScenarioCategory {
    @JsonProperty("economy")
    ECONOMY,
    @JsonProperty("social")
    SOCIAL,
    @JsonProperty("combat")
    COMBAT,
    @JsonProperty("logistics")
    LOGISTICS
}


