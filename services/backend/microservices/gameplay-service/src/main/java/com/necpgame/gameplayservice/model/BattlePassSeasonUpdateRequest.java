package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * BattlePassSeasonUpdateRequest
 */


public class BattlePassSeasonUpdateRequest {

  private @Nullable String name;

  private @Nullable String theme;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endDate;

  private @Nullable Integer maxLevel;

  private @Nullable Integer xpPerLevel;

  private @Nullable Integer xpCapPerDay;

  public BattlePassSeasonUpdateRequest name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public BattlePassSeasonUpdateRequest theme(@Nullable String theme) {
    this.theme = theme;
    return this;
  }

  /**
   * Get theme
   * @return theme
   */
  
  @Schema(name = "theme", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("theme")
  public @Nullable String getTheme() {
    return theme;
  }

  public void setTheme(@Nullable String theme) {
    this.theme = theme;
  }

  public BattlePassSeasonUpdateRequest description(@Nullable String description) {
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

  public BattlePassSeasonUpdateRequest startDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @Valid 
  @Schema(name = "startDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startDate")
  public @Nullable OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public BattlePassSeasonUpdateRequest endDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @Valid 
  @Schema(name = "endDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endDate")
  public @Nullable OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public BattlePassSeasonUpdateRequest maxLevel(@Nullable Integer maxLevel) {
    this.maxLevel = maxLevel;
    return this;
  }

  /**
   * Get maxLevel
   * @return maxLevel
   */
  
  @Schema(name = "maxLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxLevel")
  public @Nullable Integer getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(@Nullable Integer maxLevel) {
    this.maxLevel = maxLevel;
  }

  public BattlePassSeasonUpdateRequest xpPerLevel(@Nullable Integer xpPerLevel) {
    this.xpPerLevel = xpPerLevel;
    return this;
  }

  /**
   * Get xpPerLevel
   * @return xpPerLevel
   */
  
  @Schema(name = "xpPerLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpPerLevel")
  public @Nullable Integer getXpPerLevel() {
    return xpPerLevel;
  }

  public void setXpPerLevel(@Nullable Integer xpPerLevel) {
    this.xpPerLevel = xpPerLevel;
  }

  public BattlePassSeasonUpdateRequest xpCapPerDay(@Nullable Integer xpCapPerDay) {
    this.xpCapPerDay = xpCapPerDay;
    return this;
  }

  /**
   * Get xpCapPerDay
   * @return xpCapPerDay
   */
  
  @Schema(name = "xpCapPerDay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpCapPerDay")
  public @Nullable Integer getXpCapPerDay() {
    return xpCapPerDay;
  }

  public void setXpCapPerDay(@Nullable Integer xpCapPerDay) {
    this.xpCapPerDay = xpCapPerDay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BattlePassSeasonUpdateRequest battlePassSeasonUpdateRequest = (BattlePassSeasonUpdateRequest) o;
    return Objects.equals(this.name, battlePassSeasonUpdateRequest.name) &&
        Objects.equals(this.theme, battlePassSeasonUpdateRequest.theme) &&
        Objects.equals(this.description, battlePassSeasonUpdateRequest.description) &&
        Objects.equals(this.startDate, battlePassSeasonUpdateRequest.startDate) &&
        Objects.equals(this.endDate, battlePassSeasonUpdateRequest.endDate) &&
        Objects.equals(this.maxLevel, battlePassSeasonUpdateRequest.maxLevel) &&
        Objects.equals(this.xpPerLevel, battlePassSeasonUpdateRequest.xpPerLevel) &&
        Objects.equals(this.xpCapPerDay, battlePassSeasonUpdateRequest.xpCapPerDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, theme, description, startDate, endDate, maxLevel, xpPerLevel, xpCapPerDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassSeasonUpdateRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    theme: ").append(toIndentedString(theme)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    maxLevel: ").append(toIndentedString(maxLevel)).append("\n");
    sb.append("    xpPerLevel: ").append(toIndentedString(xpPerLevel)).append("\n");
    sb.append("    xpCapPerDay: ").append(toIndentedString(xpCapPerDay)).append("\n");
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

