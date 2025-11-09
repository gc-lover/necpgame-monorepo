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
 * PerformTakedownRequest
 */

@JsonTypeName("performTakedown_request")

public class PerformTakedownRequest {

  private String characterId;

  private String targetId;

  /**
   * Gets or Sets takedownType
   */
  public enum TakedownTypeEnum {
    LETHAL("lethal"),
    
    NON_LETHAL("non_lethal");

    private final String value;

    TakedownTypeEnum(String value) {
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
    public static TakedownTypeEnum fromValue(String value) {
      for (TakedownTypeEnum b : TakedownTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TakedownTypeEnum takedownType;

  public PerformTakedownRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformTakedownRequest(String characterId, String targetId, TakedownTypeEnum takedownType) {
    this.characterId = characterId;
    this.targetId = targetId;
    this.takedownType = takedownType;
  }

  public PerformTakedownRequest characterId(String characterId) {
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

  public PerformTakedownRequest targetId(String targetId) {
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

  public PerformTakedownRequest takedownType(TakedownTypeEnum takedownType) {
    this.takedownType = takedownType;
    return this;
  }

  /**
   * Get takedownType
   * @return takedownType
   */
  @NotNull 
  @Schema(name = "takedown_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("takedown_type")
  public TakedownTypeEnum getTakedownType() {
    return takedownType;
  }

  public void setTakedownType(TakedownTypeEnum takedownType) {
    this.takedownType = takedownType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformTakedownRequest performTakedownRequest = (PerformTakedownRequest) o;
    return Objects.equals(this.characterId, performTakedownRequest.characterId) &&
        Objects.equals(this.targetId, performTakedownRequest.targetId) &&
        Objects.equals(this.takedownType, performTakedownRequest.takedownType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, takedownType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformTakedownRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    takedownType: ").append(toIndentedString(takedownType)).append("\n");
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

