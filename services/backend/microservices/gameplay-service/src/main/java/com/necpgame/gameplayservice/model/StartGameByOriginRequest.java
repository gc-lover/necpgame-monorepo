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
 * StartGameByOriginRequest
 */

@JsonTypeName("startGameByOrigin_request")

public class StartGameByOriginRequest {

  private String characterId;

  /**
   * Gets or Sets originId
   */
  public enum OriginIdEnum {
    CORPO("corpo"),
    
    STREET_KID("street_kid"),
    
    NOMAD("nomad");

    private final String value;

    OriginIdEnum(String value) {
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
    public static OriginIdEnum fromValue(String value) {
      for (OriginIdEnum b : OriginIdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OriginIdEnum originId;

  public StartGameByOriginRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartGameByOriginRequest(String characterId, OriginIdEnum originId) {
    this.characterId = characterId;
    this.originId = originId;
  }

  public StartGameByOriginRequest characterId(String characterId) {
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

  public StartGameByOriginRequest originId(OriginIdEnum originId) {
    this.originId = originId;
    return this;
  }

  /**
   * Get originId
   * @return originId
   */
  @NotNull 
  @Schema(name = "origin_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin_id")
  public OriginIdEnum getOriginId() {
    return originId;
  }

  public void setOriginId(OriginIdEnum originId) {
    this.originId = originId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartGameByOriginRequest startGameByOriginRequest = (StartGameByOriginRequest) o;
    return Objects.equals(this.characterId, startGameByOriginRequest.characterId) &&
        Objects.equals(this.originId, startGameByOriginRequest.originId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, originId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartGameByOriginRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    originId: ").append(toIndentedString(originId)).append("\n");
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

