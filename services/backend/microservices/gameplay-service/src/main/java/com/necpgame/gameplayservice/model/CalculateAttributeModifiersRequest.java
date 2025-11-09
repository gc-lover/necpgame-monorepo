package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CalculateAttributeModifiersRequest
 */

@JsonTypeName("calculateAttributeModifiers_request")

public class CalculateAttributeModifiersRequest {

  private @Nullable UUID characterId;

  private Boolean includeEquipment = true;

  private Boolean includeBuffs = true;

  public CalculateAttributeModifiersRequest characterId(@Nullable UUID characterId) {
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

  public CalculateAttributeModifiersRequest includeEquipment(Boolean includeEquipment) {
    this.includeEquipment = includeEquipment;
    return this;
  }

  /**
   * Get includeEquipment
   * @return includeEquipment
   */
  
  @Schema(name = "include_equipment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("include_equipment")
  public Boolean getIncludeEquipment() {
    return includeEquipment;
  }

  public void setIncludeEquipment(Boolean includeEquipment) {
    this.includeEquipment = includeEquipment;
  }

  public CalculateAttributeModifiersRequest includeBuffs(Boolean includeBuffs) {
    this.includeBuffs = includeBuffs;
    return this;
  }

  /**
   * Get includeBuffs
   * @return includeBuffs
   */
  
  @Schema(name = "include_buffs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("include_buffs")
  public Boolean getIncludeBuffs() {
    return includeBuffs;
  }

  public void setIncludeBuffs(Boolean includeBuffs) {
    this.includeBuffs = includeBuffs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateAttributeModifiersRequest calculateAttributeModifiersRequest = (CalculateAttributeModifiersRequest) o;
    return Objects.equals(this.characterId, calculateAttributeModifiersRequest.characterId) &&
        Objects.equals(this.includeEquipment, calculateAttributeModifiersRequest.includeEquipment) &&
        Objects.equals(this.includeBuffs, calculateAttributeModifiersRequest.includeBuffs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, includeEquipment, includeBuffs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateAttributeModifiersRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    includeEquipment: ").append(toIndentedString(includeEquipment)).append("\n");
    sb.append("    includeBuffs: ").append(toIndentedString(includeBuffs)).append("\n");
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

