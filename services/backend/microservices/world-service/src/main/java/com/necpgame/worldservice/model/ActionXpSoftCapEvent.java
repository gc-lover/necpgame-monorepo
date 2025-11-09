package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ActionXpSoftCapEvent
 */


public class ActionXpSoftCapEvent {

  private UUID characterId;

  private String skillId;

  private BigDecimal dayTotal;

  private BigDecimal softCap;

  private @Nullable BigDecimal fatigueModifier;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  @Valid
  private List<String> recommendations = new ArrayList<>();

  public ActionXpSoftCapEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionXpSoftCapEvent(UUID characterId, String skillId, BigDecimal dayTotal, BigDecimal softCap, OffsetDateTime timestamp) {
    this.characterId = characterId;
    this.skillId = skillId;
    this.dayTotal = dayTotal;
    this.softCap = softCap;
    this.timestamp = timestamp;
  }

  public ActionXpSoftCapEvent characterId(UUID characterId) {
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

  public ActionXpSoftCapEvent skillId(String skillId) {
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

  public ActionXpSoftCapEvent dayTotal(BigDecimal dayTotal) {
    this.dayTotal = dayTotal;
    return this;
  }

  /**
   * Get dayTotal
   * @return dayTotal
   */
  @NotNull @Valid 
  @Schema(name = "dayTotal", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dayTotal")
  public BigDecimal getDayTotal() {
    return dayTotal;
  }

  public void setDayTotal(BigDecimal dayTotal) {
    this.dayTotal = dayTotal;
  }

  public ActionXpSoftCapEvent softCap(BigDecimal softCap) {
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

  public ActionXpSoftCapEvent fatigueModifier(@Nullable BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
    return this;
  }

  /**
   * Get fatigueModifier
   * @return fatigueModifier
   */
  @Valid 
  @Schema(name = "fatigueModifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueModifier")
  public @Nullable BigDecimal getFatigueModifier() {
    return fatigueModifier;
  }

  public void setFatigueModifier(@Nullable BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
  }

  public ActionXpSoftCapEvent timestamp(OffsetDateTime timestamp) {
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

  public ActionXpSoftCapEvent recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public ActionXpSoftCapEvent addRecommendationsItem(String recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Get recommendations
   * @return recommendations
   */
  
  @Schema(name = "recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
    this.recommendations = recommendations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpSoftCapEvent actionXpSoftCapEvent = (ActionXpSoftCapEvent) o;
    return Objects.equals(this.characterId, actionXpSoftCapEvent.characterId) &&
        Objects.equals(this.skillId, actionXpSoftCapEvent.skillId) &&
        Objects.equals(this.dayTotal, actionXpSoftCapEvent.dayTotal) &&
        Objects.equals(this.softCap, actionXpSoftCapEvent.softCap) &&
        Objects.equals(this.fatigueModifier, actionXpSoftCapEvent.fatigueModifier) &&
        Objects.equals(this.timestamp, actionXpSoftCapEvent.timestamp) &&
        Objects.equals(this.recommendations, actionXpSoftCapEvent.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skillId, dayTotal, softCap, fatigueModifier, timestamp, recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpSoftCapEvent {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    dayTotal: ").append(toIndentedString(dayTotal)).append("\n");
    sb.append("    softCap: ").append(toIndentedString(softCap)).append("\n");
    sb.append("    fatigueModifier: ").append(toIndentedString(fatigueModifier)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
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

