package com.necpgame.adminservice.model;

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
 * Appeal
 */


public class Appeal {

  private @Nullable UUID appealId;

  private @Nullable UUID banId;

  private @Nullable UUID playerId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    UNDER_REVIEW("UNDER_REVIEW"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime submittedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> resolvedAt = JsonNullable.<OffsetDateTime>undefined();

  public Appeal appealId(@Nullable UUID appealId) {
    this.appealId = appealId;
    return this;
  }

  /**
   * Get appealId
   * @return appealId
   */
  @Valid 
  @Schema(name = "appeal_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appeal_id")
  public @Nullable UUID getAppealId() {
    return appealId;
  }

  public void setAppealId(@Nullable UUID appealId) {
    this.appealId = appealId;
  }

  public Appeal banId(@Nullable UUID banId) {
    this.banId = banId;
    return this;
  }

  /**
   * Get banId
   * @return banId
   */
  @Valid 
  @Schema(name = "ban_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ban_id")
  public @Nullable UUID getBanId() {
    return banId;
  }

  public void setBanId(@Nullable UUID banId) {
    this.banId = banId;
  }

  public Appeal playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  public Appeal status(@Nullable StatusEnum status) {
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

  public Appeal submittedAt(@Nullable OffsetDateTime submittedAt) {
    this.submittedAt = submittedAt;
    return this;
  }

  /**
   * Get submittedAt
   * @return submittedAt
   */
  @Valid 
  @Schema(name = "submitted_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("submitted_at")
  public @Nullable OffsetDateTime getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(@Nullable OffsetDateTime submittedAt) {
    this.submittedAt = submittedAt;
  }

  public Appeal resolvedAt(OffsetDateTime resolvedAt) {
    this.resolvedAt = JsonNullable.of(resolvedAt);
    return this;
  }

  /**
   * Get resolvedAt
   * @return resolvedAt
   */
  @Valid 
  @Schema(name = "resolved_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolved_at")
  public JsonNullable<OffsetDateTime> getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(JsonNullable<OffsetDateTime> resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Appeal appeal = (Appeal) o;
    return Objects.equals(this.appealId, appeal.appealId) &&
        Objects.equals(this.banId, appeal.banId) &&
        Objects.equals(this.playerId, appeal.playerId) &&
        Objects.equals(this.status, appeal.status) &&
        Objects.equals(this.submittedAt, appeal.submittedAt) &&
        equalsNullable(this.resolvedAt, appeal.resolvedAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(appealId, banId, playerId, status, submittedAt, hashCodeNullable(resolvedAt));
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
    sb.append("class Appeal {\n");
    sb.append("    appealId: ").append(toIndentedString(appealId)).append("\n");
    sb.append("    banId: ").append(toIndentedString(banId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    submittedAt: ").append(toIndentedString(submittedAt)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
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

