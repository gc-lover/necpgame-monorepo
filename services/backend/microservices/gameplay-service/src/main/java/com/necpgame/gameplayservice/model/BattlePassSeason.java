package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * BattlePassSeason
 */


public class BattlePassSeason {

  private String seasonId;

  private Integer seasonNumber;

  private String name;

  private @Nullable String theme;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endDate;

  private @Nullable Integer maxLevel;

  private @Nullable Integer xpPerLevel;

  private @Nullable Integer premiumPrice;

  private @Nullable String premiumCurrency;

  private @Nullable Integer xpCapPerDay;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SCHEDULED("SCHEDULED"),
    
    ACTIVE("ACTIVE"),
    
    ENDED("ENDED"),
    
    ARCHIVED("ARCHIVED");

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

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public BattlePassSeason() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BattlePassSeason(String seasonId, Integer seasonNumber, String name, OffsetDateTime startDate, OffsetDateTime endDate, StatusEnum status) {
    this.seasonId = seasonId;
    this.seasonNumber = seasonNumber;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
  }

  public BattlePassSeason seasonId(String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  @NotNull 
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonId")
  public String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(String seasonId) {
    this.seasonId = seasonId;
  }

  public BattlePassSeason seasonNumber(Integer seasonNumber) {
    this.seasonNumber = seasonNumber;
    return this;
  }

  /**
   * Get seasonNumber
   * @return seasonNumber
   */
  @NotNull 
  @Schema(name = "seasonNumber", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("seasonNumber")
  public Integer getSeasonNumber() {
    return seasonNumber;
  }

  public void setSeasonNumber(Integer seasonNumber) {
    this.seasonNumber = seasonNumber;
  }

  public BattlePassSeason name(String name) {
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

  public BattlePassSeason theme(@Nullable String theme) {
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

  public BattlePassSeason description(@Nullable String description) {
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

  public BattlePassSeason startDate(OffsetDateTime startDate) {
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

  public BattlePassSeason endDate(OffsetDateTime endDate) {
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

  public BattlePassSeason maxLevel(@Nullable Integer maxLevel) {
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

  public BattlePassSeason xpPerLevel(@Nullable Integer xpPerLevel) {
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

  public BattlePassSeason premiumPrice(@Nullable Integer premiumPrice) {
    this.premiumPrice = premiumPrice;
    return this;
  }

  /**
   * Get premiumPrice
   * @return premiumPrice
   */
  
  @Schema(name = "premiumPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumPrice")
  public @Nullable Integer getPremiumPrice() {
    return premiumPrice;
  }

  public void setPremiumPrice(@Nullable Integer premiumPrice) {
    this.premiumPrice = premiumPrice;
  }

  public BattlePassSeason premiumCurrency(@Nullable String premiumCurrency) {
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

  public BattlePassSeason xpCapPerDay(@Nullable Integer xpCapPerDay) {
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

  public BattlePassSeason status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public BattlePassSeason createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public BattlePassSeason updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BattlePassSeason battlePassSeason = (BattlePassSeason) o;
    return Objects.equals(this.seasonId, battlePassSeason.seasonId) &&
        Objects.equals(this.seasonNumber, battlePassSeason.seasonNumber) &&
        Objects.equals(this.name, battlePassSeason.name) &&
        Objects.equals(this.theme, battlePassSeason.theme) &&
        Objects.equals(this.description, battlePassSeason.description) &&
        Objects.equals(this.startDate, battlePassSeason.startDate) &&
        Objects.equals(this.endDate, battlePassSeason.endDate) &&
        Objects.equals(this.maxLevel, battlePassSeason.maxLevel) &&
        Objects.equals(this.xpPerLevel, battlePassSeason.xpPerLevel) &&
        Objects.equals(this.premiumPrice, battlePassSeason.premiumPrice) &&
        Objects.equals(this.premiumCurrency, battlePassSeason.premiumCurrency) &&
        Objects.equals(this.xpCapPerDay, battlePassSeason.xpCapPerDay) &&
        Objects.equals(this.status, battlePassSeason.status) &&
        Objects.equals(this.createdAt, battlePassSeason.createdAt) &&
        Objects.equals(this.updatedAt, battlePassSeason.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seasonId, seasonNumber, name, theme, description, startDate, endDate, maxLevel, xpPerLevel, premiumPrice, premiumCurrency, xpCapPerDay, status, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassSeason {\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
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
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

