package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ParticipantInitInitialPosition;
import com.necpgame.gameplayservice.model.ParticipantStats;
import com.necpgame.gameplayservice.model.StatusEffect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

  private @Nullable String id;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PLAYER("PLAYER"),
    
    NPC("NPC"),
    
    AI_ENEMY("AI_ENEMY");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String team;

  private @Nullable String characterName;

  private @Nullable Integer hp;

  private @Nullable Integer maxHp;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ALIVE("ALIVE"),
    
    DOWNED("DOWNED"),
    
    DEAD("DEAD");

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
  private List<@Valid StatusEffect> statusEffects = new ArrayList<>();

  private @Nullable ParticipantInitInitialPosition position;

  private @Nullable ParticipantStats stats;

  public Participant id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public Participant type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public Participant team(@Nullable String team) {
    this.team = team;
    return this;
  }

  /**
   * Get team
   * @return team
   */
  
  @Schema(name = "team", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("team")
  public @Nullable String getTeam() {
    return team;
  }

  public void setTeam(@Nullable String team) {
    this.team = team;
  }

  public Participant characterName(@Nullable String characterName) {
    this.characterName = characterName;
    return this;
  }

  /**
   * Get characterName
   * @return characterName
   */
  
  @Schema(name = "character_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_name")
  public @Nullable String getCharacterName() {
    return characterName;
  }

  public void setCharacterName(@Nullable String characterName) {
    this.characterName = characterName;
  }

  public Participant hp(@Nullable Integer hp) {
    this.hp = hp;
    return this;
  }

  /**
   * Get hp
   * @return hp
   */
  
  @Schema(name = "hp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hp")
  public @Nullable Integer getHp() {
    return hp;
  }

  public void setHp(@Nullable Integer hp) {
    this.hp = hp;
  }

  public Participant maxHp(@Nullable Integer maxHp) {
    this.maxHp = maxHp;
    return this;
  }

  /**
   * Get maxHp
   * @return maxHp
   */
  
  @Schema(name = "max_hp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_hp")
  public @Nullable Integer getMaxHp() {
    return maxHp;
  }

  public void setMaxHp(@Nullable Integer maxHp) {
    this.maxHp = maxHp;
  }

  public Participant status(@Nullable StatusEnum status) {
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
  @Schema(name = "status_effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status_effects")
  public List<@Valid StatusEffect> getStatusEffects() {
    return statusEffects;
  }

  public void setStatusEffects(List<@Valid StatusEffect> statusEffects) {
    this.statusEffects = statusEffects;
  }

  public Participant position(@Nullable ParticipantInitInitialPosition position) {
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
  public @Nullable ParticipantInitInitialPosition getPosition() {
    return position;
  }

  public void setPosition(@Nullable ParticipantInitInitialPosition position) {
    this.position = position;
  }

  public Participant stats(@Nullable ParticipantStats stats) {
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
  public @Nullable ParticipantStats getStats() {
    return stats;
  }

  public void setStats(@Nullable ParticipantStats stats) {
    this.stats = stats;
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
    return Objects.equals(this.id, participant.id) &&
        Objects.equals(this.type, participant.type) &&
        Objects.equals(this.team, participant.team) &&
        Objects.equals(this.characterName, participant.characterName) &&
        Objects.equals(this.hp, participant.hp) &&
        Objects.equals(this.maxHp, participant.maxHp) &&
        Objects.equals(this.status, participant.status) &&
        Objects.equals(this.statusEffects, participant.statusEffects) &&
        Objects.equals(this.position, participant.position) &&
        Objects.equals(this.stats, participant.stats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, team, characterName, hp, maxHp, status, statusEffects, position, stats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Participant {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    team: ").append(toIndentedString(team)).append("\n");
    sb.append("    characterName: ").append(toIndentedString(characterName)).append("\n");
    sb.append("    hp: ").append(toIndentedString(hp)).append("\n");
    sb.append("    maxHp: ").append(toIndentedString(maxHp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusEffects: ").append(toIndentedString(statusEffects)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
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

