package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.RomanceEventInfo;
import com.necpgame.socialservice.model.ScoringResult;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RomanceEventGenerationResponseGeneratedEventsInner
 */

@JsonTypeName("RomanceEventGenerationResponse_generated_events_inner")

public class RomanceEventGenerationResponseGeneratedEventsInner {

  private @Nullable RomanceEventInfo event;

  private @Nullable ScoringResult score;

  public RomanceEventGenerationResponseGeneratedEventsInner event(@Nullable RomanceEventInfo event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable RomanceEventInfo getEvent() {
    return event;
  }

  public void setEvent(@Nullable RomanceEventInfo event) {
    this.event = event;
  }

  public RomanceEventGenerationResponseGeneratedEventsInner score(@Nullable ScoringResult score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @Valid 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable ScoringResult getScore() {
    return score;
  }

  public void setScore(@Nullable ScoringResult score) {
    this.score = score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventGenerationResponseGeneratedEventsInner romanceEventGenerationResponseGeneratedEventsInner = (RomanceEventGenerationResponseGeneratedEventsInner) o;
    return Objects.equals(this.event, romanceEventGenerationResponseGeneratedEventsInner.event) &&
        Objects.equals(this.score, romanceEventGenerationResponseGeneratedEventsInner.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventGenerationResponseGeneratedEventsInner {\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
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

