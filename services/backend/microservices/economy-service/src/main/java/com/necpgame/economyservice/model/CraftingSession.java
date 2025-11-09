package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.CraftingRecipe;
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
 * CraftingSession
 */


public class CraftingSession {

  private @Nullable UUID sessionId;

  private @Nullable UUID characterId;

  private @Nullable CraftingRecipe recipe;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED"),
    
    CANCELLED("CANCELLED");

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

  private @Nullable Integer quantity;

  private @Nullable Integer completedCount;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedCompletion;

  private @Nullable Integer timeRemainingSeconds;

  private @Nullable Float successChance;

  public CraftingSession sessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @Valid 
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public @Nullable UUID getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
  }

  public CraftingSession characterId(@Nullable UUID characterId) {
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

  public CraftingSession recipe(@Nullable CraftingRecipe recipe) {
    this.recipe = recipe;
    return this;
  }

  /**
   * Get recipe
   * @return recipe
   */
  @Valid 
  @Schema(name = "recipe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipe")
  public @Nullable CraftingRecipe getRecipe() {
    return recipe;
  }

  public void setRecipe(@Nullable CraftingRecipe recipe) {
    this.recipe = recipe;
  }

  public CraftingSession status(@Nullable StatusEnum status) {
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

  public CraftingSession quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public CraftingSession completedCount(@Nullable Integer completedCount) {
    this.completedCount = completedCount;
    return this;
  }

  /**
   * Get completedCount
   * @return completedCount
   */
  
  @Schema(name = "completed_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_count")
  public @Nullable Integer getCompletedCount() {
    return completedCount;
  }

  public void setCompletedCount(@Nullable Integer completedCount) {
    this.completedCount = completedCount;
  }

  public CraftingSession startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public CraftingSession estimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
    return this;
  }

  /**
   * Get estimatedCompletion
   * @return estimatedCompletion
   */
  @Valid 
  @Schema(name = "estimated_completion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_completion")
  public @Nullable OffsetDateTime getEstimatedCompletion() {
    return estimatedCompletion;
  }

  public void setEstimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
  }

  public CraftingSession timeRemainingSeconds(@Nullable Integer timeRemainingSeconds) {
    this.timeRemainingSeconds = timeRemainingSeconds;
    return this;
  }

  /**
   * Get timeRemainingSeconds
   * @return timeRemainingSeconds
   */
  
  @Schema(name = "time_remaining_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_remaining_seconds")
  public @Nullable Integer getTimeRemainingSeconds() {
    return timeRemainingSeconds;
  }

  public void setTimeRemainingSeconds(@Nullable Integer timeRemainingSeconds) {
    this.timeRemainingSeconds = timeRemainingSeconds;
  }

  public CraftingSession successChance(@Nullable Float successChance) {
    this.successChance = successChance;
    return this;
  }

  /**
   * Get successChance
   * @return successChance
   */
  
  @Schema(name = "success_chance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success_chance")
  public @Nullable Float getSuccessChance() {
    return successChance;
  }

  public void setSuccessChance(@Nullable Float successChance) {
    this.successChance = successChance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingSession craftingSession = (CraftingSession) o;
    return Objects.equals(this.sessionId, craftingSession.sessionId) &&
        Objects.equals(this.characterId, craftingSession.characterId) &&
        Objects.equals(this.recipe, craftingSession.recipe) &&
        Objects.equals(this.status, craftingSession.status) &&
        Objects.equals(this.quantity, craftingSession.quantity) &&
        Objects.equals(this.completedCount, craftingSession.completedCount) &&
        Objects.equals(this.startedAt, craftingSession.startedAt) &&
        Objects.equals(this.estimatedCompletion, craftingSession.estimatedCompletion) &&
        Objects.equals(this.timeRemainingSeconds, craftingSession.timeRemainingSeconds) &&
        Objects.equals(this.successChance, craftingSession.successChance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, characterId, recipe, status, quantity, completedCount, startedAt, estimatedCompletion, timeRemainingSeconds, successChance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingSession {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    recipe: ").append(toIndentedString(recipe)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    completedCount: ").append(toIndentedString(completedCount)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    estimatedCompletion: ").append(toIndentedString(estimatedCompletion)).append("\n");
    sb.append("    timeRemainingSeconds: ").append(toIndentedString(timeRemainingSeconds)).append("\n");
    sb.append("    successChance: ").append(toIndentedString(successChance)).append("\n");
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

