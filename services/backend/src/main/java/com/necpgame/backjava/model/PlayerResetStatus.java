package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.PlayerResetItems;
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
 * PlayerResetStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerResetStatus {

  private @Nullable UUID playerId;

  private @Nullable PlayerResetItems daily;

  private @Nullable PlayerResetItems weekly;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastDailyReset;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastWeeklyReset;

  public PlayerResetStatus playerId(@Nullable UUID playerId) {
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

  public PlayerResetStatus daily(@Nullable PlayerResetItems daily) {
    this.daily = daily;
    return this;
  }

  /**
   * Get daily
   * @return daily
   */
  @Valid 
  @Schema(name = "daily", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daily")
  public @Nullable PlayerResetItems getDaily() {
    return daily;
  }

  public void setDaily(@Nullable PlayerResetItems daily) {
    this.daily = daily;
  }

  public PlayerResetStatus weekly(@Nullable PlayerResetItems weekly) {
    this.weekly = weekly;
    return this;
  }

  /**
   * Get weekly
   * @return weekly
   */
  @Valid 
  @Schema(name = "weekly", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weekly")
  public @Nullable PlayerResetItems getWeekly() {
    return weekly;
  }

  public void setWeekly(@Nullable PlayerResetItems weekly) {
    this.weekly = weekly;
  }

  public PlayerResetStatus lastDailyReset(@Nullable OffsetDateTime lastDailyReset) {
    this.lastDailyReset = lastDailyReset;
    return this;
  }

  /**
   * Get lastDailyReset
   * @return lastDailyReset
   */
  @Valid 
  @Schema(name = "last_daily_reset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_daily_reset")
  public @Nullable OffsetDateTime getLastDailyReset() {
    return lastDailyReset;
  }

  public void setLastDailyReset(@Nullable OffsetDateTime lastDailyReset) {
    this.lastDailyReset = lastDailyReset;
  }

  public PlayerResetStatus lastWeeklyReset(@Nullable OffsetDateTime lastWeeklyReset) {
    this.lastWeeklyReset = lastWeeklyReset;
    return this;
  }

  /**
   * Get lastWeeklyReset
   * @return lastWeeklyReset
   */
  @Valid 
  @Schema(name = "last_weekly_reset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_weekly_reset")
  public @Nullable OffsetDateTime getLastWeeklyReset() {
    return lastWeeklyReset;
  }

  public void setLastWeeklyReset(@Nullable OffsetDateTime lastWeeklyReset) {
    this.lastWeeklyReset = lastWeeklyReset;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerResetStatus playerResetStatus = (PlayerResetStatus) o;
    return Objects.equals(this.playerId, playerResetStatus.playerId) &&
        Objects.equals(this.daily, playerResetStatus.daily) &&
        Objects.equals(this.weekly, playerResetStatus.weekly) &&
        Objects.equals(this.lastDailyReset, playerResetStatus.lastDailyReset) &&
        Objects.equals(this.lastWeeklyReset, playerResetStatus.lastWeeklyReset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, daily, weekly, lastDailyReset, lastWeeklyReset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerResetStatus {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    daily: ").append(toIndentedString(daily)).append("\n");
    sb.append("    weekly: ").append(toIndentedString(weekly)).append("\n");
    sb.append("    lastDailyReset: ").append(toIndentedString(lastDailyReset)).append("\n");
    sb.append("    lastWeeklyReset: ").append(toIndentedString(lastWeeklyReset)).append("\n");
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

