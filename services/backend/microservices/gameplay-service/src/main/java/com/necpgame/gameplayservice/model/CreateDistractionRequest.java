package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CreateDistractionRequestLocation;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateDistractionRequest
 */

@JsonTypeName("createDistraction_request")

public class CreateDistractionRequest {

  private String characterId;

  /**
   * Gets or Sets distractionType
   */
  public enum DistractionTypeEnum {
    SOUND("sound"),
    
    EXPLOSION("explosion"),
    
    HOLOGRAM("hologram"),
    
    HACK_DEVICE("hack_device");

    private final String value;

    DistractionTypeEnum(String value) {
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
    public static DistractionTypeEnum fromValue(String value) {
      for (DistractionTypeEnum b : DistractionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DistractionTypeEnum distractionType;

  private CreateDistractionRequestLocation location;

  public CreateDistractionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateDistractionRequest(String characterId, DistractionTypeEnum distractionType, CreateDistractionRequestLocation location) {
    this.characterId = characterId;
    this.distractionType = distractionType;
    this.location = location;
  }

  public CreateDistractionRequest characterId(String characterId) {
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

  public CreateDistractionRequest distractionType(DistractionTypeEnum distractionType) {
    this.distractionType = distractionType;
    return this;
  }

  /**
   * Get distractionType
   * @return distractionType
   */
  @NotNull 
  @Schema(name = "distraction_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("distraction_type")
  public DistractionTypeEnum getDistractionType() {
    return distractionType;
  }

  public void setDistractionType(DistractionTypeEnum distractionType) {
    this.distractionType = distractionType;
  }

  public CreateDistractionRequest location(CreateDistractionRequestLocation location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  @NotNull @Valid 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location")
  public CreateDistractionRequestLocation getLocation() {
    return location;
  }

  public void setLocation(CreateDistractionRequestLocation location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateDistractionRequest createDistractionRequest = (CreateDistractionRequest) o;
    return Objects.equals(this.characterId, createDistractionRequest.characterId) &&
        Objects.equals(this.distractionType, createDistractionRequest.distractionType) &&
        Objects.equals(this.location, createDistractionRequest.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, distractionType, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateDistractionRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    distractionType: ").append(toIndentedString(distractionType)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

