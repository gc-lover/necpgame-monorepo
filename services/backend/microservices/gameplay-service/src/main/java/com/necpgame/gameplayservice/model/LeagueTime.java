package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
 * LeagueTime
 */


public class LeagueTime {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime gameTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime realTime;

  private @Nullable BigDecimal timeAcceleration;

  private @Nullable Integer daysPassedGame;

  private @Nullable Integer daysRemainingGame;

  private @Nullable String era;

  public LeagueTime gameTime(@Nullable OffsetDateTime gameTime) {
    this.gameTime = gameTime;
    return this;
  }

  /**
   * Текущее игровое время (2020-2093)
   * @return gameTime
   */
  @Valid 
  @Schema(name = "game_time", description = "Текущее игровое время (2020-2093)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("game_time")
  public @Nullable OffsetDateTime getGameTime() {
    return gameTime;
  }

  public void setGameTime(@Nullable OffsetDateTime gameTime) {
    this.gameTime = gameTime;
  }

  public LeagueTime realTime(@Nullable OffsetDateTime realTime) {
    this.realTime = realTime;
    return this;
  }

  /**
   * Текущее реальное время
   * @return realTime
   */
  @Valid 
  @Schema(name = "real_time", description = "Текущее реальное время", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("real_time")
  public @Nullable OffsetDateTime getRealTime() {
    return realTime;
  }

  public void setRealTime(@Nullable OffsetDateTime realTime) {
    this.realTime = realTime;
  }

  public LeagueTime timeAcceleration(@Nullable BigDecimal timeAcceleration) {
    this.timeAcceleration = timeAcceleration;
    return this;
  }

  /**
   * Ускорение времени
   * @return timeAcceleration
   */
  @Valid 
  @Schema(name = "time_acceleration", description = "Ускорение времени", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_acceleration")
  public @Nullable BigDecimal getTimeAcceleration() {
    return timeAcceleration;
  }

  public void setTimeAcceleration(@Nullable BigDecimal timeAcceleration) {
    this.timeAcceleration = timeAcceleration;
  }

  public LeagueTime daysPassedGame(@Nullable Integer daysPassedGame) {
    this.daysPassedGame = daysPassedGame;
    return this;
  }

  /**
   * Прошло игровых дней с начала лиги
   * @return daysPassedGame
   */
  
  @Schema(name = "days_passed_game", description = "Прошло игровых дней с начала лиги", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("days_passed_game")
  public @Nullable Integer getDaysPassedGame() {
    return daysPassedGame;
  }

  public void setDaysPassedGame(@Nullable Integer daysPassedGame) {
    this.daysPassedGame = daysPassedGame;
  }

  public LeagueTime daysRemainingGame(@Nullable Integer daysRemainingGame) {
    this.daysRemainingGame = daysRemainingGame;
    return this;
  }

  /**
   * Осталось игровых дней до конца лиги
   * @return daysRemainingGame
   */
  
  @Schema(name = "days_remaining_game", description = "Осталось игровых дней до конца лиги", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("days_remaining_game")
  public @Nullable Integer getDaysRemainingGame() {
    return daysRemainingGame;
  }

  public void setDaysRemainingGame(@Nullable Integer daysRemainingGame) {
    this.daysRemainingGame = daysRemainingGame;
  }

  public LeagueTime era(@Nullable String era) {
    this.era = era;
    return this;
  }

  /**
   * Текущая эра
   * @return era
   */
  
  @Schema(name = "era", example = "2045-2060", description = "Текущая эра", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era")
  public @Nullable String getEra() {
    return era;
  }

  public void setEra(@Nullable String era) {
    this.era = era;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeagueTime leagueTime = (LeagueTime) o;
    return Objects.equals(this.gameTime, leagueTime.gameTime) &&
        Objects.equals(this.realTime, leagueTime.realTime) &&
        Objects.equals(this.timeAcceleration, leagueTime.timeAcceleration) &&
        Objects.equals(this.daysPassedGame, leagueTime.daysPassedGame) &&
        Objects.equals(this.daysRemainingGame, leagueTime.daysRemainingGame) &&
        Objects.equals(this.era, leagueTime.era);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameTime, realTime, timeAcceleration, daysPassedGame, daysRemainingGame, era);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeagueTime {\n");
    sb.append("    gameTime: ").append(toIndentedString(gameTime)).append("\n");
    sb.append("    realTime: ").append(toIndentedString(realTime)).append("\n");
    sb.append("    timeAcceleration: ").append(toIndentedString(timeAcceleration)).append("\n");
    sb.append("    daysPassedGame: ").append(toIndentedString(daysPassedGame)).append("\n");
    sb.append("    daysRemainingGame: ").append(toIndentedString(daysRemainingGame)).append("\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
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

