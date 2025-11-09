package com.necpgame.socialservice.model;

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
 * RatingUpdatedEvent
 */


public class RatingUpdatedEvent {

  private UUID eventId;

  private UUID playerId;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client");

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

  private RoleEnum role;

  private Float score;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    BRONZE("bronze"),
    
    SILVER("silver"),
    
    GOLD("gold"),
    
    PLATINUM("platinum");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CategoryEnum category;

  private @Nullable Float previousScore;

  private @Nullable String previousCategory;

  private @Nullable Float delta;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime occurredAt;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    RECALCULATE("recalculate"),
    
    REVIEW("review"),
    
    PENALTY("penalty"),
    
    MANUAL("manual");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SourceEnum source;

  public RatingUpdatedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingUpdatedEvent(UUID eventId, UUID playerId, RoleEnum role, Float score, CategoryEnum category, OffsetDateTime occurredAt) {
    this.eventId = eventId;
    this.playerId = playerId;
    this.role = role;
    this.score = score;
    this.category = category;
    this.occurredAt = occurredAt;
  }

  public RatingUpdatedEvent eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull @Valid 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public RatingUpdatedEvent playerId(UUID playerId) {
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

  public RatingUpdatedEvent role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public RatingUpdatedEvent score(Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @NotNull 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public RatingUpdatedEvent category(CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(CategoryEnum category) {
    this.category = category;
  }

  public RatingUpdatedEvent previousScore(@Nullable Float previousScore) {
    this.previousScore = previousScore;
    return this;
  }

  /**
   * Get previousScore
   * @return previousScore
   */
  
  @Schema(name = "previousScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previousScore")
  public @Nullable Float getPreviousScore() {
    return previousScore;
  }

  public void setPreviousScore(@Nullable Float previousScore) {
    this.previousScore = previousScore;
  }

  public RatingUpdatedEvent previousCategory(@Nullable String previousCategory) {
    this.previousCategory = previousCategory;
    return this;
  }

  /**
   * Get previousCategory
   * @return previousCategory
   */
  
  @Schema(name = "previousCategory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previousCategory")
  public @Nullable String getPreviousCategory() {
    return previousCategory;
  }

  public void setPreviousCategory(@Nullable String previousCategory) {
    this.previousCategory = previousCategory;
  }

  public RatingUpdatedEvent delta(@Nullable Float delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delta")
  public @Nullable Float getDelta() {
    return delta;
  }

  public void setDelta(@Nullable Float delta) {
    this.delta = delta;
  }

  public RatingUpdatedEvent occurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @NotNull @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("occurredAt")
  public OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  public RatingUpdatedEvent source(@Nullable SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable SourceEnum getSource() {
    return source;
  }

  public void setSource(@Nullable SourceEnum source) {
    this.source = source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingUpdatedEvent ratingUpdatedEvent = (RatingUpdatedEvent) o;
    return Objects.equals(this.eventId, ratingUpdatedEvent.eventId) &&
        Objects.equals(this.playerId, ratingUpdatedEvent.playerId) &&
        Objects.equals(this.role, ratingUpdatedEvent.role) &&
        Objects.equals(this.score, ratingUpdatedEvent.score) &&
        Objects.equals(this.category, ratingUpdatedEvent.category) &&
        Objects.equals(this.previousScore, ratingUpdatedEvent.previousScore) &&
        Objects.equals(this.previousCategory, ratingUpdatedEvent.previousCategory) &&
        Objects.equals(this.delta, ratingUpdatedEvent.delta) &&
        Objects.equals(this.occurredAt, ratingUpdatedEvent.occurredAt) &&
        Objects.equals(this.source, ratingUpdatedEvent.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, playerId, role, score, category, previousScore, previousCategory, delta, occurredAt, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingUpdatedEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    previousScore: ").append(toIndentedString(previousScore)).append("\n");
    sb.append("    previousCategory: ").append(toIndentedString(previousCategory)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

