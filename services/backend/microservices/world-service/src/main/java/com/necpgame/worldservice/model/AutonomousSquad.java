package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.AutonomousSquadCompositionInner;
import com.necpgame.worldservice.model.AutonomousSquadCurrentWaypoint;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * AutonomousSquad
 */


public class AutonomousSquad {

  private UUID squadId;

  private UUID factionId;

  /**
   * Gets or Sets mission
   */
  public enum MissionEnum {
    PATROL("patrol"),
    
    RAID("raid"),
    
    ESCORT("escort"),
    
    TRADE("trade"),
    
    SUPPORT("support");

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

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    FORMING("forming"),
    
    EN_ROUTE("en_route"),
    
    ENGAGED("engaged"),
    
    RETREATING("retreating"),
    
    DESTROYED("destroyed");

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

  private Integer strength;

  private Integer threatLevel;

  private @Nullable AutonomousSquadCurrentWaypoint currentWaypoint;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eta;

  private @Nullable UUID routeId;

  @Valid
  private List<@Valid AutonomousSquadCompositionInner> composition = new ArrayList<>();

  @Valid
  private List<String> supportAbilities = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  public AutonomousSquad() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AutonomousSquad(UUID squadId, UUID factionId, MissionEnum mission, StatusEnum status, Integer strength, Integer threatLevel) {
    this.squadId = squadId;
    this.factionId = factionId;
    this.mission = mission;
    this.status = status;
    this.strength = strength;
    this.threatLevel = threatLevel;
  }

  public AutonomousSquad squadId(UUID squadId) {
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

  public AutonomousSquad factionId(UUID factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  @NotNull @Valid 
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factionId")
  public UUID getFactionId() {
    return factionId;
  }

  public void setFactionId(UUID factionId) {
    this.factionId = factionId;
  }

  public AutonomousSquad mission(MissionEnum mission) {
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

  public AutonomousSquad status(StatusEnum status) {
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

  public AutonomousSquad strength(Integer strength) {
    this.strength = strength;
    return this;
  }

  /**
   * Get strength
   * minimum: 1
   * @return strength
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "strength", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("strength")
  public Integer getStrength() {
    return strength;
  }

  public void setStrength(Integer strength) {
    this.strength = strength;
  }

  public AutonomousSquad threatLevel(Integer threatLevel) {
    this.threatLevel = threatLevel;
    return this;
  }

  /**
   * Get threatLevel
   * minimum: 0
   * maximum: 5
   * @return threatLevel
   */
  @NotNull @Min(value = 0) @Max(value = 5) 
  @Schema(name = "threatLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("threatLevel")
  public Integer getThreatLevel() {
    return threatLevel;
  }

  public void setThreatLevel(Integer threatLevel) {
    this.threatLevel = threatLevel;
  }

  public AutonomousSquad currentWaypoint(@Nullable AutonomousSquadCurrentWaypoint currentWaypoint) {
    this.currentWaypoint = currentWaypoint;
    return this;
  }

  /**
   * Get currentWaypoint
   * @return currentWaypoint
   */
  @Valid 
  @Schema(name = "currentWaypoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentWaypoint")
  public @Nullable AutonomousSquadCurrentWaypoint getCurrentWaypoint() {
    return currentWaypoint;
  }

  public void setCurrentWaypoint(@Nullable AutonomousSquadCurrentWaypoint currentWaypoint) {
    this.currentWaypoint = currentWaypoint;
  }

  public AutonomousSquad eta(@Nullable OffsetDateTime eta) {
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

  public AutonomousSquad routeId(@Nullable UUID routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  @Valid 
  @Schema(name = "routeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routeId")
  public @Nullable UUID getRouteId() {
    return routeId;
  }

  public void setRouteId(@Nullable UUID routeId) {
    this.routeId = routeId;
  }

  public AutonomousSquad composition(List<@Valid AutonomousSquadCompositionInner> composition) {
    this.composition = composition;
    return this;
  }

  public AutonomousSquad addCompositionItem(AutonomousSquadCompositionInner compositionItem) {
    if (this.composition == null) {
      this.composition = new ArrayList<>();
    }
    this.composition.add(compositionItem);
    return this;
  }

  /**
   * Get composition
   * @return composition
   */
  @Valid 
  @Schema(name = "composition", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("composition")
  public List<@Valid AutonomousSquadCompositionInner> getComposition() {
    return composition;
  }

  public void setComposition(List<@Valid AutonomousSquadCompositionInner> composition) {
    this.composition = composition;
  }

  public AutonomousSquad supportAbilities(List<String> supportAbilities) {
    this.supportAbilities = supportAbilities;
    return this;
  }

  public AutonomousSquad addSupportAbilitiesItem(String supportAbilitiesItem) {
    if (this.supportAbilities == null) {
      this.supportAbilities = new ArrayList<>();
    }
    this.supportAbilities.add(supportAbilitiesItem);
    return this;
  }

  /**
   * Get supportAbilities
   * @return supportAbilities
   */
  
  @Schema(name = "supportAbilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supportAbilities")
  public List<String> getSupportAbilities() {
    return supportAbilities;
  }

  public void setSupportAbilities(List<String> supportAbilities) {
    this.supportAbilities = supportAbilities;
  }

  public AutonomousSquad lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "lastUpdated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdated")
  public @Nullable OffsetDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutonomousSquad autonomousSquad = (AutonomousSquad) o;
    return Objects.equals(this.squadId, autonomousSquad.squadId) &&
        Objects.equals(this.factionId, autonomousSquad.factionId) &&
        Objects.equals(this.mission, autonomousSquad.mission) &&
        Objects.equals(this.status, autonomousSquad.status) &&
        Objects.equals(this.strength, autonomousSquad.strength) &&
        Objects.equals(this.threatLevel, autonomousSquad.threatLevel) &&
        Objects.equals(this.currentWaypoint, autonomousSquad.currentWaypoint) &&
        Objects.equals(this.eta, autonomousSquad.eta) &&
        Objects.equals(this.routeId, autonomousSquad.routeId) &&
        Objects.equals(this.composition, autonomousSquad.composition) &&
        Objects.equals(this.supportAbilities, autonomousSquad.supportAbilities) &&
        Objects.equals(this.lastUpdated, autonomousSquad.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(squadId, factionId, mission, status, strength, threatLevel, currentWaypoint, eta, routeId, composition, supportAbilities, lastUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutonomousSquad {\n");
    sb.append("    squadId: ").append(toIndentedString(squadId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    mission: ").append(toIndentedString(mission)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    strength: ").append(toIndentedString(strength)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
    sb.append("    currentWaypoint: ").append(toIndentedString(currentWaypoint)).append("\n");
    sb.append("    eta: ").append(toIndentedString(eta)).append("\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    composition: ").append(toIndentedString(composition)).append("\n");
    sb.append("    supportAbilities: ").append(toIndentedString(supportAbilities)).append("\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
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

