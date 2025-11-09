package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.AutonomousSquadCompositionInner;
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
 * AutonomousSquadCreateRequest
 */


public class AutonomousSquadCreateRequest {

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

  private @Nullable UUID routeId;

  private @Nullable UUID originSettlementId;

  private Integer strength;

  @Valid
  private List<@Valid AutonomousSquadCompositionInner> composition = new ArrayList<>();

  private @Nullable UUID escortingPlayerId;

  private @Nullable Integer expectedDurationMinutes;

  private @Nullable String notes;

  public AutonomousSquadCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AutonomousSquadCreateRequest(UUID factionId, MissionEnum mission, Integer strength) {
    this.factionId = factionId;
    this.mission = mission;
    this.strength = strength;
  }

  public AutonomousSquadCreateRequest factionId(UUID factionId) {
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

  public AutonomousSquadCreateRequest mission(MissionEnum mission) {
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

  public AutonomousSquadCreateRequest routeId(@Nullable UUID routeId) {
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

  public AutonomousSquadCreateRequest originSettlementId(@Nullable UUID originSettlementId) {
    this.originSettlementId = originSettlementId;
    return this;
  }

  /**
   * Get originSettlementId
   * @return originSettlementId
   */
  @Valid 
  @Schema(name = "originSettlementId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("originSettlementId")
  public @Nullable UUID getOriginSettlementId() {
    return originSettlementId;
  }

  public void setOriginSettlementId(@Nullable UUID originSettlementId) {
    this.originSettlementId = originSettlementId;
  }

  public AutonomousSquadCreateRequest strength(Integer strength) {
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

  public AutonomousSquadCreateRequest composition(List<@Valid AutonomousSquadCompositionInner> composition) {
    this.composition = composition;
    return this;
  }

  public AutonomousSquadCreateRequest addCompositionItem(AutonomousSquadCompositionInner compositionItem) {
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

  public AutonomousSquadCreateRequest escortingPlayerId(@Nullable UUID escortingPlayerId) {
    this.escortingPlayerId = escortingPlayerId;
    return this;
  }

  /**
   * Get escortingPlayerId
   * @return escortingPlayerId
   */
  @Valid 
  @Schema(name = "escortingPlayerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escortingPlayerId")
  public @Nullable UUID getEscortingPlayerId() {
    return escortingPlayerId;
  }

  public void setEscortingPlayerId(@Nullable UUID escortingPlayerId) {
    this.escortingPlayerId = escortingPlayerId;
  }

  public AutonomousSquadCreateRequest expectedDurationMinutes(@Nullable Integer expectedDurationMinutes) {
    this.expectedDurationMinutes = expectedDurationMinutes;
    return this;
  }

  /**
   * Get expectedDurationMinutes
   * @return expectedDurationMinutes
   */
  
  @Schema(name = "expectedDurationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedDurationMinutes")
  public @Nullable Integer getExpectedDurationMinutes() {
    return expectedDurationMinutes;
  }

  public void setExpectedDurationMinutes(@Nullable Integer expectedDurationMinutes) {
    this.expectedDurationMinutes = expectedDurationMinutes;
  }

  public AutonomousSquadCreateRequest notes(@Nullable String notes) {
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
    AutonomousSquadCreateRequest autonomousSquadCreateRequest = (AutonomousSquadCreateRequest) o;
    return Objects.equals(this.factionId, autonomousSquadCreateRequest.factionId) &&
        Objects.equals(this.mission, autonomousSquadCreateRequest.mission) &&
        Objects.equals(this.routeId, autonomousSquadCreateRequest.routeId) &&
        Objects.equals(this.originSettlementId, autonomousSquadCreateRequest.originSettlementId) &&
        Objects.equals(this.strength, autonomousSquadCreateRequest.strength) &&
        Objects.equals(this.composition, autonomousSquadCreateRequest.composition) &&
        Objects.equals(this.escortingPlayerId, autonomousSquadCreateRequest.escortingPlayerId) &&
        Objects.equals(this.expectedDurationMinutes, autonomousSquadCreateRequest.expectedDurationMinutes) &&
        Objects.equals(this.notes, autonomousSquadCreateRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, mission, routeId, originSettlementId, strength, composition, escortingPlayerId, expectedDurationMinutes, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutonomousSquadCreateRequest {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    mission: ").append(toIndentedString(mission)).append("\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    originSettlementId: ").append(toIndentedString(originSettlementId)).append("\n");
    sb.append("    strength: ").append(toIndentedString(strength)).append("\n");
    sb.append("    composition: ").append(toIndentedString(composition)).append("\n");
    sb.append("    escortingPlayerId: ").append(toIndentedString(escortingPlayerId)).append("\n");
    sb.append("    expectedDurationMinutes: ").append(toIndentedString(expectedDurationMinutes)).append("\n");
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

