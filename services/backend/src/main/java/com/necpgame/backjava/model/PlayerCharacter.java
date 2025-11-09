package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
 * PlayerCharacter
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerCharacter {

  private @Nullable String characterId;

  private @Nullable String playerId;

  private @Nullable String name;

  private @Nullable String classId;

  private @Nullable Integer level;

  private @Nullable BigDecimal experience;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastLogin;

  private @Nullable Boolean isDeleted;

  public PlayerCharacter characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public PlayerCharacter playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public PlayerCharacter name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public PlayerCharacter classId(@Nullable String classId) {
    this.classId = classId;
    return this;
  }

  /**
   * Get classId
   * @return classId
   */
  
  @Schema(name = "class_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_id")
  public @Nullable String getClassId() {
    return classId;
  }

  public void setClassId(@Nullable String classId) {
    this.classId = classId;
  }

  public PlayerCharacter level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public PlayerCharacter experience(@Nullable BigDecimal experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  @Valid 
  @Schema(name = "experience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable BigDecimal getExperience() {
    return experience;
  }

  public void setExperience(@Nullable BigDecimal experience) {
    this.experience = experience;
  }

  public PlayerCharacter createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PlayerCharacter lastLogin(@Nullable OffsetDateTime lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  /**
   * Get lastLogin
   * @return lastLogin
   */
  @Valid 
  @Schema(name = "last_login", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_login")
  public @Nullable OffsetDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(@Nullable OffsetDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public PlayerCharacter isDeleted(@Nullable Boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  /**
   * Get isDeleted
   * @return isDeleted
   */
  
  @Schema(name = "is_deleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_deleted")
  public @Nullable Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(@Nullable Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerCharacter playerCharacter = (PlayerCharacter) o;
    return Objects.equals(this.characterId, playerCharacter.characterId) &&
        Objects.equals(this.playerId, playerCharacter.playerId) &&
        Objects.equals(this.name, playerCharacter.name) &&
        Objects.equals(this.classId, playerCharacter.classId) &&
        Objects.equals(this.level, playerCharacter.level) &&
        Objects.equals(this.experience, playerCharacter.experience) &&
        Objects.equals(this.createdAt, playerCharacter.createdAt) &&
        Objects.equals(this.lastLogin, playerCharacter.lastLogin) &&
        Objects.equals(this.isDeleted, playerCharacter.isDeleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, playerId, name, classId, level, experience, createdAt, lastLogin, isDeleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerCharacter {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
    sb.append("    isDeleted: ").append(toIndentedString(isDeleted)).append("\n");
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

