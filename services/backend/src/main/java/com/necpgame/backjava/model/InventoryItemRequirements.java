package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * РўСЂРµР±РѕРІР°РЅРёСЏ РґР»СЏ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёСЏ/СЌРєРёРїРёСЂРѕРІРєРё
 */

@Schema(name = "InventoryItem_requirements", description = "РўСЂРµР±РѕРІР°РЅРёСЏ РґР»СЏ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёСЏ/СЌРєРёРїРёСЂРѕРІРєРё")
@JsonTypeName("InventoryItem_requirements")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class InventoryItemRequirements {

  private @Nullable Integer minLevel;

  private @Nullable Integer minStrength;

  private @Nullable Integer minDexterity;

  private @Nullable Integer minIntelligence;

  public InventoryItemRequirements minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Get minLevel
   * @return minLevel
   */
  
  @Schema(name = "minLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLevel")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public InventoryItemRequirements minStrength(@Nullable Integer minStrength) {
    this.minStrength = minStrength;
    return this;
  }

  /**
   * Get minStrength
   * @return minStrength
   */
  
  @Schema(name = "minStrength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minStrength")
  public @Nullable Integer getMinStrength() {
    return minStrength;
  }

  public void setMinStrength(@Nullable Integer minStrength) {
    this.minStrength = minStrength;
  }

  public InventoryItemRequirements minDexterity(@Nullable Integer minDexterity) {
    this.minDexterity = minDexterity;
    return this;
  }

  /**
   * Get minDexterity
   * @return minDexterity
   */
  
  @Schema(name = "minDexterity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minDexterity")
  public @Nullable Integer getMinDexterity() {
    return minDexterity;
  }

  public void setMinDexterity(@Nullable Integer minDexterity) {
    this.minDexterity = minDexterity;
  }

  public InventoryItemRequirements minIntelligence(@Nullable Integer minIntelligence) {
    this.minIntelligence = minIntelligence;
    return this;
  }

  /**
   * Get minIntelligence
   * @return minIntelligence
   */
  
  @Schema(name = "minIntelligence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minIntelligence")
  public @Nullable Integer getMinIntelligence() {
    return minIntelligence;
  }

  public void setMinIntelligence(@Nullable Integer minIntelligence) {
    this.minIntelligence = minIntelligence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryItemRequirements inventoryItemRequirements = (InventoryItemRequirements) o;
    return Objects.equals(this.minLevel, inventoryItemRequirements.minLevel) &&
        Objects.equals(this.minStrength, inventoryItemRequirements.minStrength) &&
        Objects.equals(this.minDexterity, inventoryItemRequirements.minDexterity) &&
        Objects.equals(this.minIntelligence, inventoryItemRequirements.minIntelligence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minLevel, minStrength, minDexterity, minIntelligence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryItemRequirements {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    minStrength: ").append(toIndentedString(minStrength)).append("\n");
    sb.append("    minDexterity: ").append(toIndentedString(minDexterity)).append("\n");
    sb.append("    minIntelligence: ").append(toIndentedString(minIntelligence)).append("\n");
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


