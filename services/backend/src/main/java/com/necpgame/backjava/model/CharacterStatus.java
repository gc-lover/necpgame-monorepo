package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CharacterStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:21:38.856812200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CharacterStatus {

  private UUID characterId;

  private Integer health;

  private Integer maxHealth;

  private Integer energy;

  private Integer maxEnergy;

  private Integer humanity;

  private Integer maxHumanity = 100;

  private Integer level;

  private Integer experience;

  private @Nullable Integer nextLevelExperience;

  public CharacterStatus() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterStatus(UUID characterId, Integer health, Integer maxHealth, Integer energy, Integer maxEnergy, Integer humanity, Integer level, Integer experience) {
    this.characterId = characterId;
    this.health = health;
    this.maxHealth = maxHealth;
    this.energy = energy;
    this.maxEnergy = maxEnergy;
    this.humanity = humanity;
    this.level = level;
    this.experience = experience;
  }

  public CharacterStatus characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CharacterStatus health(Integer health) {
    this.health = health;
    return this;
  }

  /**
   * Get health
   * @return health
   */
  @NotNull 
  @Schema(name = "health", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("health")
  public Integer getHealth() {
    return health;
  }

  public void setHealth(Integer health) {
    this.health = health;
  }

  public CharacterStatus maxHealth(Integer maxHealth) {
    this.maxHealth = maxHealth;
    return this;
  }

  /**
   * Get maxHealth
   * @return maxHealth
   */
  @NotNull 
  @Schema(name = "maxHealth", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxHealth")
  public Integer getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(Integer maxHealth) {
    this.maxHealth = maxHealth;
  }

  public CharacterStatus energy(Integer energy) {
    this.energy = energy;
    return this;
  }

  /**
   * Get energy
   * @return energy
   */
  @NotNull 
  @Schema(name = "energy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("energy")
  public Integer getEnergy() {
    return energy;
  }

  public void setEnergy(Integer energy) {
    this.energy = energy;
  }

  public CharacterStatus maxEnergy(Integer maxEnergy) {
    this.maxEnergy = maxEnergy;
    return this;
  }

  /**
   * Get maxEnergy
   * @return maxEnergy
   */
  @NotNull 
  @Schema(name = "maxEnergy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxEnergy")
  public Integer getMaxEnergy() {
    return maxEnergy;
  }

  public void setMaxEnergy(Integer maxEnergy) {
    this.maxEnergy = maxEnergy;
  }

  public CharacterStatus humanity(Integer humanity) {
    this.humanity = humanity;
    return this;
  }

  /**
   * Get humanity
   * @return humanity
   */
  @NotNull 
  @Schema(name = "humanity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("humanity")
  public Integer getHumanity() {
    return humanity;
  }

  public void setHumanity(Integer humanity) {
    this.humanity = humanity;
  }

  public CharacterStatus maxHumanity(Integer maxHumanity) {
    this.maxHumanity = maxHumanity;
    return this;
  }

  /**
   * Get maxHumanity
   * @return maxHumanity
   */
  
  @Schema(name = "maxHumanity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxHumanity")
  public Integer getMaxHumanity() {
    return maxHumanity;
  }

  public void setMaxHumanity(Integer maxHumanity) {
    this.maxHumanity = maxHumanity;
  }

  public CharacterStatus level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  @NotNull 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public CharacterStatus experience(Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  @NotNull 
  @Schema(name = "experience", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("experience")
  public Integer getExperience() {
    return experience;
  }

  public void setExperience(Integer experience) {
    this.experience = experience;
  }

  public CharacterStatus nextLevelExperience(@Nullable Integer nextLevelExperience) {
    this.nextLevelExperience = nextLevelExperience;
    return this;
  }

  /**
   * Get nextLevelExperience
   * @return nextLevelExperience
   */
  
  @Schema(name = "nextLevelExperience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextLevelExperience")
  public @Nullable Integer getNextLevelExperience() {
    return nextLevelExperience;
  }

  public void setNextLevelExperience(@Nullable Integer nextLevelExperience) {
    this.nextLevelExperience = nextLevelExperience;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterStatus characterStatus = (CharacterStatus) o;
    return Objects.equals(this.characterId, characterStatus.characterId) &&
        Objects.equals(this.health, characterStatus.health) &&
        Objects.equals(this.maxHealth, characterStatus.maxHealth) &&
        Objects.equals(this.energy, characterStatus.energy) &&
        Objects.equals(this.maxEnergy, characterStatus.maxEnergy) &&
        Objects.equals(this.humanity, characterStatus.humanity) &&
        Objects.equals(this.maxHumanity, characterStatus.maxHumanity) &&
        Objects.equals(this.level, characterStatus.level) &&
        Objects.equals(this.experience, characterStatus.experience) &&
        Objects.equals(this.nextLevelExperience, characterStatus.nextLevelExperience);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, health, maxHealth, energy, maxEnergy, humanity, maxHumanity, level, experience, nextLevelExperience);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterStatus {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
    sb.append("    maxHealth: ").append(toIndentedString(maxHealth)).append("\n");
    sb.append("    energy: ").append(toIndentedString(energy)).append("\n");
    sb.append("    maxEnergy: ").append(toIndentedString(maxEnergy)).append("\n");
    sb.append("    humanity: ").append(toIndentedString(humanity)).append("\n");
    sb.append("    maxHumanity: ").append(toIndentedString(maxHumanity)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    nextLevelExperience: ").append(toIndentedString(nextLevelExperience)).append("\n");
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

