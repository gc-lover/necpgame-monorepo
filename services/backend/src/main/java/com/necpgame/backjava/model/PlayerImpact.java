package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.PlayerImpactImpactsByCategory;
import com.necpgame.backjava.model.PlayerImpactMajorDecisionsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerImpact
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerImpact {

  private @Nullable UUID characterId;

  private @Nullable Integer totalImpactScore;

  private @Nullable PlayerImpactImpactsByCategory impactsByCategory;

  @Valid
  private List<@Valid PlayerImpactMajorDecisionsInner> majorDecisions = new ArrayList<>();

  public PlayerImpact characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public PlayerImpact totalImpactScore(@Nullable Integer totalImpactScore) {
    this.totalImpactScore = totalImpactScore;
    return this;
  }

  /**
   * Общее влияние на мир
   * @return totalImpactScore
   */
  
  @Schema(name = "total_impact_score", description = "Общее влияние на мир", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_impact_score")
  public @Nullable Integer getTotalImpactScore() {
    return totalImpactScore;
  }

  public void setTotalImpactScore(@Nullable Integer totalImpactScore) {
    this.totalImpactScore = totalImpactScore;
  }

  public PlayerImpact impactsByCategory(@Nullable PlayerImpactImpactsByCategory impactsByCategory) {
    this.impactsByCategory = impactsByCategory;
    return this;
  }

  /**
   * Get impactsByCategory
   * @return impactsByCategory
   */
  @Valid 
  @Schema(name = "impacts_by_category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impacts_by_category")
  public @Nullable PlayerImpactImpactsByCategory getImpactsByCategory() {
    return impactsByCategory;
  }

  public void setImpactsByCategory(@Nullable PlayerImpactImpactsByCategory impactsByCategory) {
    this.impactsByCategory = impactsByCategory;
  }

  public PlayerImpact majorDecisions(List<@Valid PlayerImpactMajorDecisionsInner> majorDecisions) {
    this.majorDecisions = majorDecisions;
    return this;
  }

  public PlayerImpact addMajorDecisionsItem(PlayerImpactMajorDecisionsInner majorDecisionsItem) {
    if (this.majorDecisions == null) {
      this.majorDecisions = new ArrayList<>();
    }
    this.majorDecisions.add(majorDecisionsItem);
    return this;
  }

  /**
   * Важные решения персонажа
   * @return majorDecisions
   */
  @Valid 
  @Schema(name = "major_decisions", description = "Важные решения персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("major_decisions")
  public List<@Valid PlayerImpactMajorDecisionsInner> getMajorDecisions() {
    return majorDecisions;
  }

  public void setMajorDecisions(List<@Valid PlayerImpactMajorDecisionsInner> majorDecisions) {
    this.majorDecisions = majorDecisions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerImpact playerImpact = (PlayerImpact) o;
    return Objects.equals(this.characterId, playerImpact.characterId) &&
        Objects.equals(this.totalImpactScore, playerImpact.totalImpactScore) &&
        Objects.equals(this.impactsByCategory, playerImpact.impactsByCategory) &&
        Objects.equals(this.majorDecisions, playerImpact.majorDecisions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, totalImpactScore, impactsByCategory, majorDecisions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerImpact {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    totalImpactScore: ").append(toIndentedString(totalImpactScore)).append("\n");
    sb.append("    impactsByCategory: ").append(toIndentedString(impactsByCategory)).append("\n");
    sb.append("    majorDecisions: ").append(toIndentedString(majorDecisions)).append("\n");
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

