package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.RomanceEvent;
import com.necpgame.socialservice.model.RomanceNPC;
import com.necpgame.socialservice.model.RomanceRelationshipDetailedAllOfRelationshipHistory;
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
 * RomanceRelationshipDetailed
 */


public class RomanceRelationshipDetailed {

  private @Nullable UUID relationshipId;

  private @Nullable UUID characterId;

  private @Nullable String npcId;

  private @Nullable String npcName;

  /**
   * Gets or Sets stage
   */
  public enum StageEnum {
    MEETING("MEETING"),
    
    FRIENDSHIP("FRIENDSHIP"),
    
    FLIRTING("FLIRTING"),
    
    DATING("DATING"),
    
    INTIMACY("INTIMACY"),
    
    CONFLICT("CONFLICT"),
    
    RECONCILIATION("RECONCILIATION"),
    
    COMMITMENT("COMMITMENT"),
    
    BREAKUP("BREAKUP");

    private final String value;

    StageEnum(String value) {
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
    public static StageEnum fromValue(String value) {
      for (StageEnum b : StageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StageEnum stage;

  private @Nullable Integer affectionLevel;

  private @Nullable Integer trustLevel;

  private @Nullable Integer jealousyLevel;

  private @Nullable Integer eventsCompleted;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  private @Nullable RomanceNPC npcDetails;

  @Valid
  private List<@Valid RomanceRelationshipDetailedAllOfRelationshipHistory> relationshipHistory = new ArrayList<>();

  @Valid
  private List<@Valid RomanceEvent> availableEvents = new ArrayList<>();

  @Valid
  private List<Object> conflicts = new ArrayList<>();

  public RomanceRelationshipDetailed relationshipId(@Nullable UUID relationshipId) {
    this.relationshipId = relationshipId;
    return this;
  }

  /**
   * Get relationshipId
   * @return relationshipId
   */
  @Valid 
  @Schema(name = "relationship_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_id")
  public @Nullable UUID getRelationshipId() {
    return relationshipId;
  }

  public void setRelationshipId(@Nullable UUID relationshipId) {
    this.relationshipId = relationshipId;
  }

  public RomanceRelationshipDetailed characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public RomanceRelationshipDetailed npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public RomanceRelationshipDetailed npcName(@Nullable String npcName) {
    this.npcName = npcName;
    return this;
  }

  /**
   * Get npcName
   * @return npcName
   */
  
  @Schema(name = "npc_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_name")
  public @Nullable String getNpcName() {
    return npcName;
  }

  public void setNpcName(@Nullable String npcName) {
    this.npcName = npcName;
  }

  public RomanceRelationshipDetailed stage(@Nullable StageEnum stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Get stage
   * @return stage
   */
  
  @Schema(name = "stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable StageEnum getStage() {
    return stage;
  }

  public void setStage(@Nullable StageEnum stage) {
    this.stage = stage;
  }

  public RomanceRelationshipDetailed affectionLevel(@Nullable Integer affectionLevel) {
    this.affectionLevel = affectionLevel;
    return this;
  }

  /**
   * Get affectionLevel
   * minimum: 0
   * maximum: 100
   * @return affectionLevel
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "affection_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affection_level")
  public @Nullable Integer getAffectionLevel() {
    return affectionLevel;
  }

  public void setAffectionLevel(@Nullable Integer affectionLevel) {
    this.affectionLevel = affectionLevel;
  }

  public RomanceRelationshipDetailed trustLevel(@Nullable Integer trustLevel) {
    this.trustLevel = trustLevel;
    return this;
  }

  /**
   * Get trustLevel
   * minimum: 0
   * maximum: 100
   * @return trustLevel
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "trust_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trust_level")
  public @Nullable Integer getTrustLevel() {
    return trustLevel;
  }

  public void setTrustLevel(@Nullable Integer trustLevel) {
    this.trustLevel = trustLevel;
  }

  public RomanceRelationshipDetailed jealousyLevel(@Nullable Integer jealousyLevel) {
    this.jealousyLevel = jealousyLevel;
    return this;
  }

  /**
   * Get jealousyLevel
   * minimum: 0
   * maximum: 100
   * @return jealousyLevel
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "jealousy_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("jealousy_level")
  public @Nullable Integer getJealousyLevel() {
    return jealousyLevel;
  }

  public void setJealousyLevel(@Nullable Integer jealousyLevel) {
    this.jealousyLevel = jealousyLevel;
  }

  public RomanceRelationshipDetailed eventsCompleted(@Nullable Integer eventsCompleted) {
    this.eventsCompleted = eventsCompleted;
    return this;
  }

  /**
   * Get eventsCompleted
   * @return eventsCompleted
   */
  
  @Schema(name = "events_completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events_completed")
  public @Nullable Integer getEventsCompleted() {
    return eventsCompleted;
  }

  public void setEventsCompleted(@Nullable Integer eventsCompleted) {
    this.eventsCompleted = eventsCompleted;
  }

  public RomanceRelationshipDetailed startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public RomanceRelationshipDetailed npcDetails(@Nullable RomanceNPC npcDetails) {
    this.npcDetails = npcDetails;
    return this;
  }

  /**
   * Get npcDetails
   * @return npcDetails
   */
  @Valid 
  @Schema(name = "npc_details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_details")
  public @Nullable RomanceNPC getNpcDetails() {
    return npcDetails;
  }

  public void setNpcDetails(@Nullable RomanceNPC npcDetails) {
    this.npcDetails = npcDetails;
  }

  public RomanceRelationshipDetailed relationshipHistory(List<@Valid RomanceRelationshipDetailedAllOfRelationshipHistory> relationshipHistory) {
    this.relationshipHistory = relationshipHistory;
    return this;
  }

  public RomanceRelationshipDetailed addRelationshipHistoryItem(RomanceRelationshipDetailedAllOfRelationshipHistory relationshipHistoryItem) {
    if (this.relationshipHistory == null) {
      this.relationshipHistory = new ArrayList<>();
    }
    this.relationshipHistory.add(relationshipHistoryItem);
    return this;
  }

  /**
   * Get relationshipHistory
   * @return relationshipHistory
   */
  @Valid 
  @Schema(name = "relationship_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_history")
  public List<@Valid RomanceRelationshipDetailedAllOfRelationshipHistory> getRelationshipHistory() {
    return relationshipHistory;
  }

  public void setRelationshipHistory(List<@Valid RomanceRelationshipDetailedAllOfRelationshipHistory> relationshipHistory) {
    this.relationshipHistory = relationshipHistory;
  }

  public RomanceRelationshipDetailed availableEvents(List<@Valid RomanceEvent> availableEvents) {
    this.availableEvents = availableEvents;
    return this;
  }

  public RomanceRelationshipDetailed addAvailableEventsItem(RomanceEvent availableEventsItem) {
    if (this.availableEvents == null) {
      this.availableEvents = new ArrayList<>();
    }
    this.availableEvents.add(availableEventsItem);
    return this;
  }

  /**
   * Get availableEvents
   * @return availableEvents
   */
  @Valid 
  @Schema(name = "available_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_events")
  public List<@Valid RomanceEvent> getAvailableEvents() {
    return availableEvents;
  }

  public void setAvailableEvents(List<@Valid RomanceEvent> availableEvents) {
    this.availableEvents = availableEvents;
  }

  public RomanceRelationshipDetailed conflicts(List<Object> conflicts) {
    this.conflicts = conflicts;
    return this;
  }

  public RomanceRelationshipDetailed addConflictsItem(Object conflictsItem) {
    if (this.conflicts == null) {
      this.conflicts = new ArrayList<>();
    }
    this.conflicts.add(conflictsItem);
    return this;
  }

  /**
   * Активные конфликты
   * @return conflicts
   */
  
  @Schema(name = "conflicts", description = "Активные конфликты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflicts")
  public List<Object> getConflicts() {
    return conflicts;
  }

  public void setConflicts(List<Object> conflicts) {
    this.conflicts = conflicts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceRelationshipDetailed romanceRelationshipDetailed = (RomanceRelationshipDetailed) o;
    return Objects.equals(this.relationshipId, romanceRelationshipDetailed.relationshipId) &&
        Objects.equals(this.characterId, romanceRelationshipDetailed.characterId) &&
        Objects.equals(this.npcId, romanceRelationshipDetailed.npcId) &&
        Objects.equals(this.npcName, romanceRelationshipDetailed.npcName) &&
        Objects.equals(this.stage, romanceRelationshipDetailed.stage) &&
        Objects.equals(this.affectionLevel, romanceRelationshipDetailed.affectionLevel) &&
        Objects.equals(this.trustLevel, romanceRelationshipDetailed.trustLevel) &&
        Objects.equals(this.jealousyLevel, romanceRelationshipDetailed.jealousyLevel) &&
        Objects.equals(this.eventsCompleted, romanceRelationshipDetailed.eventsCompleted) &&
        Objects.equals(this.startedAt, romanceRelationshipDetailed.startedAt) &&
        Objects.equals(this.npcDetails, romanceRelationshipDetailed.npcDetails) &&
        Objects.equals(this.relationshipHistory, romanceRelationshipDetailed.relationshipHistory) &&
        Objects.equals(this.availableEvents, romanceRelationshipDetailed.availableEvents) &&
        Objects.equals(this.conflicts, romanceRelationshipDetailed.conflicts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, characterId, npcId, npcName, stage, affectionLevel, trustLevel, jealousyLevel, eventsCompleted, startedAt, npcDetails, relationshipHistory, availableEvents, conflicts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceRelationshipDetailed {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    npcName: ").append(toIndentedString(npcName)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    affectionLevel: ").append(toIndentedString(affectionLevel)).append("\n");
    sb.append("    trustLevel: ").append(toIndentedString(trustLevel)).append("\n");
    sb.append("    jealousyLevel: ").append(toIndentedString(jealousyLevel)).append("\n");
    sb.append("    eventsCompleted: ").append(toIndentedString(eventsCompleted)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    npcDetails: ").append(toIndentedString(npcDetails)).append("\n");
    sb.append("    relationshipHistory: ").append(toIndentedString(relationshipHistory)).append("\n");
    sb.append("    availableEvents: ").append(toIndentedString(availableEvents)).append("\n");
    sb.append("    conflicts: ").append(toIndentedString(conflicts)).append("\n");
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

