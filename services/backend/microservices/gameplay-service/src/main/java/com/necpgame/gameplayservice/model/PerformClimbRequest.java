package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PerformClimbRequest
 */

@JsonTypeName("performClimb_request")

public class PerformClimbRequest {

  private String characterId;

  /**
   * Gets or Sets surfaceType
   */
  public enum SurfaceTypeEnum {
    WALL("wall"),
    
    LEDGE("ledge"),
    
    PIPE("pipe"),
    
    LADDER("ladder");

    private final String value;

    SurfaceTypeEnum(String value) {
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
    public static SurfaceTypeEnum fromValue(String value) {
      for (SurfaceTypeEnum b : SurfaceTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SurfaceTypeEnum surfaceType;

  private BigDecimal height;

  private @Nullable Boolean hasImplants;

  public PerformClimbRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformClimbRequest(String characterId, SurfaceTypeEnum surfaceType, BigDecimal height) {
    this.characterId = characterId;
    this.surfaceType = surfaceType;
    this.height = height;
  }

  public PerformClimbRequest characterId(String characterId) {
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

  public PerformClimbRequest surfaceType(SurfaceTypeEnum surfaceType) {
    this.surfaceType = surfaceType;
    return this;
  }

  /**
   * Get surfaceType
   * @return surfaceType
   */
  @NotNull 
  @Schema(name = "surface_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("surface_type")
  public SurfaceTypeEnum getSurfaceType() {
    return surfaceType;
  }

  public void setSurfaceType(SurfaceTypeEnum surfaceType) {
    this.surfaceType = surfaceType;
  }

  public PerformClimbRequest height(BigDecimal height) {
    this.height = height;
    return this;
  }

  /**
   * Высота лазания (метры)
   * @return height
   */
  @NotNull @Valid 
  @Schema(name = "height", description = "Высота лазания (метры)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("height")
  public BigDecimal getHeight() {
    return height;
  }

  public void setHeight(BigDecimal height) {
    this.height = height;
  }

  public PerformClimbRequest hasImplants(@Nullable Boolean hasImplants) {
    this.hasImplants = hasImplants;
    return this;
  }

  /**
   * Есть ли импланты для лазания
   * @return hasImplants
   */
  
  @Schema(name = "has_implants", description = "Есть ли импланты для лазания", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_implants")
  public @Nullable Boolean getHasImplants() {
    return hasImplants;
  }

  public void setHasImplants(@Nullable Boolean hasImplants) {
    this.hasImplants = hasImplants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformClimbRequest performClimbRequest = (PerformClimbRequest) o;
    return Objects.equals(this.characterId, performClimbRequest.characterId) &&
        Objects.equals(this.surfaceType, performClimbRequest.surfaceType) &&
        Objects.equals(this.height, performClimbRequest.height) &&
        Objects.equals(this.hasImplants, performClimbRequest.hasImplants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, surfaceType, height, hasImplants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformClimbRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    surfaceType: ").append(toIndentedString(surfaceType)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    hasImplants: ").append(toIndentedString(hasImplants)).append("\n");
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

