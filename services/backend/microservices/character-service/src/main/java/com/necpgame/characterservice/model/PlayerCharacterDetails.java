package com.necpgame.characterservice.model;

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
 * PlayerCharacterDetails
 */


public class PlayerCharacterDetails {

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

  private @Nullable Object attributes;

  private @Nullable Object skills;

  private @Nullable Object reputation;

  private @Nullable Object position;

  private @Nullable Object appearance;

  public PlayerCharacterDetails characterId(@Nullable String characterId) {
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

  public PlayerCharacterDetails playerId(@Nullable String playerId) {
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

  public PlayerCharacterDetails name(@Nullable String name) {
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

  public PlayerCharacterDetails classId(@Nullable String classId) {
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

  public PlayerCharacterDetails level(@Nullable Integer level) {
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

  public PlayerCharacterDetails experience(@Nullable BigDecimal experience) {
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

  public PlayerCharacterDetails createdAt(@Nullable OffsetDateTime createdAt) {
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

  public PlayerCharacterDetails lastLogin(@Nullable OffsetDateTime lastLogin) {
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

  public PlayerCharacterDetails isDeleted(@Nullable Boolean isDeleted) {
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

  public PlayerCharacterDetails attributes(@Nullable Object attributes) {
    this.attributes = attributes;
    return this;
  }

  /**
   * Get attributes
   * @return attributes
   */
  
  @Schema(name = "attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public @Nullable Object getAttributes() {
    return attributes;
  }

  public void setAttributes(@Nullable Object attributes) {
    this.attributes = attributes;
  }

  public PlayerCharacterDetails skills(@Nullable Object skills) {
    this.skills = skills;
    return this;
  }

  /**
   * Get skills
   * @return skills
   */
  
  @Schema(name = "skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public @Nullable Object getSkills() {
    return skills;
  }

  public void setSkills(@Nullable Object skills) {
    this.skills = skills;
  }

  public PlayerCharacterDetails reputation(@Nullable Object reputation) {
    this.reputation = reputation;
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public @Nullable Object getReputation() {
    return reputation;
  }

  public void setReputation(@Nullable Object reputation) {
    this.reputation = reputation;
  }

  public PlayerCharacterDetails position(@Nullable Object position) {
    this.position = position;
    return this;
  }

  /**
   * Get position
   * @return position
   */
  
  @Schema(name = "position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("position")
  public @Nullable Object getPosition() {
    return position;
  }

  public void setPosition(@Nullable Object position) {
    this.position = position;
  }

  public PlayerCharacterDetails appearance(@Nullable Object appearance) {
    this.appearance = appearance;
    return this;
  }

  /**
   * Get appearance
   * @return appearance
   */
  
  @Schema(name = "appearance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appearance")
  public @Nullable Object getAppearance() {
    return appearance;
  }

  public void setAppearance(@Nullable Object appearance) {
    this.appearance = appearance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerCharacterDetails playerCharacterDetails = (PlayerCharacterDetails) o;
    return Objects.equals(this.characterId, playerCharacterDetails.characterId) &&
        Objects.equals(this.playerId, playerCharacterDetails.playerId) &&
        Objects.equals(this.name, playerCharacterDetails.name) &&
        Objects.equals(this.classId, playerCharacterDetails.classId) &&
        Objects.equals(this.level, playerCharacterDetails.level) &&
        Objects.equals(this.experience, playerCharacterDetails.experience) &&
        Objects.equals(this.createdAt, playerCharacterDetails.createdAt) &&
        Objects.equals(this.lastLogin, playerCharacterDetails.lastLogin) &&
        Objects.equals(this.isDeleted, playerCharacterDetails.isDeleted) &&
        Objects.equals(this.attributes, playerCharacterDetails.attributes) &&
        Objects.equals(this.skills, playerCharacterDetails.skills) &&
        Objects.equals(this.reputation, playerCharacterDetails.reputation) &&
        Objects.equals(this.position, playerCharacterDetails.position) &&
        Objects.equals(this.appearance, playerCharacterDetails.appearance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, playerId, name, classId, level, experience, createdAt, lastLogin, isDeleted, attributes, skills, reputation, position, appearance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerCharacterDetails {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
    sb.append("    isDeleted: ").append(toIndentedString(isDeleted)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    appearance: ").append(toIndentedString(appearance)).append("\n");
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

