package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * SkillFatigue
 */


public class SkillFatigue {

  private UUID characterId;

  private String skillId;

  private BigDecimal dailyXpTotal;

  private BigDecimal fatigueModifier;

  private @Nullable BigDecimal fatigueScore;

  private BigDecimal softCap;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime resetAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  /**
   * Gets or Sets fatigueState
   */
  public enum FatigueStateEnum {
    NORMAL("normal"),
    
    APPROACHING_CAP("approaching_cap"),
    
    SOFT_CAP("soft_cap"),
    
    EXHAUSTED("exhausted");

    private final String value;

    FatigueStateEnum(String value) {
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
    public static FatigueStateEnum fromValue(String value) {
      for (FatigueStateEnum b : FatigueStateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable FatigueStateEnum fatigueState;

  public SkillFatigue() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SkillFatigue(UUID characterId, String skillId, BigDecimal dailyXpTotal, BigDecimal fatigueModifier, BigDecimal softCap, OffsetDateTime resetAt) {
    this.characterId = characterId;
    this.skillId = skillId;
    this.dailyXpTotal = dailyXpTotal;
    this.fatigueModifier = fatigueModifier;
    this.softCap = softCap;
    this.resetAt = resetAt;
  }

  public SkillFatigue characterId(UUID characterId) {
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

  public SkillFatigue skillId(String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  @NotNull 
  @Schema(name = "skillId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skillId")
  public String getSkillId() {
    return skillId;
  }

  public void setSkillId(String skillId) {
    this.skillId = skillId;
  }

  public SkillFatigue dailyXpTotal(BigDecimal dailyXpTotal) {
    this.dailyXpTotal = dailyXpTotal;
    return this;
  }

  /**
   * Get dailyXpTotal
   * @return dailyXpTotal
   */
  @NotNull @Valid 
  @Schema(name = "dailyXpTotal", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dailyXpTotal")
  public BigDecimal getDailyXpTotal() {
    return dailyXpTotal;
  }

  public void setDailyXpTotal(BigDecimal dailyXpTotal) {
    this.dailyXpTotal = dailyXpTotal;
  }

  public SkillFatigue fatigueModifier(BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
    return this;
  }

  /**
   * Get fatigueModifier
   * @return fatigueModifier
   */
  @NotNull @Valid 
  @Schema(name = "fatigueModifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fatigueModifier")
  public BigDecimal getFatigueModifier() {
    return fatigueModifier;
  }

  public void setFatigueModifier(BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
  }

  public SkillFatigue fatigueScore(@Nullable BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
    return this;
  }

  /**
   * Get fatigueScore
   * @return fatigueScore
   */
  @Valid 
  @Schema(name = "fatigueScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueScore")
  public @Nullable BigDecimal getFatigueScore() {
    return fatigueScore;
  }

  public void setFatigueScore(@Nullable BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
  }

  public SkillFatigue softCap(BigDecimal softCap) {
    this.softCap = softCap;
    return this;
  }

  /**
   * Get softCap
   * @return softCap
   */
  @NotNull @Valid 
  @Schema(name = "softCap", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("softCap")
  public BigDecimal getSoftCap() {
    return softCap;
  }

  public void setSoftCap(BigDecimal softCap) {
    this.softCap = softCap;
  }

  public SkillFatigue resetAt(OffsetDateTime resetAt) {
    this.resetAt = resetAt;
    return this;
  }

  /**
   * Get resetAt
   * @return resetAt
   */
  @NotNull @Valid 
  @Schema(name = "resetAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resetAt")
  public OffsetDateTime getResetAt() {
    return resetAt;
  }

  public void setResetAt(OffsetDateTime resetAt) {
    this.resetAt = resetAt;
  }

  public SkillFatigue lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "lastUpdated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdated")
  public @Nullable OffsetDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public SkillFatigue fatigueState(@Nullable FatigueStateEnum fatigueState) {
    this.fatigueState = fatigueState;
    return this;
  }

  /**
   * Get fatigueState
   * @return fatigueState
   */
  
  @Schema(name = "fatigueState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueState")
  public @Nullable FatigueStateEnum getFatigueState() {
    return fatigueState;
  }

  public void setFatigueState(@Nullable FatigueStateEnum fatigueState) {
    this.fatigueState = fatigueState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillFatigue skillFatigue = (SkillFatigue) o;
    return Objects.equals(this.characterId, skillFatigue.characterId) &&
        Objects.equals(this.skillId, skillFatigue.skillId) &&
        Objects.equals(this.dailyXpTotal, skillFatigue.dailyXpTotal) &&
        Objects.equals(this.fatigueModifier, skillFatigue.fatigueModifier) &&
        Objects.equals(this.fatigueScore, skillFatigue.fatigueScore) &&
        Objects.equals(this.softCap, skillFatigue.softCap) &&
        Objects.equals(this.resetAt, skillFatigue.resetAt) &&
        Objects.equals(this.lastUpdated, skillFatigue.lastUpdated) &&
        Objects.equals(this.fatigueState, skillFatigue.fatigueState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skillId, dailyXpTotal, fatigueModifier, fatigueScore, softCap, resetAt, lastUpdated, fatigueState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillFatigue {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    dailyXpTotal: ").append(toIndentedString(dailyXpTotal)).append("\n");
    sb.append("    fatigueModifier: ").append(toIndentedString(fatigueModifier)).append("\n");
    sb.append("    fatigueScore: ").append(toIndentedString(fatigueScore)).append("\n");
    sb.append("    softCap: ").append(toIndentedString(softCap)).append("\n");
    sb.append("    resetAt: ").append(toIndentedString(resetAt)).append("\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
    sb.append("    fatigueState: ").append(toIndentedString(fatigueState)).append("\n");
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

