package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.MaintenanceStatusSessionDrain;
import com.necpgame.backjava.model.MaintenanceStatusTimelineInner;
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
 * MaintenanceStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MaintenanceStatus {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    NONE("NONE"),
    
    UPCOMING("UPCOMING"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    PAUSED("PAUSED"),
    
    COMPLETED("COMPLETED"),
    
    EMERGENCY("EMERGENCY");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable Float progressPercent;

  @Valid
  private List<String> affectedServices = new ArrayList<>();

  private @Nullable Integer playerCount;

  private @Nullable MaintenanceStatusSessionDrain sessionDrain;

  @Valid
  private List<@Valid MaintenanceStatusTimelineInner> timeline = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  public MaintenanceStatus() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceStatus(StatusEnum status, OffsetDateTime updatedAt) {
    this.status = status;
    this.updatedAt = updatedAt;
  }

  public MaintenanceStatus status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public MaintenanceStatus progressPercent(@Nullable Float progressPercent) {
    this.progressPercent = progressPercent;
    return this;
  }

  /**
   * Get progressPercent
   * minimum: 0
   * maximum: 100
   * @return progressPercent
   */
  @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "progressPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progressPercent")
  public @Nullable Float getProgressPercent() {
    return progressPercent;
  }

  public void setProgressPercent(@Nullable Float progressPercent) {
    this.progressPercent = progressPercent;
  }

  public MaintenanceStatus affectedServices(List<String> affectedServices) {
    this.affectedServices = affectedServices;
    return this;
  }

  public MaintenanceStatus addAffectedServicesItem(String affectedServicesItem) {
    if (this.affectedServices == null) {
      this.affectedServices = new ArrayList<>();
    }
    this.affectedServices.add(affectedServicesItem);
    return this;
  }

  /**
   * Get affectedServices
   * @return affectedServices
   */
  
  @Schema(name = "affectedServices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedServices")
  public List<String> getAffectedServices() {
    return affectedServices;
  }

  public void setAffectedServices(List<String> affectedServices) {
    this.affectedServices = affectedServices;
  }

  public MaintenanceStatus playerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
    return this;
  }

  /**
   * Get playerCount
   * @return playerCount
   */
  
  @Schema(name = "playerCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerCount")
  public @Nullable Integer getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
  }

  public MaintenanceStatus sessionDrain(@Nullable MaintenanceStatusSessionDrain sessionDrain) {
    this.sessionDrain = sessionDrain;
    return this;
  }

  /**
   * Get sessionDrain
   * @return sessionDrain
   */
  @Valid 
  @Schema(name = "sessionDrain", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionDrain")
  public @Nullable MaintenanceStatusSessionDrain getSessionDrain() {
    return sessionDrain;
  }

  public void setSessionDrain(@Nullable MaintenanceStatusSessionDrain sessionDrain) {
    this.sessionDrain = sessionDrain;
  }

  public MaintenanceStatus timeline(List<@Valid MaintenanceStatusTimelineInner> timeline) {
    this.timeline = timeline;
    return this;
  }

  public MaintenanceStatus addTimelineItem(MaintenanceStatusTimelineInner timelineItem) {
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
  public List<@Valid MaintenanceStatusTimelineInner> getTimeline() {
    return timeline;
  }

  public void setTimeline(List<@Valid MaintenanceStatusTimelineInner> timeline) {
    this.timeline = timeline;
  }

  public MaintenanceStatus updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceStatus maintenanceStatus = (MaintenanceStatus) o;
    return Objects.equals(this.status, maintenanceStatus.status) &&
        Objects.equals(this.progressPercent, maintenanceStatus.progressPercent) &&
        Objects.equals(this.affectedServices, maintenanceStatus.affectedServices) &&
        Objects.equals(this.playerCount, maintenanceStatus.playerCount) &&
        Objects.equals(this.sessionDrain, maintenanceStatus.sessionDrain) &&
        Objects.equals(this.timeline, maintenanceStatus.timeline) &&
        Objects.equals(this.updatedAt, maintenanceStatus.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, progressPercent, affectedServices, playerCount, sessionDrain, timeline, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceStatus {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    progressPercent: ").append(toIndentedString(progressPercent)).append("\n");
    sb.append("    affectedServices: ").append(toIndentedString(affectedServices)).append("\n");
    sb.append("    playerCount: ").append(toIndentedString(playerCount)).append("\n");
    sb.append("    sessionDrain: ").append(toIndentedString(sessionDrain)).append("\n");
    sb.append("    timeline: ").append(toIndentedString(timeline)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

