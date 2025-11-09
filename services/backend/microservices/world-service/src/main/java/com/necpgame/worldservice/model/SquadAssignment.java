package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * SquadAssignment
 */


public class SquadAssignment {

  private UUID squadId;

  /**
   * Gets or Sets mission
   */
  public enum MissionEnum {
    ESCORT("escort"),
    
    PATROL("patrol"),
    
    INTERCEPT("intercept"),
    
    SCOUT("scout");

    private final String value;

    MissionEnum(String value) {
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
    public static MissionEnum fromValue(String value) {
      for (MissionEnum b : MissionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MissionEnum mission;

  private Integer strength;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eta;

  public SquadAssignment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SquadAssignment(UUID squadId, MissionEnum mission, Integer strength) {
    this.squadId = squadId;
    this.mission = mission;
    this.strength = strength;
  }

  public SquadAssignment squadId(UUID squadId) {
    this.squadId = squadId;
    return this;
  }

  /**
   * Get squadId
   * @return squadId
   */
  @NotNull @Valid 
  @Schema(name = "squadId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("squadId")
  public UUID getSquadId() {
    return squadId;
  }

  public void setSquadId(UUID squadId) {
    this.squadId = squadId;
  }

  public SquadAssignment mission(MissionEnum mission) {
    this.mission = mission;
    return this;
  }

  /**
   * Get mission
   * @return mission
   */
  @NotNull 
  @Schema(name = "mission", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mission")
  public MissionEnum getMission() {
    return mission;
  }

  public void setMission(MissionEnum mission) {
    this.mission = mission;
  }

  public SquadAssignment strength(Integer strength) {
    this.strength = strength;
    return this;
  }

  /**
   * Get strength
   * @return strength
   */
  @NotNull 
  @Schema(name = "strength", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("strength")
  public Integer getStrength() {
    return strength;
  }

  public void setStrength(Integer strength) {
    this.strength = strength;
  }

  public SquadAssignment eta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
    return this;
  }

  /**
   * Get eta
   * @return eta
   */
  @Valid 
  @Schema(name = "eta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eta")
  public @Nullable OffsetDateTime getEta() {
    return eta;
  }

  public void setEta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SquadAssignment squadAssignment = (SquadAssignment) o;
    return Objects.equals(this.squadId, squadAssignment.squadId) &&
        Objects.equals(this.mission, squadAssignment.mission) &&
        Objects.equals(this.strength, squadAssignment.strength) &&
        Objects.equals(this.eta, squadAssignment.eta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(squadId, mission, strength, eta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SquadAssignment {\n");
    sb.append("    squadId: ").append(toIndentedString(squadId)).append("\n");
    sb.append("    mission: ").append(toIndentedString(mission)).append("\n");
    sb.append("    strength: ").append(toIndentedString(strength)).append("\n");
    sb.append("    eta: ").append(toIndentedString(eta)).append("\n");
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

