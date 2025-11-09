package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.combatservice.model.StatsSnapshot;
import com.necpgame.combatservice.model.StatusEffect;
import com.necpgame.combatservice.model.Vector3;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Participant
 */


public class Participant {

  private @Nullable String participantId;

  /**
   * Gets or Sets kind
   */
  public enum KindEnum {
    PLAYER("PLAYER"),
    
    NPC("NPC"),
    
    SUMMON("SUMMON"),
    
    PET("PET");

    private final String value;

    KindEnum(String value) {
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
    public static KindEnum fromValue(String value) {
      for (KindEnum b : KindEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable KindEnum kind;

  private @Nullable String referenceId;

  private @Nullable String teamId;

  private @Nullable String propertyClass;

  private @Nullable StatsSnapshot stats;

  @Valid
  private Map<String, Object> resources = new HashMap<>();

  private @Nullable Vector3 position;

  @Valid
  private List<@Valid StatusEffect> statusEffects = new ArrayList<>();

  public Participant participantId(@Nullable String participantId) {
    this.participantId = participantId;
    return this;
  }

  /**
   * Get participantId
   * @return participantId
   */
  
  @Schema(name = "participantId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participantId")
  public @Nullable String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(@Nullable String participantId) {
    this.participantId = participantId;
  }

  public Participant kind(@Nullable KindEnum kind) {
    this.kind = kind;
    return this;
  }

  /**
   * Get kind
   * @return kind
   */
  
  @Schema(name = "kind", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kind")
  public @Nullable KindEnum getKind() {
    return kind;
  }

  public void setKind(@Nullable KindEnum kind) {
    this.kind = kind;
  }

  public Participant referenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  /**
   * Get referenceId
   * @return referenceId
   */
  
  @Schema(name = "referenceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referenceId")
  public @Nullable String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
  }

  public Participant teamId(@Nullable String teamId) {
    this.teamId = teamId;
    return this;
  }

  /**
   * Get teamId
   * @return teamId
   */
  
  @Schema(name = "teamId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("teamId")
  public @Nullable String getTeamId() {
    return teamId;
  }

  public void setTeamId(@Nullable String teamId) {
    this.teamId = teamId;
  }

  public Participant propertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Get propertyClass
   * @return propertyClass
   */
  
  @Schema(name = "class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class")
  public @Nullable String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public Participant stats(@Nullable StatsSnapshot stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  @Valid 
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public @Nullable StatsSnapshot getStats() {
    return stats;
  }

  public void setStats(@Nullable StatsSnapshot stats) {
    this.stats = stats;
  }

  public Participant resources(Map<String, Object> resources) {
    this.resources = resources;
    return this;
  }

  public Participant putResourcesItem(String key, Object resourcesItem) {
    if (this.resources == null) {
      this.resources = new HashMap<>();
    }
    this.resources.put(key, resourcesItem);
    return this;
  }

  /**
   * Get resources
   * @return resources
   */
  
  @Schema(name = "resources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resources")
  public Map<String, Object> getResources() {
    return resources;
  }

  public void setResources(Map<String, Object> resources) {
    this.resources = resources;
  }

  public Participant position(@Nullable Vector3 position) {
    this.position = position;
    return this;
  }

  /**
   * Get position
   * @return position
   */
  @Valid 
  @Schema(name = "position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position")
  public @Nullable Vector3 getPosition() {
    return position;
  }

  public void setPosition(@Nullable Vector3 position) {
    this.position = position;
  }

  public Participant statusEffects(List<@Valid StatusEffect> statusEffects) {
    this.statusEffects = statusEffects;
    return this;
  }

  public Participant addStatusEffectsItem(StatusEffect statusEffectsItem) {
    if (this.statusEffects == null) {
      this.statusEffects = new ArrayList<>();
    }
    this.statusEffects.add(statusEffectsItem);
    return this;
  }

  /**
   * Get statusEffects
   * @return statusEffects
   */
  @Valid 
  @Schema(name = "statusEffects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusEffects")
  public List<@Valid StatusEffect> getStatusEffects() {
    return statusEffects;
  }

  public void setStatusEffects(List<@Valid StatusEffect> statusEffects) {
    this.statusEffects = statusEffects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Participant participant = (Participant) o;
    return Objects.equals(this.participantId, participant.participantId) &&
        Objects.equals(this.kind, participant.kind) &&
        Objects.equals(this.referenceId, participant.referenceId) &&
        Objects.equals(this.teamId, participant.teamId) &&
        Objects.equals(this.propertyClass, participant.propertyClass) &&
        Objects.equals(this.stats, participant.stats) &&
        Objects.equals(this.resources, participant.resources) &&
        Objects.equals(this.position, participant.position) &&
        Objects.equals(this.statusEffects, participant.statusEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participantId, kind, referenceId, teamId, propertyClass, stats, resources, position, statusEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Participant {\n");
    sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    referenceId: ").append(toIndentedString(referenceId)).append("\n");
    sb.append("    teamId: ").append(toIndentedString(teamId)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
    sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    statusEffects: ").append(toIndentedString(statusEffects)).append("\n");
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

