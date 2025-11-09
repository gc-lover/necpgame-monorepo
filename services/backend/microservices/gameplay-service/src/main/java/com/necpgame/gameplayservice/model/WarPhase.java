package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WarPhase
 */


public class WarPhase {

  /**
   * Gets or Sets phase
   */
  public enum PhaseEnum {
    DECLARATION("DECLARATION"),
    
    PREPARATION("PREPARATION"),
    
    SIEGE("SIEGE"),
    
    RESOLUTION("RESOLUTION"),
    
    COOLDOWN("COOLDOWN");

    private final String value;

    PhaseEnum(String value) {
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
    public static PhaseEnum fromValue(String value) {
      for (PhaseEnum b : PhaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PhaseEnum phase;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startsAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endsAt;

  private @Nullable String notes;

  public WarPhase phase(@Nullable PhaseEnum phase) {
    this.phase = phase;
    return this;
  }

  /**
   * Get phase
   * @return phase
   */
  
  @Schema(name = "phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase")
  public @Nullable PhaseEnum getPhase() {
    return phase;
  }

  public void setPhase(@Nullable PhaseEnum phase) {
    this.phase = phase;
  }

  public WarPhase startsAt(@Nullable OffsetDateTime startsAt) {
    this.startsAt = startsAt;
    return this;
  }

  /**
   * Get startsAt
   * @return startsAt
   */
  @Valid 
  @Schema(name = "startsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startsAt")
  public @Nullable OffsetDateTime getStartsAt() {
    return startsAt;
  }

  public void setStartsAt(@Nullable OffsetDateTime startsAt) {
    this.startsAt = startsAt;
  }

  public WarPhase endsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
    return this;
  }

  /**
   * Get endsAt
   * @return endsAt
   */
  @Valid 
  @Schema(name = "endsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endsAt")
  public @Nullable OffsetDateTime getEndsAt() {
    return endsAt;
  }

  public void setEndsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
  }

  public WarPhase notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarPhase warPhase = (WarPhase) o;
    return Objects.equals(this.phase, warPhase.phase) &&
        Objects.equals(this.startsAt, warPhase.startsAt) &&
        Objects.equals(this.endsAt, warPhase.endsAt) &&
        Objects.equals(this.notes, warPhase.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phase, startsAt, endsAt, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarPhase {\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    startsAt: ").append(toIndentedString(startsAt)).append("\n");
    sb.append("    endsAt: ").append(toIndentedString(endsAt)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

