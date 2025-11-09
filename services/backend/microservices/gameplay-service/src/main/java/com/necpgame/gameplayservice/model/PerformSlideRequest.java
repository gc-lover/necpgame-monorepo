package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.PerformJumpRequestTargetPosition;
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
 * PerformSlideRequest
 */

@JsonTypeName("performSlide_request")

public class PerformSlideRequest {

  private String characterId;

  private PerformJumpRequestTargetPosition direction;

  private @Nullable BigDecimal duration;

  public PerformSlideRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformSlideRequest(String characterId, PerformJumpRequestTargetPosition direction) {
    this.characterId = characterId;
    this.direction = direction;
  }

  public PerformSlideRequest characterId(String characterId) {
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

  public PerformSlideRequest direction(PerformJumpRequestTargetPosition direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Get direction
   * @return direction
   */
  @NotNull @Valid 
  @Schema(name = "direction", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("direction")
  public PerformJumpRequestTargetPosition getDirection() {
    return direction;
  }

  public void setDirection(PerformJumpRequestTargetPosition direction) {
    this.direction = direction;
  }

  public PerformSlideRequest duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Длительность скольжения (секунды)
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", description = "Длительность скольжения (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    PerformSlideRequest performSlideRequest = (PerformSlideRequest) o;
    return Objects.equals(this.characterId, performSlideRequest.characterId) &&
        Objects.equals(this.direction, performSlideRequest.direction) &&
        Objects.equals(this.duration, performSlideRequest.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, direction, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformSlideRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
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

