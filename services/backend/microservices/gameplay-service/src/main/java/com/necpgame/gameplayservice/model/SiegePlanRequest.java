package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SiegePlanRequestAttackerForces;
import com.necpgame.gameplayservice.model.SiegePlanRequestDefenderForces;
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
 * SiegePlanRequest
 */


public class SiegePlanRequest {

  private String territoryId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  private Integer durationMinutes;

  @Valid
  private List<String> objectives = new ArrayList<>();

  private @Nullable SiegePlanRequestAttackerForces attackerForces;

  private @Nullable SiegePlanRequestDefenderForces defenderForces;

  public SiegePlanRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SiegePlanRequest(String territoryId, OffsetDateTime startTime, Integer durationMinutes) {
    this.territoryId = territoryId;
    this.startTime = startTime;
    this.durationMinutes = durationMinutes;
  }

  public SiegePlanRequest territoryId(String territoryId) {
    this.territoryId = territoryId;
    return this;
  }

  /**
   * Get territoryId
   * @return territoryId
   */
  @NotNull 
  @Schema(name = "territoryId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("territoryId")
  public String getTerritoryId() {
    return territoryId;
  }

  public void setTerritoryId(String territoryId) {
    this.territoryId = territoryId;
  }

  public SiegePlanRequest startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
   */
  @NotNull @Valid 
  @Schema(name = "startTime", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startTime")
  public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public SiegePlanRequest durationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * minimum: 15
   * maximum: 180
   * @return durationMinutes
   */
  @NotNull @Min(value = 15) @Max(value = 180) 
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("durationMinutes")
  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public SiegePlanRequest objectives(List<String> objectives) {
    this.objectives = objectives;
    return this;
  }

  public SiegePlanRequest addObjectivesItem(String objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<String> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<String> objectives) {
    this.objectives = objectives;
  }

  public SiegePlanRequest attackerForces(@Nullable SiegePlanRequestAttackerForces attackerForces) {
    this.attackerForces = attackerForces;
    return this;
  }

  /**
   * Get attackerForces
   * @return attackerForces
   */
  @Valid 
  @Schema(name = "attackerForces", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attackerForces")
  public @Nullable SiegePlanRequestAttackerForces getAttackerForces() {
    return attackerForces;
  }

  public void setAttackerForces(@Nullable SiegePlanRequestAttackerForces attackerForces) {
    this.attackerForces = attackerForces;
  }

  public SiegePlanRequest defenderForces(@Nullable SiegePlanRequestDefenderForces defenderForces) {
    this.defenderForces = defenderForces;
    return this;
  }

  /**
   * Get defenderForces
   * @return defenderForces
   */
  @Valid 
  @Schema(name = "defenderForces", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defenderForces")
  public @Nullable SiegePlanRequestDefenderForces getDefenderForces() {
    return defenderForces;
  }

  public void setDefenderForces(@Nullable SiegePlanRequestDefenderForces defenderForces) {
    this.defenderForces = defenderForces;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SiegePlanRequest siegePlanRequest = (SiegePlanRequest) o;
    return Objects.equals(this.territoryId, siegePlanRequest.territoryId) &&
        Objects.equals(this.startTime, siegePlanRequest.startTime) &&
        Objects.equals(this.durationMinutes, siegePlanRequest.durationMinutes) &&
        Objects.equals(this.objectives, siegePlanRequest.objectives) &&
        Objects.equals(this.attackerForces, siegePlanRequest.attackerForces) &&
        Objects.equals(this.defenderForces, siegePlanRequest.defenderForces);
  }

  @Override
  public int hashCode() {
    return Objects.hash(territoryId, startTime, durationMinutes, objectives, attackerForces, defenderForces);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SiegePlanRequest {\n");
    sb.append("    territoryId: ").append(toIndentedString(territoryId)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    attackerForces: ").append(toIndentedString(attackerForces)).append("\n");
    sb.append("    defenderForces: ").append(toIndentedString(defenderForces)).append("\n");
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

