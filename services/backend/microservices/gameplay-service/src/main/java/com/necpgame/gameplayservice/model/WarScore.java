package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WarScore
 */


public class WarScore {

  private @Nullable Integer attacker;

  private @Nullable Integer defender;

  private @Nullable Integer territoriesCaptured;

  private @Nullable Integer objectivesCompleted;

  public WarScore attacker(@Nullable Integer attacker) {
    this.attacker = attacker;
    return this;
  }

  /**
   * Get attacker
   * @return attacker
   */
  
  @Schema(name = "attacker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attacker")
  public @Nullable Integer getAttacker() {
    return attacker;
  }

  public void setAttacker(@Nullable Integer attacker) {
    this.attacker = attacker;
  }

  public WarScore defender(@Nullable Integer defender) {
    this.defender = defender;
    return this;
  }

  /**
   * Get defender
   * @return defender
   */
  
  @Schema(name = "defender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defender")
  public @Nullable Integer getDefender() {
    return defender;
  }

  public void setDefender(@Nullable Integer defender) {
    this.defender = defender;
  }

  public WarScore territoriesCaptured(@Nullable Integer territoriesCaptured) {
    this.territoriesCaptured = territoriesCaptured;
    return this;
  }

  /**
   * Get territoriesCaptured
   * @return territoriesCaptured
   */
  
  @Schema(name = "territoriesCaptured", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territoriesCaptured")
  public @Nullable Integer getTerritoriesCaptured() {
    return territoriesCaptured;
  }

  public void setTerritoriesCaptured(@Nullable Integer territoriesCaptured) {
    this.territoriesCaptured = territoriesCaptured;
  }

  public WarScore objectivesCompleted(@Nullable Integer objectivesCompleted) {
    this.objectivesCompleted = objectivesCompleted;
    return this;
  }

  /**
   * Get objectivesCompleted
   * @return objectivesCompleted
   */
  
  @Schema(name = "objectivesCompleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectivesCompleted")
  public @Nullable Integer getObjectivesCompleted() {
    return objectivesCompleted;
  }

  public void setObjectivesCompleted(@Nullable Integer objectivesCompleted) {
    this.objectivesCompleted = objectivesCompleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarScore warScore = (WarScore) o;
    return Objects.equals(this.attacker, warScore.attacker) &&
        Objects.equals(this.defender, warScore.defender) &&
        Objects.equals(this.territoriesCaptured, warScore.territoriesCaptured) &&
        Objects.equals(this.objectivesCompleted, warScore.objectivesCompleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attacker, defender, territoriesCaptured, objectivesCompleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarScore {\n");
    sb.append("    attacker: ").append(toIndentedString(attacker)).append("\n");
    sb.append("    defender: ").append(toIndentedString(defender)).append("\n");
    sb.append("    territoriesCaptured: ").append(toIndentedString(territoriesCaptured)).append("\n");
    sb.append("    objectivesCompleted: ").append(toIndentedString(objectivesCompleted)).append("\n");
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

