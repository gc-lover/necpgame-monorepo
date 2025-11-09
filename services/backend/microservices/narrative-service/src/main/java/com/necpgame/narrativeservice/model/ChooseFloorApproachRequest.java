package com.necpgame.narrativeservice.model;

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
 * ChooseFloorApproachRequest
 */

@JsonTypeName("chooseFloorApproach_request")

public class ChooseFloorApproachRequest {

  private String characterId;

  /**
   * Gets or Sets approach
   */
  public enum ApproachEnum {
    STEALTH("stealth"),
    
    COMBAT("combat");

    private final String value;

    ApproachEnum(String value) {
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
    public static ApproachEnum fromValue(String value) {
      for (ApproachEnum b : ApproachEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ApproachEnum approach;

  public ChooseFloorApproachRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChooseFloorApproachRequest(String characterId, ApproachEnum approach) {
    this.characterId = characterId;
    this.approach = approach;
  }

  public ChooseFloorApproachRequest characterId(String characterId) {
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

  public ChooseFloorApproachRequest approach(ApproachEnum approach) {
    this.approach = approach;
    return this;
  }

  /**
   * Get approach
   * @return approach
   */
  @NotNull 
  @Schema(name = "approach", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("approach")
  public ApproachEnum getApproach() {
    return approach;
  }

  public void setApproach(ApproachEnum approach) {
    this.approach = approach;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChooseFloorApproachRequest chooseFloorApproachRequest = (ChooseFloorApproachRequest) o;
    return Objects.equals(this.characterId, chooseFloorApproachRequest.characterId) &&
        Objects.equals(this.approach, chooseFloorApproachRequest.approach);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, approach);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChooseFloorApproachRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    approach: ").append(toIndentedString(approach)).append("\n");
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

