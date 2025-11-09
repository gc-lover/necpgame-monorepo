package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.FactionEvolutionEvolutionStagesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FactionEvolution
 */


public class FactionEvolution {

  private @Nullable String factionId;

  private @Nullable String factionName;

  @Valid
  private List<@Valid FactionEvolutionEvolutionStagesInner> evolutionStages = new ArrayList<>();

  public FactionEvolution factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public FactionEvolution factionName(@Nullable String factionName) {
    this.factionName = factionName;
    return this;
  }

  /**
   * Get factionName
   * @return factionName
   */
  
  @Schema(name = "faction_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_name")
  public @Nullable String getFactionName() {
    return factionName;
  }

  public void setFactionName(@Nullable String factionName) {
    this.factionName = factionName;
  }

  public FactionEvolution evolutionStages(List<@Valid FactionEvolutionEvolutionStagesInner> evolutionStages) {
    this.evolutionStages = evolutionStages;
    return this;
  }

  public FactionEvolution addEvolutionStagesItem(FactionEvolutionEvolutionStagesInner evolutionStagesItem) {
    if (this.evolutionStages == null) {
      this.evolutionStages = new ArrayList<>();
    }
    this.evolutionStages.add(evolutionStagesItem);
    return this;
  }

  /**
   * Get evolutionStages
   * @return evolutionStages
   */
  @Valid 
  @Schema(name = "evolution_stages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evolution_stages")
  public List<@Valid FactionEvolutionEvolutionStagesInner> getEvolutionStages() {
    return evolutionStages;
  }

  public void setEvolutionStages(List<@Valid FactionEvolutionEvolutionStagesInner> evolutionStages) {
    this.evolutionStages = evolutionStages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionEvolution factionEvolution = (FactionEvolution) o;
    return Objects.equals(this.factionId, factionEvolution.factionId) &&
        Objects.equals(this.factionName, factionEvolution.factionName) &&
        Objects.equals(this.evolutionStages, factionEvolution.evolutionStages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, factionName, evolutionStages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionEvolution {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    factionName: ").append(toIndentedString(factionName)).append("\n");
    sb.append("    evolutionStages: ").append(toIndentedString(evolutionStages)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

