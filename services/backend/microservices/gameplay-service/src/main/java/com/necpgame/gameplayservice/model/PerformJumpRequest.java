package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.PerformJumpRequestTargetPosition;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformJumpRequest
 */

@JsonTypeName("performJump_request")

public class PerformJumpRequest {

  private String characterId;

  /**
   * Gets or Sets jumpType
   */
  public enum JumpTypeEnum {
    NORMAL("normal"),
    
    DOUBLE("double"),
    
    ROOF_TO_ROOF("roof_to_roof"),
    
    LEDGE_GRAB("ledge_grab");

    private final String value;

    JumpTypeEnum(String value) {
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
    public static JumpTypeEnum fromValue(String value) {
      for (JumpTypeEnum b : JumpTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private JumpTypeEnum jumpType;

  private PerformJumpRequestTargetPosition targetPosition;

  private @Nullable BigDecimal currentMomentum;

  public PerformJumpRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformJumpRequest(String characterId, JumpTypeEnum jumpType, PerformJumpRequestTargetPosition targetPosition) {
    this.characterId = characterId;
    this.jumpType = jumpType;
    this.targetPosition = targetPosition;
  }

  public PerformJumpRequest characterId(String characterId) {
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

  public PerformJumpRequest jumpType(JumpTypeEnum jumpType) {
    this.jumpType = jumpType;
    return this;
  }

  /**
   * Get jumpType
   * @return jumpType
   */
  @NotNull 
  @Schema(name = "jump_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("jump_type")
  public JumpTypeEnum getJumpType() {
    return jumpType;
  }

  public void setJumpType(JumpTypeEnum jumpType) {
    this.jumpType = jumpType;
  }

  public PerformJumpRequest targetPosition(PerformJumpRequestTargetPosition targetPosition) {
    this.targetPosition = targetPosition;
    return this;
  }

  /**
   * Get targetPosition
   * @return targetPosition
   */
  @NotNull @Valid 
  @Schema(name = "target_position", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_position")
  public PerformJumpRequestTargetPosition getTargetPosition() {
    return targetPosition;
  }

  public void setTargetPosition(PerformJumpRequestTargetPosition targetPosition) {
    this.targetPosition = targetPosition;
  }

  public PerformJumpRequest currentMomentum(@Nullable BigDecimal currentMomentum) {
    this.currentMomentum = currentMomentum;
    return this;
  }

  /**
   * Текущий импульс персонажа
   * @return currentMomentum
   */
  @Valid 
  @Schema(name = "current_momentum", description = "Текущий импульс персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_momentum")
  public @Nullable BigDecimal getCurrentMomentum() {
    return currentMomentum;
  }

  public void setCurrentMomentum(@Nullable BigDecimal currentMomentum) {
    this.currentMomentum = currentMomentum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformJumpRequest performJumpRequest = (PerformJumpRequest) o;
    return Objects.equals(this.characterId, performJumpRequest.characterId) &&
        Objects.equals(this.jumpType, performJumpRequest.jumpType) &&
        Objects.equals(this.targetPosition, performJumpRequest.targetPosition) &&
        Objects.equals(this.currentMomentum, performJumpRequest.currentMomentum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, jumpType, targetPosition, currentMomentum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformJumpRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    jumpType: ").append(toIndentedString(jumpType)).append("\n");
    sb.append("    targetPosition: ").append(toIndentedString(targetPosition)).append("\n");
    sb.append("    currentMomentum: ").append(toIndentedString(currentMomentum)).append("\n");
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

