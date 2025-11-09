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
 * PerformStealthKillCheckRequest
 */

@JsonTypeName("performStealthKillCheck_request")

public class PerformStealthKillCheckRequest {

  private String characterId;

  private String targetId;

  /**
   * Gets or Sets approachType
   */
  public enum ApproachTypeEnum {
    BEHIND("behind"),
    
    FROM_ABOVE("from_above"),
    
    DISTRACTED("distracted"),
    
    SLEEPING("sleeping");

    private final String value;

    ApproachTypeEnum(String value) {
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
    public static ApproachTypeEnum fromValue(String value) {
      for (ApproachTypeEnum b : ApproachTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ApproachTypeEnum approachType;

  public PerformStealthKillCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformStealthKillCheckRequest(String characterId, String targetId, ApproachTypeEnum approachType) {
    this.characterId = characterId;
    this.targetId = targetId;
    this.approachType = approachType;
  }

  public PerformStealthKillCheckRequest characterId(String characterId) {
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

  public PerformStealthKillCheckRequest targetId(String targetId) {
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

  public PerformStealthKillCheckRequest approachType(ApproachTypeEnum approachType) {
    this.approachType = approachType;
    return this;
  }

  /**
   * Get approachType
   * @return approachType
   */
  @NotNull 
  @Schema(name = "approach_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("approach_type")
  public ApproachTypeEnum getApproachType() {
    return approachType;
  }

  public void setApproachType(ApproachTypeEnum approachType) {
    this.approachType = approachType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformStealthKillCheckRequest performStealthKillCheckRequest = (PerformStealthKillCheckRequest) o;
    return Objects.equals(this.characterId, performStealthKillCheckRequest.characterId) &&
        Objects.equals(this.targetId, performStealthKillCheckRequest.targetId) &&
        Objects.equals(this.approachType, performStealthKillCheckRequest.approachType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, approachType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformStealthKillCheckRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    approachType: ").append(toIndentedString(approachType)).append("\n");
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

