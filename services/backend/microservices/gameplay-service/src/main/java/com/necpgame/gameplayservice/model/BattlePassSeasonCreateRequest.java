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
 * BattlePassSeasonCreateRequest
 */


public class BattlePassSeasonCreateRequest {

  private Integer seasonNumber;

  private String name;

  private @Nullable String theme;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endDate;

  private Integer maxLevel;

  private Integer xpPerLevel;

  private @Nullable Integer premiumPrice;

  private @Nullable String premiumCurrency;

  private @Nullable Integer xpCapPerDay;

  public BattlePassSeasonCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BattlePassSeasonCreateRequest(Integer seasonNumber, String name, OffsetDateTime startDate, OffsetDateTime endDate, Integer maxLevel, Integer xpPerLevel) {
    this.seasonNumber = seasonNumber;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.maxLevel = maxLevel;
    this.xpPerLevel = xpPerLevel;
  }

  public BattlePassSeasonCreateRequest seasonNumber(Integer seasonNumber) {
    this.seasonNumber = seasonNumber;
    return this;
  }

  /**
   * Get seasonNumber
   * minimum: 1
   * @return seasonNumber
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "seasonNumber", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonNumber")
  public Integer getSeasonNumber() {
    return seasonNumber;
  }

  public void setSeasonNumber(Integer seasonNumber) {
    this.seasonNumber = seasonNumber;
  }

  public BattlePassSeasonCreateRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BattlePassSeasonCreateRequest theme(@Nullable String theme) {
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

  public BattlePassSeasonCreateRequest description(@Nullable String description) {
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

  public BattlePassSeasonCreateRequest startDate(OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @NotNull @Valid 
  @Schema(name = "startDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startDate")
  public OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public BattlePassSeasonCreateRequest endDate(OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @NotNull @Valid 
  @Schema(name = "endDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endDate")
  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public BattlePassSeasonCreateRequest maxLevel(Integer maxLevel) {
    this.maxLevel = maxLevel;
    return this;
  }

  /**
   * Get maxLevel
   * minimum: 1
   * @return maxLevel
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "maxLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxLevel")
  public Integer getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(Integer maxLevel) {
    this.maxLevel = maxLevel;
  }

  public BattlePassSeasonCreateRequest xpPerLevel(Integer xpPerLevel) {
    this.xpPerLevel = xpPerLevel;
    return this;
  }

  /**
   * Get xpPerLevel
   * minimum: 1
   * @return xpPerLevel
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "xpPerLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("xpPerLevel")
  public Integer getXpPerLevel() {
    return xpPerLevel;
  }

  public void setXpPerLevel(Integer xpPerLevel) {
    this.xpPerLevel = xpPerLevel;
  }

  public BattlePassSeasonCreateRequest premiumPrice(@Nullable Integer premiumPrice) {
    this.premiumPrice = premiumPrice;
    return this;
  }

  /**
   * Get premiumPrice
   * minimum: 0
   * @return premiumPrice
   */
  @Min(value = 0) 
  @Schema(name = "premiumPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumPrice")
  public @Nullable Integer getPremiumPrice() {
    return premiumPrice;
  }

  public void setPremiumPrice(@Nullable Integer premiumPrice) {
    this.premiumPrice = premiumPrice;
  }

  public BattlePassSeasonCreateRequest premiumCurrency(@Nullable String premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
    return this;
  }

  /**
   * Get premiumCurrency
   * @return premiumCurrency
   */
  
  @Schema(name = "premiumCurrency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumCurrency")
  public @Nullable String getPremiumCurrency() {
    return premiumCurrency;
  }

  public void setPremiumCurrency(@Nullable String premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
  }

  public BattlePassSeasonCreateRequest xpCapPerDay(@Nullable Integer xpCapPerDay) {
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
    BattlePassSeasonCreateRequest battlePassSeasonCreateRequest = (BattlePassSeasonCreateRequest) o;
    return Objects.equals(this.seasonNumber, battlePassSeasonCreateRequest.seasonNumber) &&
        Objects.equals(this.name, battlePassSeasonCreateRequest.name) &&
        Objects.equals(this.theme, battlePassSeasonCreateRequest.theme) &&
        Objects.equals(this.description, battlePassSeasonCreateRequest.description) &&
        Objects.equals(this.startDate, battlePassSeasonCreateRequest.startDate) &&
        Objects.equals(this.endDate, battlePassSeasonCreateRequest.endDate) &&
        Objects.equals(this.maxLevel, battlePassSeasonCreateRequest.maxLevel) &&
        Objects.equals(this.xpPerLevel, battlePassSeasonCreateRequest.xpPerLevel) &&
        Objects.equals(this.premiumPrice, battlePassSeasonCreateRequest.premiumPrice) &&
        Objects.equals(this.premiumCurrency, battlePassSeasonCreateRequest.premiumCurrency) &&
        Objects.equals(this.xpCapPerDay, battlePassSeasonCreateRequest.xpCapPerDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seasonNumber, name, theme, description, startDate, endDate, maxLevel, xpPerLevel, premiumPrice, premiumCurrency, xpCapPerDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassSeasonCreateRequest {\n");
    sb.append("    seasonNumber: ").append(toIndentedString(seasonNumber)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    theme: ").append(toIndentedString(theme)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    maxLevel: ").append(toIndentedString(maxLevel)).append("\n");
    sb.append("    xpPerLevel: ").append(toIndentedString(xpPerLevel)).append("\n");
    sb.append("    premiumPrice: ").append(toIndentedString(premiumPrice)).append("\n");
    sb.append("    premiumCurrency: ").append(toIndentedString(premiumCurrency)).append("\n");
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

