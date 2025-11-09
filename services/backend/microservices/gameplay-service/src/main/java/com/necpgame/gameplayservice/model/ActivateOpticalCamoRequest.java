package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ActivateOpticalCamoRequest
 */

@JsonTypeName("activateOpticalCamo_request")

public class ActivateOpticalCamoRequest {

  private String characterId;

  private @Nullable BigDecimal duration;

  public ActivateOpticalCamoRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActivateOpticalCamoRequest(String characterId) {
    this.characterId = characterId;
  }

  public ActivateOpticalCamoRequest characterId(String characterId) {
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

  public ActivateOpticalCamoRequest duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Желаемая длительность (секунды)
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", description = "Желаемая длительность (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivateOpticalCamoRequest activateOpticalCamoRequest = (ActivateOpticalCamoRequest) o;
    return Objects.equals(this.characterId, activateOpticalCamoRequest.characterId) &&
        Objects.equals(this.duration, activateOpticalCamoRequest.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivateOpticalCamoRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

