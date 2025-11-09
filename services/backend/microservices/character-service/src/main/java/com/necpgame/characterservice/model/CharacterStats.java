package com.necpgame.characterservice.model;

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
 * CharacterStats
 */


public class CharacterStats {

  private UUID characterId;

  private Integer strength;

  private Integer reflexes;

  private Integer intelligence;

  private Integer technical;

  private Integer cool;

  public CharacterStats() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterStats(UUID characterId, Integer strength, Integer reflexes, Integer intelligence, Integer technical, Integer cool) {
    this.characterId = characterId;
    this.strength = strength;
    this.reflexes = reflexes;
    this.intelligence = intelligence;
    this.technical = technical;
    this.cool = cool;
  }

  public CharacterStats characterId(UUID characterId) {
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

  public CharacterStats strength(Integer strength) {
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

  public CharacterStats reflexes(Integer reflexes) {
    this.reflexes = reflexes;
    return this;
  }

  /**
   * Get reflexes
   * @return reflexes
   */
  @NotNull 
  @Schema(name = "reflexes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reflexes")
  public Integer getReflexes() {
    return reflexes;
  }

  public void setReflexes(Integer reflexes) {
    this.reflexes = reflexes;
  }

  public CharacterStats intelligence(Integer intelligence) {
    this.intelligence = intelligence;
    return this;
  }

  /**
   * Get intelligence
   * @return intelligence
   */
  @NotNull 
  @Schema(name = "intelligence", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("intelligence")
  public Integer getIntelligence() {
    return intelligence;
  }

  public void setIntelligence(Integer intelligence) {
    this.intelligence = intelligence;
  }

  public CharacterStats technical(Integer technical) {
    this.technical = technical;
    return this;
  }

  /**
   * Get technical
   * @return technical
   */
  @NotNull 
  @Schema(name = "technical", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("technical")
  public Integer getTechnical() {
    return technical;
  }

  public void setTechnical(Integer technical) {
    this.technical = technical;
  }

  public CharacterStats cool(Integer cool) {
    this.cool = cool;
    return this;
  }

  /**
   * Get cool
   * @return cool
   */
  @NotNull 
  @Schema(name = "cool", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cool")
  public Integer getCool() {
    return cool;
  }

  public void setCool(Integer cool) {
    this.cool = cool;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterStats characterStats = (CharacterStats) o;
    return Objects.equals(this.characterId, characterStats.characterId) &&
        Objects.equals(this.strength, characterStats.strength) &&
        Objects.equals(this.reflexes, characterStats.reflexes) &&
        Objects.equals(this.intelligence, characterStats.intelligence) &&
        Objects.equals(this.technical, characterStats.technical) &&
        Objects.equals(this.cool, characterStats.cool);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, strength, reflexes, intelligence, technical, cool);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterStats {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    strength: ").append(toIndentedString(strength)).append("\n");
    sb.append("    reflexes: ").append(toIndentedString(reflexes)).append("\n");
    sb.append("    intelligence: ").append(toIndentedString(intelligence)).append("\n");
    sb.append("    technical: ").append(toIndentedString(technical)).append("\n");
    sb.append("    cool: ").append(toIndentedString(cool)).append("\n");
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

