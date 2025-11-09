package com.necpgame.economyservice.model;

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
 * GuildMember
 */


public class GuildMember {

  private @Nullable UUID characterId;

  private @Nullable String characterName;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    GUILD_MASTER("GUILD_MASTER"),
    
    TREASURER("TREASURER"),
    
    MERCHANT("MERCHANT"),
    
    TRADER("TRADER"),
    
    RECRUIT("RECRUIT");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  private @Nullable Integer contributionTotal;

  private @Nullable Integer tradesCompleted;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime joinedAt;

  public GuildMember characterId(@Nullable UUID characterId) {
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

  public GuildMember characterName(@Nullable String characterName) {
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

  public GuildMember role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  public GuildMember contributionTotal(@Nullable Integer contributionTotal) {
    this.contributionTotal = contributionTotal;
    return this;
  }

  /**
   * Общий вклад в казну
   * @return contributionTotal
   */
  
  @Schema(name = "contribution_total", description = "Общий вклад в казну", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contribution_total")
  public @Nullable Integer getContributionTotal() {
    return contributionTotal;
  }

  public void setContributionTotal(@Nullable Integer contributionTotal) {
    this.contributionTotal = contributionTotal;
  }

  public GuildMember tradesCompleted(@Nullable Integer tradesCompleted) {
    this.tradesCompleted = tradesCompleted;
    return this;
  }

  /**
   * Get tradesCompleted
   * @return tradesCompleted
   */
  
  @Schema(name = "trades_completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trades_completed")
  public @Nullable Integer getTradesCompleted() {
    return tradesCompleted;
  }

  public void setTradesCompleted(@Nullable Integer tradesCompleted) {
    this.tradesCompleted = tradesCompleted;
  }

  public GuildMember joinedAt(@Nullable OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
    return this;
  }

  /**
   * Get joinedAt
   * @return joinedAt
   */
  @Valid 
  @Schema(name = "joined_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("joined_at")
  public @Nullable OffsetDateTime getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(@Nullable OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildMember guildMember = (GuildMember) o;
    return Objects.equals(this.characterId, guildMember.characterId) &&
        Objects.equals(this.characterName, guildMember.characterName) &&
        Objects.equals(this.role, guildMember.role) &&
        Objects.equals(this.contributionTotal, guildMember.contributionTotal) &&
        Objects.equals(this.tradesCompleted, guildMember.tradesCompleted) &&
        Objects.equals(this.joinedAt, guildMember.joinedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, characterName, role, contributionTotal, tradesCompleted, joinedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildMember {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    characterName: ").append(toIndentedString(characterName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    contributionTotal: ").append(toIndentedString(contributionTotal)).append("\n");
    sb.append("    tradesCompleted: ").append(toIndentedString(tradesCompleted)).append("\n");
    sb.append("    joinedAt: ").append(toIndentedString(joinedAt)).append("\n");
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

