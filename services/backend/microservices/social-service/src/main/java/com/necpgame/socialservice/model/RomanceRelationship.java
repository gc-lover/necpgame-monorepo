package com.necpgame.socialservice.model;

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
 * RomanceRelationship
 */


public class RomanceRelationship {

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

  public RomanceRelationship relationshipId(@Nullable UUID relationshipId) {
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

  public RomanceRelationship characterId(@Nullable UUID characterId) {
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

  public RomanceRelationship npcId(@Nullable String npcId) {
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

  public RomanceRelationship npcName(@Nullable String npcName) {
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

  public RomanceRelationship stage(@Nullable StageEnum stage) {
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

  public RomanceRelationship affectionLevel(@Nullable Integer affectionLevel) {
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

  public RomanceRelationship trustLevel(@Nullable Integer trustLevel) {
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

  public RomanceRelationship jealousyLevel(@Nullable Integer jealousyLevel) {
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

  public RomanceRelationship eventsCompleted(@Nullable Integer eventsCompleted) {
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

  public RomanceRelationship startedAt(@Nullable OffsetDateTime startedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceRelationship romanceRelationship = (RomanceRelationship) o;
    return Objects.equals(this.relationshipId, romanceRelationship.relationshipId) &&
        Objects.equals(this.characterId, romanceRelationship.characterId) &&
        Objects.equals(this.npcId, romanceRelationship.npcId) &&
        Objects.equals(this.npcName, romanceRelationship.npcName) &&
        Objects.equals(this.stage, romanceRelationship.stage) &&
        Objects.equals(this.affectionLevel, romanceRelationship.affectionLevel) &&
        Objects.equals(this.trustLevel, romanceRelationship.trustLevel) &&
        Objects.equals(this.jealousyLevel, romanceRelationship.jealousyLevel) &&
        Objects.equals(this.eventsCompleted, romanceRelationship.eventsCompleted) &&
        Objects.equals(this.startedAt, romanceRelationship.startedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, characterId, npcId, npcName, stage, affectionLevel, trustLevel, jealousyLevel, eventsCompleted, startedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceRelationship {\n");
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

