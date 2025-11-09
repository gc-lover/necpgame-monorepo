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
 * PerformParkourCheckRequest
 */

@JsonTypeName("performParkourCheck_request")

public class PerformParkourCheckRequest {

  private String characterId;

  /**
   * Gets or Sets maneuverType
   */
  public enum ManeuverTypeEnum {
    WALL_RUN("wall_run"),
    
    ROOF_JUMP("roof_jump"),
    
    SLIDE_UNDER("slide_under"),
    
    VAULT_OVER("vault_over");

    private final String value;

    ManeuverTypeEnum(String value) {
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
    public static ManeuverTypeEnum fromValue(String value) {
      for (ManeuverTypeEnum b : ManeuverTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ManeuverTypeEnum maneuverType;

  private Boolean underFire = false;

  public PerformParkourCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformParkourCheckRequest(String characterId, ManeuverTypeEnum maneuverType) {
    this.characterId = characterId;
    this.maneuverType = maneuverType;
  }

  public PerformParkourCheckRequest characterId(String characterId) {
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

  public PerformParkourCheckRequest maneuverType(ManeuverTypeEnum maneuverType) {
    this.maneuverType = maneuverType;
    return this;
  }

  /**
   * Get maneuverType
   * @return maneuverType
   */
  @NotNull 
  @Schema(name = "maneuver_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maneuver_type")
  public ManeuverTypeEnum getManeuverType() {
    return maneuverType;
  }

  public void setManeuverType(ManeuverTypeEnum maneuverType) {
    this.maneuverType = maneuverType;
  }

  public PerformParkourCheckRequest underFire(Boolean underFire) {
    this.underFire = underFire;
    return this;
  }

  /**
   * Под обстрелом (повышает DC)
   * @return underFire
   */
  
  @Schema(name = "under_fire", description = "Под обстрелом (повышает DC)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("under_fire")
  public Boolean getUnderFire() {
    return underFire;
  }

  public void setUnderFire(Boolean underFire) {
    this.underFire = underFire;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformParkourCheckRequest performParkourCheckRequest = (PerformParkourCheckRequest) o;
    return Objects.equals(this.characterId, performParkourCheckRequest.characterId) &&
        Objects.equals(this.maneuverType, performParkourCheckRequest.maneuverType) &&
        Objects.equals(this.underFire, performParkourCheckRequest.underFire);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, maneuverType, underFire);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformParkourCheckRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    maneuverType: ").append(toIndentedString(maneuverType)).append("\n");
    sb.append("    underFire: ").append(toIndentedString(underFire)).append("\n");
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

