package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ActionXpEvent
 */


public class ActionXpEvent {

  private UUID characterId;

  private String skillId;

  private BigDecimal xp;

  private BigDecimal fatigueScore;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private @Nullable UUID sourceEventId;

  private @Nullable BigDecimal multiplier;

  public ActionXpEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionXpEvent(UUID characterId, String skillId, BigDecimal xp, BigDecimal fatigueScore, OffsetDateTime timestamp) {
    this.characterId = characterId;
    this.skillId = skillId;
    this.xp = xp;
    this.fatigueScore = fatigueScore;
    this.timestamp = timestamp;
  }

  public ActionXpEvent characterId(UUID characterId) {
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

  public ActionXpEvent skillId(String skillId) {
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

  public ActionXpEvent xp(BigDecimal xp) {
    this.xp = xp;
    return this;
  }

  /**
   * Get xp
   * @return xp
   */
  @NotNull @Valid 
  @Schema(name = "xp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("xp")
  public BigDecimal getXp() {
    return xp;
  }

  public void setXp(BigDecimal xp) {
    this.xp = xp;
  }

  public ActionXpEvent fatigueScore(BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
    return this;
  }

  /**
   * Get fatigueScore
   * @return fatigueScore
   */
  @NotNull @Valid 
  @Schema(name = "fatigueScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fatigueScore")
  public BigDecimal getFatigueScore() {
    return fatigueScore;
  }

  public void setFatigueScore(BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
  }

  public ActionXpEvent timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ActionXpEvent sourceEventId(@Nullable UUID sourceEventId) {
    this.sourceEventId = sourceEventId;
    return this;
  }

  /**
   * Get sourceEventId
   * @return sourceEventId
   */
  @Valid 
  @Schema(name = "sourceEventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceEventId")
  public @Nullable UUID getSourceEventId() {
    return sourceEventId;
  }

  public void setSourceEventId(@Nullable UUID sourceEventId) {
    this.sourceEventId = sourceEventId;
  }

  public ActionXpEvent multiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  /**
   * Get multiplier
   * @return multiplier
   */
  @Valid 
  @Schema(name = "multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multiplier")
  public @Nullable BigDecimal getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpEvent actionXpEvent = (ActionXpEvent) o;
    return Objects.equals(this.characterId, actionXpEvent.characterId) &&
        Objects.equals(this.skillId, actionXpEvent.skillId) &&
        Objects.equals(this.xp, actionXpEvent.xp) &&
        Objects.equals(this.fatigueScore, actionXpEvent.fatigueScore) &&
        Objects.equals(this.timestamp, actionXpEvent.timestamp) &&
        Objects.equals(this.sourceEventId, actionXpEvent.sourceEventId) &&
        Objects.equals(this.multiplier, actionXpEvent.multiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skillId, xp, fatigueScore, timestamp, sourceEventId, multiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpEvent {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    xp: ").append(toIndentedString(xp)).append("\n");
    sb.append("    fatigueScore: ").append(toIndentedString(fatigueScore)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    sourceEventId: ").append(toIndentedString(sourceEventId)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
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

