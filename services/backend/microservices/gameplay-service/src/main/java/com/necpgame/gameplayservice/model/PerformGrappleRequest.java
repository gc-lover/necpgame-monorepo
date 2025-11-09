package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.PerformJumpRequestTargetPosition;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformGrappleRequest
 */

@JsonTypeName("performGrapple_request")

public class PerformGrappleRequest {

  private String characterId;

  /**
   * Gets or Sets grappleType
   */
  public enum GrappleTypeEnum {
    HOOK("hook"),
    
    GRAVIBOT("gravibot"),
    
    MANTIS_BLADES("mantis_blades");

    private final String value;

    GrappleTypeEnum(String value) {
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
    public static GrappleTypeEnum fromValue(String value) {
      for (GrappleTypeEnum b : GrappleTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private GrappleTypeEnum grappleType;

  private PerformJumpRequestTargetPosition targetPoint;

  public PerformGrappleRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformGrappleRequest(String characterId, GrappleTypeEnum grappleType, PerformJumpRequestTargetPosition targetPoint) {
    this.characterId = characterId;
    this.grappleType = grappleType;
    this.targetPoint = targetPoint;
  }

  public PerformGrappleRequest characterId(String characterId) {
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

  public PerformGrappleRequest grappleType(GrappleTypeEnum grappleType) {
    this.grappleType = grappleType;
    return this;
  }

  /**
   * Get grappleType
   * @return grappleType
   */
  @NotNull 
  @Schema(name = "grapple_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("grapple_type")
  public GrappleTypeEnum getGrappleType() {
    return grappleType;
  }

  public void setGrappleType(GrappleTypeEnum grappleType) {
    this.grappleType = grappleType;
  }

  public PerformGrappleRequest targetPoint(PerformJumpRequestTargetPosition targetPoint) {
    this.targetPoint = targetPoint;
    return this;
  }

  /**
   * Get targetPoint
   * @return targetPoint
   */
  @NotNull @Valid 
  @Schema(name = "target_point", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_point")
  public PerformJumpRequestTargetPosition getTargetPoint() {
    return targetPoint;
  }

  public void setTargetPoint(PerformJumpRequestTargetPosition targetPoint) {
    this.targetPoint = targetPoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformGrappleRequest performGrappleRequest = (PerformGrappleRequest) o;
    return Objects.equals(this.characterId, performGrappleRequest.characterId) &&
        Objects.equals(this.grappleType, performGrappleRequest.grappleType) &&
        Objects.equals(this.targetPoint, performGrappleRequest.targetPoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, grappleType, targetPoint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformGrappleRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    grappleType: ").append(toIndentedString(grappleType)).append("\n");
    sb.append("    targetPoint: ").append(toIndentedString(targetPoint)).append("\n");
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

