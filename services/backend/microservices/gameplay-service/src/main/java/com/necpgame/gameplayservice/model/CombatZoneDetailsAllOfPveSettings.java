package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CombatZoneDetailsAllOfPveSettings
 */

@JsonTypeName("CombatZoneDetails_allOf_pve_settings")

public class CombatZoneDetailsAllOfPveSettings {

  private @Nullable String aiDifficulty;

  @Valid
  private List<String> enemyTypes = new ArrayList<>();

  public CombatZoneDetailsAllOfPveSettings aiDifficulty(@Nullable String aiDifficulty) {
    this.aiDifficulty = aiDifficulty;
    return this;
  }

  /**
   * Get aiDifficulty
   * @return aiDifficulty
   */
  
  @Schema(name = "ai_difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ai_difficulty")
  public @Nullable String getAiDifficulty() {
    return aiDifficulty;
  }

  public void setAiDifficulty(@Nullable String aiDifficulty) {
    this.aiDifficulty = aiDifficulty;
  }

  public CombatZoneDetailsAllOfPveSettings enemyTypes(List<String> enemyTypes) {
    this.enemyTypes = enemyTypes;
    return this;
  }

  public CombatZoneDetailsAllOfPveSettings addEnemyTypesItem(String enemyTypesItem) {
    if (this.enemyTypes == null) {
      this.enemyTypes = new ArrayList<>();
    }
    this.enemyTypes.add(enemyTypesItem);
    return this;
  }

  /**
   * Get enemyTypes
   * @return enemyTypes
   */
  
  @Schema(name = "enemy_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enemy_types")
  public List<String> getEnemyTypes() {
    return enemyTypes;
  }

  public void setEnemyTypes(List<String> enemyTypes) {
    this.enemyTypes = enemyTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatZoneDetailsAllOfPveSettings combatZoneDetailsAllOfPveSettings = (CombatZoneDetailsAllOfPveSettings) o;
    return Objects.equals(this.aiDifficulty, combatZoneDetailsAllOfPveSettings.aiDifficulty) &&
        Objects.equals(this.enemyTypes, combatZoneDetailsAllOfPveSettings.enemyTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aiDifficulty, enemyTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatZoneDetailsAllOfPveSettings {\n");
    sb.append("    aiDifficulty: ").append(toIndentedString(aiDifficulty)).append("\n");
    sb.append("    enemyTypes: ").append(toIndentedString(enemyTypes)).append("\n");
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

