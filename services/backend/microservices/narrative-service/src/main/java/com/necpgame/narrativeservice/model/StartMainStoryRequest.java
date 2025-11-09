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
 * StartMainStoryRequest
 */

@JsonTypeName("startMainStory_request")

public class StartMainStoryRequest {

  private String characterId;

  /**
   * Gets or Sets lifePath
   */
  public enum LifePathEnum {
    CORPO("corpo"),
    
    STREET_KID("street_kid"),
    
    NOMAD("nomad");

    private final String value;

    LifePathEnum(String value) {
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
    public static LifePathEnum fromValue(String value) {
      for (LifePathEnum b : LifePathEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LifePathEnum lifePath;

  public StartMainStoryRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartMainStoryRequest(String characterId, LifePathEnum lifePath) {
    this.characterId = characterId;
    this.lifePath = lifePath;
  }

  public StartMainStoryRequest characterId(String characterId) {
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

  public StartMainStoryRequest lifePath(LifePathEnum lifePath) {
    this.lifePath = lifePath;
    return this;
  }

  /**
   * Get lifePath
   * @return lifePath
   */
  @NotNull 
  @Schema(name = "life_path", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("life_path")
  public LifePathEnum getLifePath() {
    return lifePath;
  }

  public void setLifePath(LifePathEnum lifePath) {
    this.lifePath = lifePath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartMainStoryRequest startMainStoryRequest = (StartMainStoryRequest) o;
    return Objects.equals(this.characterId, startMainStoryRequest.characterId) &&
        Objects.equals(this.lifePath, startMainStoryRequest.lifePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, lifePath);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartMainStoryRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    lifePath: ").append(toIndentedString(lifePath)).append("\n");
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

