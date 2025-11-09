package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CombatLogResponse;
import com.necpgame.backjava.model.CombatSession;
import com.necpgame.backjava.model.StatusEffect;
import com.necpgame.backjava.model.TurnState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatSessionStateResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CombatSessionStateResponse {

  private @Nullable CombatSession session;

  @Valid
  private List<@Valid TurnState> timeline = new ArrayList<>();

  @Valid
  private List<@Valid StatusEffect> activeEffects = new ArrayList<>();

  private @Nullable CombatLogResponse log;

  public CombatSessionStateResponse session(@Nullable CombatSession session) {
    this.session = session;
    return this;
  }

  /**
   * Get session
   * @return session
   */
  @Valid 
  @Schema(name = "session", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session")
  public @Nullable CombatSession getSession() {
    return session;
  }

  public void setSession(@Nullable CombatSession session) {
    this.session = session;
  }

  public CombatSessionStateResponse timeline(List<@Valid TurnState> timeline) {
    this.timeline = timeline;
    return this;
  }

  public CombatSessionStateResponse addTimelineItem(TurnState timelineItem) {
    if (this.timeline == null) {
      this.timeline = new ArrayList<>();
    }
    this.timeline.add(timelineItem);
    return this;
  }

  /**
   * Get timeline
   * @return timeline
   */
  @Valid 
  @Schema(name = "timeline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeline")
  public List<@Valid TurnState> getTimeline() {
    return timeline;
  }

  public void setTimeline(List<@Valid TurnState> timeline) {
    this.timeline = timeline;
  }

  public CombatSessionStateResponse activeEffects(List<@Valid StatusEffect> activeEffects) {
    this.activeEffects = activeEffects;
    return this;
  }

  public CombatSessionStateResponse addActiveEffectsItem(StatusEffect activeEffectsItem) {
    if (this.activeEffects == null) {
      this.activeEffects = new ArrayList<>();
    }
    this.activeEffects.add(activeEffectsItem);
    return this;
  }

  /**
   * Get activeEffects
   * @return activeEffects
   */
  @Valid 
  @Schema(name = "activeEffects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeEffects")
  public List<@Valid StatusEffect> getActiveEffects() {
    return activeEffects;
  }

  public void setActiveEffects(List<@Valid StatusEffect> activeEffects) {
    this.activeEffects = activeEffects;
  }

  public CombatSessionStateResponse log(@Nullable CombatLogResponse log) {
    this.log = log;
    return this;
  }

  /**
   * Get log
   * @return log
   */
  @Valid 
  @Schema(name = "log", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("log")
  public @Nullable CombatLogResponse getLog() {
    return log;
  }

  public void setLog(@Nullable CombatLogResponse log) {
    this.log = log;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatSessionStateResponse combatSessionStateResponse = (CombatSessionStateResponse) o;
    return Objects.equals(this.session, combatSessionStateResponse.session) &&
        Objects.equals(this.timeline, combatSessionStateResponse.timeline) &&
        Objects.equals(this.activeEffects, combatSessionStateResponse.activeEffects) &&
        Objects.equals(this.log, combatSessionStateResponse.log);
  }

  @Override
  public int hashCode() {
    return Objects.hash(session, timeline, activeEffects, log);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatSessionStateResponse {\n");
    sb.append("    session: ").append(toIndentedString(session)).append("\n");
    sb.append("    timeline: ").append(toIndentedString(timeline)).append("\n");
    sb.append("    activeEffects: ").append(toIndentedString(activeEffects)).append("\n");
    sb.append("    log: ").append(toIndentedString(log)).append("\n");
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

