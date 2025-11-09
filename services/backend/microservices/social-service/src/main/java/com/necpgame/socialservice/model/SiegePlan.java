package com.necpgame.socialservice.model;

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
 * SiegePlan
 */


public class SiegePlan {

  private String siegeId;

  private String territoryId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  private Integer durationMinutes;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SCHEDULED("SCHEDULED"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED"),
    
    CANCELLED("CANCELLED");

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

  private @Nullable StatusEnum status;

  @Valid
  private List<String> objectives = new ArrayList<>();

  private @Nullable Object attackerForces;

  private @Nullable Object defenderForces;

  public SiegePlan() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SiegePlan(String siegeId, String territoryId, OffsetDateTime startTime, Integer durationMinutes) {
    this.siegeId = siegeId;
    this.territoryId = territoryId;
    this.startTime = startTime;
    this.durationMinutes = durationMinutes;
  }

  public SiegePlan siegeId(String siegeId) {
    this.siegeId = siegeId;
    return this;
  }

  /**
   * Get siegeId
   * @return siegeId
   */
  @NotNull 
  @Schema(name = "siegeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("siegeId")
  public String getSiegeId() {
    return siegeId;
  }

  public void setSiegeId(String siegeId) {
    this.siegeId = siegeId;
  }

  public SiegePlan territoryId(String territoryId) {
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

  public SiegePlan startTime(OffsetDateTime startTime) {
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

  public SiegePlan durationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * @return durationMinutes
   */
  @NotNull 
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("durationMinutes")
  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public SiegePlan status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public SiegePlan objectives(List<String> objectives) {
    this.objectives = objectives;
    return this;
  }

  public SiegePlan addObjectivesItem(String objectivesItem) {
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

  public SiegePlan attackerForces(@Nullable Object attackerForces) {
    this.attackerForces = attackerForces;
    return this;
  }

  /**
   * Get attackerForces
   * @return attackerForces
   */
  
  @Schema(name = "attackerForces", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attackerForces")
  public @Nullable Object getAttackerForces() {
    return attackerForces;
  }

  public void setAttackerForces(@Nullable Object attackerForces) {
    this.attackerForces = attackerForces;
  }

  public SiegePlan defenderForces(@Nullable Object defenderForces) {
    this.defenderForces = defenderForces;
    return this;
  }

  /**
   * Get defenderForces
   * @return defenderForces
   */
  
  @Schema(name = "defenderForces", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defenderForces")
  public @Nullable Object getDefenderForces() {
    return defenderForces;
  }

  public void setDefenderForces(@Nullable Object defenderForces) {
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
    SiegePlan siegePlan = (SiegePlan) o;
    return Objects.equals(this.siegeId, siegePlan.siegeId) &&
        Objects.equals(this.territoryId, siegePlan.territoryId) &&
        Objects.equals(this.startTime, siegePlan.startTime) &&
        Objects.equals(this.durationMinutes, siegePlan.durationMinutes) &&
        Objects.equals(this.status, siegePlan.status) &&
        Objects.equals(this.objectives, siegePlan.objectives) &&
        Objects.equals(this.attackerForces, siegePlan.attackerForces) &&
        Objects.equals(this.defenderForces, siegePlan.defenderForces);
  }

  @Override
  public int hashCode() {
    return Objects.hash(siegeId, territoryId, startTime, durationMinutes, status, objectives, attackerForces, defenderForces);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SiegePlan {\n");
    sb.append("    siegeId: ").append(toIndentedString(siegeId)).append("\n");
    sb.append("    territoryId: ").append(toIndentedString(territoryId)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

