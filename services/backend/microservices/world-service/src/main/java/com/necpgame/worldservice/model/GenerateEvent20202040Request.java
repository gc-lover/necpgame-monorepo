package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GenerateEvent20202040Request
 */

@JsonTypeName("generateEvent2020_2040_request")

public class GenerateEvent20202040Request {

  private String characterId;

  private String locationId;

  private @Nullable Integer rollOverride;

  public GenerateEvent20202040Request() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GenerateEvent20202040Request(String characterId, String locationId) {
    this.characterId = characterId;
    this.locationId = locationId;
  }

  public GenerateEvent20202040Request characterId(String characterId) {
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

  public GenerateEvent20202040Request locationId(String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  @NotNull 
  @Schema(name = "location_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location_id")
  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public GenerateEvent20202040Request rollOverride(@Nullable Integer rollOverride) {
    this.rollOverride = rollOverride;
    return this;
  }

  /**
   * Переопределить бросок d100 (для тестирования)
   * minimum: 1
   * maximum: 100
   * @return rollOverride
   */
  @Min(value = 1) @Max(value = 100) 
  @Schema(name = "roll_override", description = "Переопределить бросок d100 (для тестирования)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll_override")
  public @Nullable Integer getRollOverride() {
    return rollOverride;
  }

  public void setRollOverride(@Nullable Integer rollOverride) {
    this.rollOverride = rollOverride;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateEvent20202040Request generateEvent20202040Request = (GenerateEvent20202040Request) o;
    return Objects.equals(this.characterId, generateEvent20202040Request.characterId) &&
        Objects.equals(this.locationId, generateEvent20202040Request.locationId) &&
        Objects.equals(this.rollOverride, generateEvent20202040Request.rollOverride);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, locationId, rollOverride);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateEvent20202040Request {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    rollOverride: ").append(toIndentedString(rollOverride)).append("\n");
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

