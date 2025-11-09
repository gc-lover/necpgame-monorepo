package com.necpgame.lootservice.model;

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
 * LootParticipant
 */


public class LootParticipant {

  private UUID playerId;

  private UUID characterId;

  private @Nullable Integer level;

  private @Nullable Float contribution;

  private @Nullable Float luckBonus;

  private @Nullable String role;

  public LootParticipant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootParticipant(UUID playerId, UUID characterId) {
    this.playerId = playerId;
    this.characterId = characterId;
  }

  public LootParticipant playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public LootParticipant characterId(UUID characterId) {
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

  public LootParticipant level(@Nullable Integer level) {
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

  public LootParticipant contribution(@Nullable Float contribution) {
    this.contribution = contribution;
    return this;
  }

  /**
   * Get contribution
   * @return contribution
   */
  
  @Schema(name = "contribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contribution")
  public @Nullable Float getContribution() {
    return contribution;
  }

  public void setContribution(@Nullable Float contribution) {
    this.contribution = contribution;
  }

  public LootParticipant luckBonus(@Nullable Float luckBonus) {
    this.luckBonus = luckBonus;
    return this;
  }

  /**
   * Get luckBonus
   * @return luckBonus
   */
  
  @Schema(name = "luckBonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("luckBonus")
  public @Nullable Float getLuckBonus() {
    return luckBonus;
  }

  public void setLuckBonus(@Nullable Float luckBonus) {
    this.luckBonus = luckBonus;
  }

  public LootParticipant role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootParticipant lootParticipant = (LootParticipant) o;
    return Objects.equals(this.playerId, lootParticipant.playerId) &&
        Objects.equals(this.characterId, lootParticipant.characterId) &&
        Objects.equals(this.level, lootParticipant.level) &&
        Objects.equals(this.contribution, lootParticipant.contribution) &&
        Objects.equals(this.luckBonus, lootParticipant.luckBonus) &&
        Objects.equals(this.role, lootParticipant.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, characterId, level, contribution, luckBonus, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootParticipant {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    contribution: ").append(toIndentedString(contribution)).append("\n");
    sb.append("    luckBonus: ").append(toIndentedString(luckBonus)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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

