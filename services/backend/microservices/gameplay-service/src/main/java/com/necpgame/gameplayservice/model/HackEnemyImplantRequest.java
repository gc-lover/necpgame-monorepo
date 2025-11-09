package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HackEnemyImplantRequest
 */

@JsonTypeName("hackEnemyImplant_request")

public class HackEnemyImplantRequest {

  private String characterId;

  private String targetId;

  /**
   * Gets or Sets hackType
   */
  public enum HackTypeEnum {
    DISABLE("disable"),
    
    CONTROL("control"),
    
    OVERLOAD("overload"),
    
    DATA_STEAL("data_steal");

    private final String value;

    HackTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static HackTypeEnum fromValue(String value) {
      for (HackTypeEnum b : HackTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private HackTypeEnum hackType;

  private @Nullable String quickhackId;

  public HackEnemyImplantRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HackEnemyImplantRequest(String characterId, String targetId, HackTypeEnum hackType) {
    this.characterId = characterId;
    this.targetId = targetId;
    this.hackType = hackType;
  }

  public HackEnemyImplantRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public HackEnemyImplantRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull 
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_id")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public HackEnemyImplantRequest hackType(HackTypeEnum hackType) {
    this.hackType = hackType;
    return this;
  }

  /**
   * Get hackType
   * @return hackType
   */
  @NotNull 
  @Schema(name = "hack_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hack_type")
  public HackTypeEnum getHackType() {
    return hackType;
  }

  public void setHackType(HackTypeEnum hackType) {
    this.hackType = hackType;
  }

  public HackEnemyImplantRequest quickhackId(@Nullable String quickhackId) {
    this.quickhackId = quickhackId;
    return this;
  }

  /**
   * ID quickhack для использования
   * @return quickhackId
   */
  
  @Schema(name = "quickhack_id", description = "ID quickhack для использования", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quickhack_id")
  public @Nullable String getQuickhackId() {
    return quickhackId;
  }

  public void setQuickhackId(@Nullable String quickhackId) {
    this.quickhackId = quickhackId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackEnemyImplantRequest hackEnemyImplantRequest = (HackEnemyImplantRequest) o;
    return Objects.equals(this.characterId, hackEnemyImplantRequest.characterId) &&
        Objects.equals(this.targetId, hackEnemyImplantRequest.targetId) &&
        Objects.equals(this.hackType, hackEnemyImplantRequest.hackType) &&
        Objects.equals(this.quickhackId, hackEnemyImplantRequest.quickhackId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, hackType, quickhackId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackEnemyImplantRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    hackType: ").append(toIndentedString(hackType)).append("\n");
    sb.append("    quickhackId: ").append(toIndentedString(quickhackId)).append("\n");
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

