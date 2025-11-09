package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ControlShiftRequestEvidence;
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
 * ControlShiftRequest
 */


public class ControlShiftRequest {

  private UUID regionId;

  private UUID newOwnerFactionId;

  /**
   * Gets or Sets trigger
   */
  public enum TriggerEnum {
    LEADER_DEATH("leader_death"),
    
    RAID_VICTORY("raid_victory"),
    
    ECONOMIC_COLLAPSE("economic_collapse"),
    
    PLAYER_CONTRACT("player_contract"),
    
    STORY_EVENT("story_event");

    private final String value;

    TriggerEnum(String value) {
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
    public static TriggerEnum fromValue(String value) {
      for (TriggerEnum b : TriggerEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TriggerEnum trigger;

  private ControlShiftRequestEvidence evidence;

  private Integer controlScoreDelta;

  private @Nullable String justification;

  private @Nullable String requestedBy;

  public ControlShiftRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ControlShiftRequest(UUID regionId, UUID newOwnerFactionId, TriggerEnum trigger, ControlShiftRequestEvidence evidence, Integer controlScoreDelta) {
    this.regionId = regionId;
    this.newOwnerFactionId = newOwnerFactionId;
    this.trigger = trigger;
    this.evidence = evidence;
    this.controlScoreDelta = controlScoreDelta;
  }

  public ControlShiftRequest regionId(UUID regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * Get regionId
   * @return regionId
   */
  @NotNull @Valid 
  @Schema(name = "regionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionId")
  public UUID getRegionId() {
    return regionId;
  }

  public void setRegionId(UUID regionId) {
    this.regionId = regionId;
  }

  public ControlShiftRequest newOwnerFactionId(UUID newOwnerFactionId) {
    this.newOwnerFactionId = newOwnerFactionId;
    return this;
  }

  /**
   * Get newOwnerFactionId
   * @return newOwnerFactionId
   */
  @NotNull @Valid 
  @Schema(name = "newOwnerFactionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newOwnerFactionId")
  public UUID getNewOwnerFactionId() {
    return newOwnerFactionId;
  }

  public void setNewOwnerFactionId(UUID newOwnerFactionId) {
    this.newOwnerFactionId = newOwnerFactionId;
  }

  public ControlShiftRequest trigger(TriggerEnum trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * Get trigger
   * @return trigger
   */
  @NotNull 
  @Schema(name = "trigger", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trigger")
  public TriggerEnum getTrigger() {
    return trigger;
  }

  public void setTrigger(TriggerEnum trigger) {
    this.trigger = trigger;
  }

  public ControlShiftRequest evidence(ControlShiftRequestEvidence evidence) {
    this.evidence = evidence;
    return this;
  }

  /**
   * Get evidence
   * @return evidence
   */
  @NotNull @Valid 
  @Schema(name = "evidence", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("evidence")
  public ControlShiftRequestEvidence getEvidence() {
    return evidence;
  }

  public void setEvidence(ControlShiftRequestEvidence evidence) {
    this.evidence = evidence;
  }

  public ControlShiftRequest controlScoreDelta(Integer controlScoreDelta) {
    this.controlScoreDelta = controlScoreDelta;
    return this;
  }

  /**
   * Get controlScoreDelta
   * minimum: -200
   * maximum: 200
   * @return controlScoreDelta
   */
  @NotNull @Min(value = -200) @Max(value = 200) 
  @Schema(name = "controlScoreDelta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("controlScoreDelta")
  public Integer getControlScoreDelta() {
    return controlScoreDelta;
  }

  public void setControlScoreDelta(Integer controlScoreDelta) {
    this.controlScoreDelta = controlScoreDelta;
  }

  public ControlShiftRequest justification(@Nullable String justification) {
    this.justification = justification;
    return this;
  }

  /**
   * Get justification
   * @return justification
   */
  
  @Schema(name = "justification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("justification")
  public @Nullable String getJustification() {
    return justification;
  }

  public void setJustification(@Nullable String justification) {
    this.justification = justification;
  }

  public ControlShiftRequest requestedBy(@Nullable String requestedBy) {
    this.requestedBy = requestedBy;
    return this;
  }

  /**
   * Контекст инициатора (GM, factionAI, playerContract).
   * @return requestedBy
   */
  
  @Schema(name = "requestedBy", description = "Контекст инициатора (GM, factionAI, playerContract).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedBy")
  public @Nullable String getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(@Nullable String requestedBy) {
    this.requestedBy = requestedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ControlShiftRequest controlShiftRequest = (ControlShiftRequest) o;
    return Objects.equals(this.regionId, controlShiftRequest.regionId) &&
        Objects.equals(this.newOwnerFactionId, controlShiftRequest.newOwnerFactionId) &&
        Objects.equals(this.trigger, controlShiftRequest.trigger) &&
        Objects.equals(this.evidence, controlShiftRequest.evidence) &&
        Objects.equals(this.controlScoreDelta, controlShiftRequest.controlScoreDelta) &&
        Objects.equals(this.justification, controlShiftRequest.justification) &&
        Objects.equals(this.requestedBy, controlShiftRequest.requestedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regionId, newOwnerFactionId, trigger, evidence, controlScoreDelta, justification, requestedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControlShiftRequest {\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    newOwnerFactionId: ").append(toIndentedString(newOwnerFactionId)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
    sb.append("    controlScoreDelta: ").append(toIndentedString(controlScoreDelta)).append("\n");
    sb.append("    justification: ").append(toIndentedString(justification)).append("\n");
    sb.append("    requestedBy: ").append(toIndentedString(requestedBy)).append("\n");
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

