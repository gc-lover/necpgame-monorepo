package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * LeaguePhase
 */


public class LeaguePhase {

  /**
   * Gets or Sets phaseName
   */
  public enum PhaseNameEnum {
    START("start"),
    
    RISE("rise"),
    
    CRISIS("crisis"),
    
    ENDGAME("endgame"),
    
    FINALE("finale");

    private final String value;

    PhaseNameEnum(String value) {
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
    public static PhaseNameEnum fromValue(String value) {
      for (PhaseNameEnum b : PhaseNameEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PhaseNameEnum phaseName;

  private @Nullable String eraRange;

  private @Nullable String focus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endsAt;

  @Valid
  private List<String> activeEvents = new ArrayList<>();

  @Valid
  private List<String> specialMechanics = new ArrayList<>();

  public LeaguePhase phaseName(@Nullable PhaseNameEnum phaseName) {
    this.phaseName = phaseName;
    return this;
  }

  /**
   * Get phaseName
   * @return phaseName
   */
  
  @Schema(name = "phase_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase_name")
  public @Nullable PhaseNameEnum getPhaseName() {
    return phaseName;
  }

  public void setPhaseName(@Nullable PhaseNameEnum phaseName) {
    this.phaseName = phaseName;
  }

  public LeaguePhase eraRange(@Nullable String eraRange) {
    this.eraRange = eraRange;
    return this;
  }

  /**
   * Диапазон игровых лет
   * @return eraRange
   */
  
  @Schema(name = "era_range", example = "2060-2090", description = "Диапазон игровых лет", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era_range")
  public @Nullable String getEraRange() {
    return eraRange;
  }

  public void setEraRange(@Nullable String eraRange) {
    this.eraRange = eraRange;
  }

  public LeaguePhase focus(@Nullable String focus) {
    this.focus = focus;
    return this;
  }

  /**
   * Фокус фазы
   * @return focus
   */
  
  @Schema(name = "focus", description = "Фокус фазы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("focus")
  public @Nullable String getFocus() {
    return focus;
  }

  public void setFocus(@Nullable String focus) {
    this.focus = focus;
  }

  public LeaguePhase startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public LeaguePhase endsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
    return this;
  }

  /**
   * Get endsAt
   * @return endsAt
   */
  @Valid 
  @Schema(name = "ends_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ends_at")
  public @Nullable OffsetDateTime getEndsAt() {
    return endsAt;
  }

  public void setEndsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
  }

  public LeaguePhase activeEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public LeaguePhase addActiveEventsItem(String activeEventsItem) {
    if (this.activeEvents == null) {
      this.activeEvents = new ArrayList<>();
    }
    this.activeEvents.add(activeEventsItem);
    return this;
  }

  /**
   * Активные глобальные события
   * @return activeEvents
   */
  
  @Schema(name = "active_events", description = "Активные глобальные события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events")
  public List<String> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
  }

  public LeaguePhase specialMechanics(List<String> specialMechanics) {
    this.specialMechanics = specialMechanics;
    return this;
  }

  public LeaguePhase addSpecialMechanicsItem(String specialMechanicsItem) {
    if (this.specialMechanics == null) {
      this.specialMechanics = new ArrayList<>();
    }
    this.specialMechanics.add(specialMechanicsItem);
    return this;
  }

  /**
   * Специальные механики фазы
   * @return specialMechanics
   */
  
  @Schema(name = "special_mechanics", description = "Специальные механики фазы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("special_mechanics")
  public List<String> getSpecialMechanics() {
    return specialMechanics;
  }

  public void setSpecialMechanics(List<String> specialMechanics) {
    this.specialMechanics = specialMechanics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaguePhase leaguePhase = (LeaguePhase) o;
    return Objects.equals(this.phaseName, leaguePhase.phaseName) &&
        Objects.equals(this.eraRange, leaguePhase.eraRange) &&
        Objects.equals(this.focus, leaguePhase.focus) &&
        Objects.equals(this.startedAt, leaguePhase.startedAt) &&
        Objects.equals(this.endsAt, leaguePhase.endsAt) &&
        Objects.equals(this.activeEvents, leaguePhase.activeEvents) &&
        Objects.equals(this.specialMechanics, leaguePhase.specialMechanics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phaseName, eraRange, focus, startedAt, endsAt, activeEvents, specialMechanics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaguePhase {\n");
    sb.append("    phaseName: ").append(toIndentedString(phaseName)).append("\n");
    sb.append("    eraRange: ").append(toIndentedString(eraRange)).append("\n");
    sb.append("    focus: ").append(toIndentedString(focus)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    endsAt: ").append(toIndentedString(endsAt)).append("\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
    sb.append("    specialMechanics: ").append(toIndentedString(specialMechanics)).append("\n");
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

