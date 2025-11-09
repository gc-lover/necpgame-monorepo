package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LagCompensationResponse
 */


public class LagCompensationResponse {

  private @Nullable String eventId;

  /**
   * Gets or Sets outcome
   */
  public enum OutcomeEnum {
    REFRESHED("REFRESHED"),
    
    DENIED("DENIED"),
    
    RETAINED("RETAINED");

    private final String value;

    OutcomeEnum(String value) {
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
    public static OutcomeEnum fromValue(String value) {
      for (OutcomeEnum b : OutcomeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OutcomeEnum outcome;

  private @Nullable Boolean adjusted;

  @Valid
  private Map<String, Object> corrections = new HashMap<>();

  public LagCompensationResponse eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventId")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public LagCompensationResponse outcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable OutcomeEnum getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
  }

  public LagCompensationResponse adjusted(@Nullable Boolean adjusted) {
    this.adjusted = adjusted;
    return this;
  }

  /**
   * Get adjusted
   * @return adjusted
   */
  
  @Schema(name = "adjusted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("adjusted")
  public @Nullable Boolean getAdjusted() {
    return adjusted;
  }

  public void setAdjusted(@Nullable Boolean adjusted) {
    this.adjusted = adjusted;
  }

  public LagCompensationResponse corrections(Map<String, Object> corrections) {
    this.corrections = corrections;
    return this;
  }

  public LagCompensationResponse putCorrectionsItem(String key, Object correctionsItem) {
    if (this.corrections == null) {
      this.corrections = new HashMap<>();
    }
    this.corrections.put(key, correctionsItem);
    return this;
  }

  /**
   * Get corrections
   * @return corrections
   */
  
  @Schema(name = "corrections", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("corrections")
  public Map<String, Object> getCorrections() {
    return corrections;
  }

  public void setCorrections(Map<String, Object> corrections) {
    this.corrections = corrections;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LagCompensationResponse lagCompensationResponse = (LagCompensationResponse) o;
    return Objects.equals(this.eventId, lagCompensationResponse.eventId) &&
        Objects.equals(this.outcome, lagCompensationResponse.outcome) &&
        Objects.equals(this.adjusted, lagCompensationResponse.adjusted) &&
        Objects.equals(this.corrections, lagCompensationResponse.corrections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, outcome, adjusted, corrections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LagCompensationResponse {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    adjusted: ").append(toIndentedString(adjusted)).append("\n");
    sb.append("    corrections: ").append(toIndentedString(corrections)).append("\n");
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

