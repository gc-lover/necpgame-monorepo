package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ResetTypeStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ResetTypeStatus {

  /**
   * Gets or Sets resetType
   */
  public enum ResetTypeEnum {
    DAILY("DAILY"),
    
    WEEKLY("WEEKLY"),
    
    MONTHLY("MONTHLY");

    private final String value;

    ResetTypeEnum(String value) {
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
    public static ResetTypeEnum fromValue(String value) {
      for (ResetTypeEnum b : ResetTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ResetTypeEnum resetType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastReset;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime nextReset;

  private @Nullable String timeUntilReset;

  private @Nullable Integer timeUntilResetSeconds;

  @Valid
  private List<String> resetItems = new ArrayList<>();

  public ResetTypeStatus resetType(@Nullable ResetTypeEnum resetType) {
    this.resetType = resetType;
    return this;
  }

  /**
   * Get resetType
   * @return resetType
   */
  
  @Schema(name = "reset_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reset_type")
  public @Nullable ResetTypeEnum getResetType() {
    return resetType;
  }

  public void setResetType(@Nullable ResetTypeEnum resetType) {
    this.resetType = resetType;
  }

  public ResetTypeStatus lastReset(@Nullable OffsetDateTime lastReset) {
    this.lastReset = lastReset;
    return this;
  }

  /**
   * Get lastReset
   * @return lastReset
   */
  @Valid 
  @Schema(name = "last_reset", example = "2077-06-01T00:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_reset")
  public @Nullable OffsetDateTime getLastReset() {
    return lastReset;
  }

  public void setLastReset(@Nullable OffsetDateTime lastReset) {
    this.lastReset = lastReset;
  }

  public ResetTypeStatus nextReset(@Nullable OffsetDateTime nextReset) {
    this.nextReset = nextReset;
    return this;
  }

  /**
   * Get nextReset
   * @return nextReset
   */
  @Valid 
  @Schema(name = "next_reset", example = "2077-06-02T00:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_reset")
  public @Nullable OffsetDateTime getNextReset() {
    return nextReset;
  }

  public void setNextReset(@Nullable OffsetDateTime nextReset) {
    this.nextReset = nextReset;
  }

  public ResetTypeStatus timeUntilReset(@Nullable String timeUntilReset) {
    this.timeUntilReset = timeUntilReset;
    return this;
  }

  /**
   * Человекочитаемое время до сброса
   * @return timeUntilReset
   */
  
  @Schema(name = "time_until_reset", example = "5 hours 30 minutes", description = "Человекочитаемое время до сброса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_until_reset")
  public @Nullable String getTimeUntilReset() {
    return timeUntilReset;
  }

  public void setTimeUntilReset(@Nullable String timeUntilReset) {
    this.timeUntilReset = timeUntilReset;
  }

  public ResetTypeStatus timeUntilResetSeconds(@Nullable Integer timeUntilResetSeconds) {
    this.timeUntilResetSeconds = timeUntilResetSeconds;
    return this;
  }

  /**
   * Секунды до сброса
   * @return timeUntilResetSeconds
   */
  
  @Schema(name = "time_until_reset_seconds", example = "19800", description = "Секунды до сброса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_until_reset_seconds")
  public @Nullable Integer getTimeUntilResetSeconds() {
    return timeUntilResetSeconds;
  }

  public void setTimeUntilResetSeconds(@Nullable Integer timeUntilResetSeconds) {
    this.timeUntilResetSeconds = timeUntilResetSeconds;
  }

  public ResetTypeStatus resetItems(List<String> resetItems) {
    this.resetItems = resetItems;
    return this;
  }

  public ResetTypeStatus addResetItemsItem(String resetItemsItem) {
    if (this.resetItems == null) {
      this.resetItems = new ArrayList<>();
    }
    this.resetItems.add(resetItemsItem);
    return this;
  }

  /**
   * Что сбрасывается
   * @return resetItems
   */
  
  @Schema(name = "reset_items", example = "[\"daily_quests\",\"daily_limits\",\"vendor_inventory\"]", description = "Что сбрасывается", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reset_items")
  public List<String> getResetItems() {
    return resetItems;
  }

  public void setResetItems(List<String> resetItems) {
    this.resetItems = resetItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetTypeStatus resetTypeStatus = (ResetTypeStatus) o;
    return Objects.equals(this.resetType, resetTypeStatus.resetType) &&
        Objects.equals(this.lastReset, resetTypeStatus.lastReset) &&
        Objects.equals(this.nextReset, resetTypeStatus.nextReset) &&
        Objects.equals(this.timeUntilReset, resetTypeStatus.timeUntilReset) &&
        Objects.equals(this.timeUntilResetSeconds, resetTypeStatus.timeUntilResetSeconds) &&
        Objects.equals(this.resetItems, resetTypeStatus.resetItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resetType, lastReset, nextReset, timeUntilReset, timeUntilResetSeconds, resetItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetTypeStatus {\n");
    sb.append("    resetType: ").append(toIndentedString(resetType)).append("\n");
    sb.append("    lastReset: ").append(toIndentedString(lastReset)).append("\n");
    sb.append("    nextReset: ").append(toIndentedString(nextReset)).append("\n");
    sb.append("    timeUntilReset: ").append(toIndentedString(timeUntilReset)).append("\n");
    sb.append("    timeUntilResetSeconds: ").append(toIndentedString(timeUntilResetSeconds)).append("\n");
    sb.append("    resetItems: ").append(toIndentedString(resetItems)).append("\n");
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

