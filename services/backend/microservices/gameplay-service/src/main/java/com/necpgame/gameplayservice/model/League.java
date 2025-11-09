package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * League
 */


public class League {

  private String leagueId;

  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endDate;

  /**
   * Текущая фаза лиги
   */
  public enum CurrentPhaseEnum {
    START("start"),
    
    RISE("rise"),
    
    CRISIS("crisis"),
    
    ENDGAME("endgame"),
    
    FINALE("finale");

    private final String value;

    CurrentPhaseEnum(String value) {
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
    public static CurrentPhaseEnum fromValue(String value) {
      for (CurrentPhaseEnum b : CurrentPhaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CurrentPhaseEnum currentPhase;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime gameTimeCurrent;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime gameTimeStart;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime gameTimeEnd;

  private @Nullable String realTimeRemaining;

  private @Nullable BigDecimal timeAcceleration;

  /**
   * Gets or Sets leagueType
   */
  public enum LeagueTypeEnum {
    STANDARD("standard"),
    
    HARDCORE("hardcore"),
    
    EVENT("event");

    private final String value;

    LeagueTypeEnum(String value) {
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
    public static LeagueTypeEnum fromValue(String value) {
      for (LeagueTypeEnum b : LeagueTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LeagueTypeEnum leagueType;

  private @Nullable Integer playerCount;

  public League() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public League(String leagueId, String name, OffsetDateTime startDate, OffsetDateTime endDate, CurrentPhaseEnum currentPhase, LeagueTypeEnum leagueType) {
    this.leagueId = leagueId;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.currentPhase = currentPhase;
    this.leagueType = leagueType;
  }

  public League leagueId(String leagueId) {
    this.leagueId = leagueId;
    return this;
  }

  /**
   * Get leagueId
   * @return leagueId
   */
  @NotNull 
  @Schema(name = "league_id", example = "league_12", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("league_id")
  public String getLeagueId() {
    return leagueId;
  }

  public void setLeagueId(String leagueId) {
    this.leagueId = leagueId;
  }

  public League name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "League of Shadows #12", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public League startDate(OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Дата начала лиги (реальное время)
   * @return startDate
   */
  @NotNull @Valid 
  @Schema(name = "start_date", description = "Дата начала лиги (реальное время)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("start_date")
  public OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public League endDate(OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Дата конца лиги (реальное время)
   * @return endDate
   */
  @NotNull @Valid 
  @Schema(name = "end_date", description = "Дата конца лиги (реальное время)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("end_date")
  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public League currentPhase(CurrentPhaseEnum currentPhase) {
    this.currentPhase = currentPhase;
    return this;
  }

  /**
   * Текущая фаза лиги
   * @return currentPhase
   */
  @NotNull 
  @Schema(name = "current_phase", description = "Текущая фаза лиги", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current_phase")
  public CurrentPhaseEnum getCurrentPhase() {
    return currentPhase;
  }

  public void setCurrentPhase(CurrentPhaseEnum currentPhase) {
    this.currentPhase = currentPhase;
  }

  public League gameTimeCurrent(@Nullable OffsetDateTime gameTimeCurrent) {
    this.gameTimeCurrent = gameTimeCurrent;
    return this;
  }

  /**
   * Текущее игровое время (2020-2093)
   * @return gameTimeCurrent
   */
  @Valid 
  @Schema(name = "game_time_current", description = "Текущее игровое время (2020-2093)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("game_time_current")
  public @Nullable OffsetDateTime getGameTimeCurrent() {
    return gameTimeCurrent;
  }

  public void setGameTimeCurrent(@Nullable OffsetDateTime gameTimeCurrent) {
    this.gameTimeCurrent = gameTimeCurrent;
  }

  public League gameTimeStart(@Nullable OffsetDateTime gameTimeStart) {
    this.gameTimeStart = gameTimeStart;
    return this;
  }

  /**
   * Get gameTimeStart
   * @return gameTimeStart
   */
  @Valid 
  @Schema(name = "game_time_start", example = "2020-01-01T00:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("game_time_start")
  public @Nullable OffsetDateTime getGameTimeStart() {
    return gameTimeStart;
  }

  public void setGameTimeStart(@Nullable OffsetDateTime gameTimeStart) {
    this.gameTimeStart = gameTimeStart;
  }

  public League gameTimeEnd(@Nullable OffsetDateTime gameTimeEnd) {
    this.gameTimeEnd = gameTimeEnd;
    return this;
  }

  /**
   * Get gameTimeEnd
   * @return gameTimeEnd
   */
  @Valid 
  @Schema(name = "game_time_end", example = "2093-07-27T00:00Z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("game_time_end")
  public @Nullable OffsetDateTime getGameTimeEnd() {
    return gameTimeEnd;
  }

  public void setGameTimeEnd(@Nullable OffsetDateTime gameTimeEnd) {
    this.gameTimeEnd = gameTimeEnd;
  }

  public League realTimeRemaining(@Nullable String realTimeRemaining) {
    this.realTimeRemaining = realTimeRemaining;
    return this;
  }

  /**
   * Оставшееся реальное время до конца лиги
   * @return realTimeRemaining
   */
  
  @Schema(name = "real_time_remaining", description = "Оставшееся реальное время до конца лиги", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("real_time_remaining")
  public @Nullable String getRealTimeRemaining() {
    return realTimeRemaining;
  }

  public void setRealTimeRemaining(@Nullable String realTimeRemaining) {
    this.realTimeRemaining = realTimeRemaining;
  }

  public League timeAcceleration(@Nullable BigDecimal timeAcceleration) {
    this.timeAcceleration = timeAcceleration;
    return this;
  }

  /**
   * Ускорение времени (1 реальный день = X игровых дней)
   * @return timeAcceleration
   */
  @Valid 
  @Schema(name = "time_acceleration", example = "20", description = "Ускорение времени (1 реальный день = X игровых дней)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_acceleration")
  public @Nullable BigDecimal getTimeAcceleration() {
    return timeAcceleration;
  }

  public void setTimeAcceleration(@Nullable BigDecimal timeAcceleration) {
    this.timeAcceleration = timeAcceleration;
  }

  public League leagueType(LeagueTypeEnum leagueType) {
    this.leagueType = leagueType;
    return this;
  }

  /**
   * Get leagueType
   * @return leagueType
   */
  @NotNull 
  @Schema(name = "league_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("league_type")
  public LeagueTypeEnum getLeagueType() {
    return leagueType;
  }

  public void setLeagueType(LeagueTypeEnum leagueType) {
    this.leagueType = leagueType;
  }

  public League playerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
    return this;
  }

  /**
   * Количество активных игроков в лиге
   * @return playerCount
   */
  
  @Schema(name = "player_count", description = "Количество активных игроков в лиге", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_count")
  public @Nullable Integer getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    League league = (League) o;
    return Objects.equals(this.leagueId, league.leagueId) &&
        Objects.equals(this.name, league.name) &&
        Objects.equals(this.startDate, league.startDate) &&
        Objects.equals(this.endDate, league.endDate) &&
        Objects.equals(this.currentPhase, league.currentPhase) &&
        Objects.equals(this.gameTimeCurrent, league.gameTimeCurrent) &&
        Objects.equals(this.gameTimeStart, league.gameTimeStart) &&
        Objects.equals(this.gameTimeEnd, league.gameTimeEnd) &&
        Objects.equals(this.realTimeRemaining, league.realTimeRemaining) &&
        Objects.equals(this.timeAcceleration, league.timeAcceleration) &&
        Objects.equals(this.leagueType, league.leagueType) &&
        Objects.equals(this.playerCount, league.playerCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leagueId, name, startDate, endDate, currentPhase, gameTimeCurrent, gameTimeStart, gameTimeEnd, realTimeRemaining, timeAcceleration, leagueType, playerCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class League {\n");
    sb.append("    leagueId: ").append(toIndentedString(leagueId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    currentPhase: ").append(toIndentedString(currentPhase)).append("\n");
    sb.append("    gameTimeCurrent: ").append(toIndentedString(gameTimeCurrent)).append("\n");
    sb.append("    gameTimeStart: ").append(toIndentedString(gameTimeStart)).append("\n");
    sb.append("    gameTimeEnd: ").append(toIndentedString(gameTimeEnd)).append("\n");
    sb.append("    realTimeRemaining: ").append(toIndentedString(realTimeRemaining)).append("\n");
    sb.append("    timeAcceleration: ").append(toIndentedString(timeAcceleration)).append("\n");
    sb.append("    leagueType: ").append(toIndentedString(leagueType)).append("\n");
    sb.append("    playerCount: ").append(toIndentedString(playerCount)).append("\n");
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

