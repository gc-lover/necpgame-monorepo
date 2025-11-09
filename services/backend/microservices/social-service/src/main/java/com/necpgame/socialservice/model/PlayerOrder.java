package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrder
 */


public class PlayerOrder {

  private @Nullable UUID orderId;

  private @Nullable UUID creatorId;

  private @Nullable String creatorName;

  private @Nullable String type;

  private @Nullable String title;

  private @Nullable String description;

  private @Nullable Integer payment;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    OPEN("OPEN"),
    
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED"),
    
    CANCELLED("CANCELLED"),
    
    EXPIRED("EXPIRED");

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

  private @Nullable String difficulty;

  private JsonNullable<UUID> executorId = JsonNullable.<UUID>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> deadline = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable Integer views;

  public PlayerOrder orderId(@Nullable UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @Valid 
  @Schema(name = "order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_id")
  public @Nullable UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable UUID orderId) {
    this.orderId = orderId;
  }

  public PlayerOrder creatorId(@Nullable UUID creatorId) {
    this.creatorId = creatorId;
    return this;
  }

  /**
   * Get creatorId
   * @return creatorId
   */
  @Valid 
  @Schema(name = "creator_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("creator_id")
  public @Nullable UUID getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(@Nullable UUID creatorId) {
    this.creatorId = creatorId;
  }

  public PlayerOrder creatorName(@Nullable String creatorName) {
    this.creatorName = creatorName;
    return this;
  }

  /**
   * Get creatorName
   * @return creatorName
   */
  
  @Schema(name = "creator_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("creator_name")
  public @Nullable String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(@Nullable String creatorName) {
    this.creatorName = creatorName;
  }

  public PlayerOrder type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public PlayerOrder title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public PlayerOrder description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public PlayerOrder payment(@Nullable Integer payment) {
    this.payment = payment;
    return this;
  }

  /**
   * Get payment
   * @return payment
   */
  
  @Schema(name = "payment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment")
  public @Nullable Integer getPayment() {
    return payment;
  }

  public void setPayment(@Nullable Integer payment) {
    this.payment = payment;
  }

  public PlayerOrder status(@Nullable StatusEnum status) {
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

  public PlayerOrder difficulty(@Nullable String difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable String difficulty) {
    this.difficulty = difficulty;
  }

  public PlayerOrder executorId(UUID executorId) {
    this.executorId = JsonNullable.of(executorId);
    return this;
  }

  /**
   * Get executorId
   * @return executorId
   */
  @Valid 
  @Schema(name = "executor_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executor_id")
  public JsonNullable<UUID> getExecutorId() {
    return executorId;
  }

  public void setExecutorId(JsonNullable<UUID> executorId) {
    this.executorId = executorId;
  }

  public PlayerOrder createdAt(@Nullable OffsetDateTime createdAt) {
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

  public PlayerOrder deadline(OffsetDateTime deadline) {
    this.deadline = JsonNullable.of(deadline);
    return this;
  }

  /**
   * Get deadline
   * @return deadline
   */
  @Valid 
  @Schema(name = "deadline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deadline")
  public JsonNullable<OffsetDateTime> getDeadline() {
    return deadline;
  }

  public void setDeadline(JsonNullable<OffsetDateTime> deadline) {
    this.deadline = deadline;
  }

  public PlayerOrder views(@Nullable Integer views) {
    this.views = views;
    return this;
  }

  /**
   * Get views
   * @return views
   */
  
  @Schema(name = "views", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("views")
  public @Nullable Integer getViews() {
    return views;
  }

  public void setViews(@Nullable Integer views) {
    this.views = views;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrder playerOrder = (PlayerOrder) o;
    return Objects.equals(this.orderId, playerOrder.orderId) &&
        Objects.equals(this.creatorId, playerOrder.creatorId) &&
        Objects.equals(this.creatorName, playerOrder.creatorName) &&
        Objects.equals(this.type, playerOrder.type) &&
        Objects.equals(this.title, playerOrder.title) &&
        Objects.equals(this.description, playerOrder.description) &&
        Objects.equals(this.payment, playerOrder.payment) &&
        Objects.equals(this.status, playerOrder.status) &&
        Objects.equals(this.difficulty, playerOrder.difficulty) &&
        equalsNullable(this.executorId, playerOrder.executorId) &&
        Objects.equals(this.createdAt, playerOrder.createdAt) &&
        equalsNullable(this.deadline, playerOrder.deadline) &&
        Objects.equals(this.views, playerOrder.views);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, creatorId, creatorName, type, title, description, payment, status, difficulty, hashCodeNullable(executorId), createdAt, hashCodeNullable(deadline), views);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrder {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    creatorId: ").append(toIndentedString(creatorId)).append("\n");
    sb.append("    creatorName: ").append(toIndentedString(creatorName)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    payment: ").append(toIndentedString(payment)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    executorId: ").append(toIndentedString(executorId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
    sb.append("    views: ").append(toIndentedString(views)).append("\n");
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

