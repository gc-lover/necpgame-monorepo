package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.CharacterStateHealth;
import com.necpgame.adminservice.model.CharacterStatePosition;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * CharacterState
 */


public class CharacterState {

  private @Nullable UUID characterId;

  private @Nullable Integer version;

  private @Nullable CharacterStatePosition position;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ONLINE("ONLINE"),
    
    OFFLINE("OFFLINE"),
    
    IN_COMBAT("IN_COMBAT"),
    
    IN_QUEST("IN_QUEST"),
    
    IN_TRADE("IN_TRADE"),
    
    AFK("AFK");

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

  private @Nullable CharacterStateHealth health;

  @Valid
  private List<Object> activeEffects = new ArrayList<>();

  @Valid
  private Map<String, String> questStates = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  public CharacterState characterId(@Nullable UUID characterId) {
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

  public CharacterState version(@Nullable Integer version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable Integer getVersion() {
    return version;
  }

  public void setVersion(@Nullable Integer version) {
    this.version = version;
  }

  public CharacterState position(@Nullable CharacterStatePosition position) {
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
  public @Nullable CharacterStatePosition getPosition() {
    return position;
  }

  public void setPosition(@Nullable CharacterStatePosition position) {
    this.position = position;
  }

  public CharacterState status(@Nullable StatusEnum status) {
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

  public CharacterState health(@Nullable CharacterStateHealth health) {
    this.health = health;
    return this;
  }

  /**
   * Get health
   * @return health
   */
  @Valid 
  @Schema(name = "health", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("health")
  public @Nullable CharacterStateHealth getHealth() {
    return health;
  }

  public void setHealth(@Nullable CharacterStateHealth health) {
    this.health = health;
  }

  public CharacterState activeEffects(List<Object> activeEffects) {
    this.activeEffects = activeEffects;
    return this;
  }

  public CharacterState addActiveEffectsItem(Object activeEffectsItem) {
    if (this.activeEffects == null) {
      this.activeEffects = new ArrayList<>();
    }
    this.activeEffects.add(activeEffectsItem);
    return this;
  }

  /**
   * Get activeEffects
   * @return activeEffects
   */
  
  @Schema(name = "active_effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_effects")
  public List<Object> getActiveEffects() {
    return activeEffects;
  }

  public void setActiveEffects(List<Object> activeEffects) {
    this.activeEffects = activeEffects;
  }

  public CharacterState questStates(Map<String, String> questStates) {
    this.questStates = questStates;
    return this;
  }

  public CharacterState putQuestStatesItem(String key, String questStatesItem) {
    if (this.questStates == null) {
      this.questStates = new HashMap<>();
    }
    this.questStates.put(key, questStatesItem);
    return this;
  }

  /**
   * Get questStates
   * @return questStates
   */
  
  @Schema(name = "quest_states", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_states")
  public Map<String, String> getQuestStates() {
    return questStates;
  }

  public void setQuestStates(Map<String, String> questStates) {
    this.questStates = questStates;
  }

  public CharacterState lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "last_updated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_updated")
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
    CharacterState characterState = (CharacterState) o;
    return Objects.equals(this.characterId, characterState.characterId) &&
        Objects.equals(this.version, characterState.version) &&
        Objects.equals(this.position, characterState.position) &&
        Objects.equals(this.status, characterState.status) &&
        Objects.equals(this.health, characterState.health) &&
        Objects.equals(this.activeEffects, characterState.activeEffects) &&
        Objects.equals(this.questStates, characterState.questStates) &&
        Objects.equals(this.lastUpdated, characterState.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, version, position, status, health, activeEffects, questStates, lastUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterState {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
    sb.append("    activeEffects: ").append(toIndentedString(activeEffects)).append("\n");
    sb.append("    questStates: ").append(toIndentedString(questStates)).append("\n");
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

