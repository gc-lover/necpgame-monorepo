package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RestActionRequest
 */

@JsonTypeName("restAction_request")

public class RestActionRequest {

  private UUID characterId;

  private @Nullable Integer duration;

  public RestActionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RestActionRequest(UUID characterId) {
    this.characterId = characterId;
  }

  public RestActionRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public RestActionRequest duration(@Nullable Integer duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Минуты отдыха
   * minimum: 1
   * @return duration
   */
  @Min(value = 1) 
  @Schema(name = "duration", description = "Минуты отдыха", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable Integer getDuration() {
    return duration;
  }

  public void setDuration(@Nullable Integer duration) {
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
    RestActionRequest restActionRequest = (RestActionRequest) o;
    return Objects.equals(this.characterId, restActionRequest.characterId) &&
        Objects.equals(this.duration, restActionRequest.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RestActionRequest {\n");
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

