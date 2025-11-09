package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.DistrictPopulationState;
import com.necpgame.worldservice.model.EventImpact;
import com.necpgame.worldservice.model.PlayerImpactSummary;
import com.necpgame.worldservice.model.PopulationAlert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * DistrictChange
 */


public class DistrictChange {

  private UUID districtId;

  private @Nullable DistrictPopulationState oldState;

  private DistrictPopulationState newState;

  @Valid
  private List<@Valid PopulationAlert> alerts = new ArrayList<>();

  private @Nullable PlayerImpactSummary playerImpact;

  @Valid
  private List<@Valid EventImpact> eventImpacts = new ArrayList<>();

  public DistrictChange() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DistrictChange(UUID districtId, DistrictPopulationState newState) {
    this.districtId = districtId;
    this.newState = newState;
  }

  public DistrictChange districtId(UUID districtId) {
    this.districtId = districtId;
    return this;
  }

  /**
   * Get districtId
   * @return districtId
   */
  @NotNull @Valid 
  @Schema(name = "districtId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("districtId")
  public UUID getDistrictId() {
    return districtId;
  }

  public void setDistrictId(UUID districtId) {
    this.districtId = districtId;
  }

  public DistrictChange oldState(@Nullable DistrictPopulationState oldState) {
    this.oldState = oldState;
    return this;
  }

  /**
   * Get oldState
   * @return oldState
   */
  @Valid 
  @Schema(name = "oldState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("oldState")
  public @Nullable DistrictPopulationState getOldState() {
    return oldState;
  }

  public void setOldState(@Nullable DistrictPopulationState oldState) {
    this.oldState = oldState;
  }

  public DistrictChange newState(DistrictPopulationState newState) {
    this.newState = newState;
    return this;
  }

  /**
   * Get newState
   * @return newState
   */
  @NotNull @Valid 
  @Schema(name = "newState", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newState")
  public DistrictPopulationState getNewState() {
    return newState;
  }

  public void setNewState(DistrictPopulationState newState) {
    this.newState = newState;
  }

  public DistrictChange alerts(List<@Valid PopulationAlert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public DistrictChange addAlertsItem(PopulationAlert alertsItem) {
    if (this.alerts == null) {
      this.alerts = new ArrayList<>();
    }
    this.alerts.add(alertsItem);
    return this;
  }

  /**
   * Get alerts
   * @return alerts
   */
  @Valid 
  @Schema(name = "alerts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alerts")
  public List<@Valid PopulationAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<@Valid PopulationAlert> alerts) {
    this.alerts = alerts;
  }

  public DistrictChange playerImpact(@Nullable PlayerImpactSummary playerImpact) {
    this.playerImpact = playerImpact;
    return this;
  }

  /**
   * Get playerImpact
   * @return playerImpact
   */
  @Valid 
  @Schema(name = "playerImpact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerImpact")
  public @Nullable PlayerImpactSummary getPlayerImpact() {
    return playerImpact;
  }

  public void setPlayerImpact(@Nullable PlayerImpactSummary playerImpact) {
    this.playerImpact = playerImpact;
  }

  public DistrictChange eventImpacts(List<@Valid EventImpact> eventImpacts) {
    this.eventImpacts = eventImpacts;
    return this;
  }

  public DistrictChange addEventImpactsItem(EventImpact eventImpactsItem) {
    if (this.eventImpacts == null) {
      this.eventImpacts = new ArrayList<>();
    }
    this.eventImpacts.add(eventImpactsItem);
    return this;
  }

  /**
   * Get eventImpacts
   * @return eventImpacts
   */
  @Valid 
  @Schema(name = "eventImpacts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventImpacts")
  public List<@Valid EventImpact> getEventImpacts() {
    return eventImpacts;
  }

  public void setEventImpacts(List<@Valid EventImpact> eventImpacts) {
    this.eventImpacts = eventImpacts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistrictChange districtChange = (DistrictChange) o;
    return Objects.equals(this.districtId, districtChange.districtId) &&
        Objects.equals(this.oldState, districtChange.oldState) &&
        Objects.equals(this.newState, districtChange.newState) &&
        Objects.equals(this.alerts, districtChange.alerts) &&
        Objects.equals(this.playerImpact, districtChange.playerImpact) &&
        Objects.equals(this.eventImpacts, districtChange.eventImpacts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, oldState, newState, alerts, playerImpact, eventImpacts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistrictChange {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    oldState: ").append(toIndentedString(oldState)).append("\n");
    sb.append("    newState: ").append(toIndentedString(newState)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    playerImpact: ").append(toIndentedString(playerImpact)).append("\n");
    sb.append("    eventImpacts: ").append(toIndentedString(eventImpacts)).append("\n");
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

