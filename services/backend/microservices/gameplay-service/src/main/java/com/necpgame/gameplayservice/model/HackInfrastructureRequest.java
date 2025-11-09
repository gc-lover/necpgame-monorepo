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
 * HackInfrastructureRequest
 */

@JsonTypeName("hackInfrastructure_request")

public class HackInfrastructureRequest {

  private String characterId;

  private String infrastructureId;

  /**
   * Gets or Sets hackType
   */
  public enum HackTypeEnum {
    BLACKOUT("blackout"),
    
    TRAFFIC_CONTROL("traffic_control"),
    
    NETWORK_BREACH("network_breach"),
    
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

  public HackInfrastructureRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HackInfrastructureRequest(String characterId, String infrastructureId, HackTypeEnum hackType) {
    this.characterId = characterId;
    this.infrastructureId = infrastructureId;
    this.hackType = hackType;
  }

  public HackInfrastructureRequest characterId(String characterId) {
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

  public HackInfrastructureRequest infrastructureId(String infrastructureId) {
    this.infrastructureId = infrastructureId;
    return this;
  }

  /**
   * Get infrastructureId
   * @return infrastructureId
   */
  @NotNull 
  @Schema(name = "infrastructure_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("infrastructure_id")
  public String getInfrastructureId() {
    return infrastructureId;
  }

  public void setInfrastructureId(String infrastructureId) {
    this.infrastructureId = infrastructureId;
  }

  public HackInfrastructureRequest hackType(HackTypeEnum hackType) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackInfrastructureRequest hackInfrastructureRequest = (HackInfrastructureRequest) o;
    return Objects.equals(this.characterId, hackInfrastructureRequest.characterId) &&
        Objects.equals(this.infrastructureId, hackInfrastructureRequest.infrastructureId) &&
        Objects.equals(this.hackType, hackInfrastructureRequest.hackType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, infrastructureId, hackType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackInfrastructureRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    infrastructureId: ").append(toIndentedString(infrastructureId)).append("\n");
    sb.append("    hackType: ").append(toIndentedString(hackType)).append("\n");
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

