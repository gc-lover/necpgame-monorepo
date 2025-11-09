package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ClanPerformance;
import com.necpgame.socialservice.model.WarAnalyticsResponseTerritoryControlInner;
import com.necpgame.socialservice.model.WarAnalyticsResponseWinRate;
import java.math.BigDecimal;
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
 * WarAnalyticsResponse
 */


public class WarAnalyticsResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime generatedAt;

  private @Nullable String range;

  private @Nullable BigDecimal warsPerWeek;

  private @Nullable WarAnalyticsResponseWinRate winRate;

  @Valid
  private List<@Valid WarAnalyticsResponseTerritoryControlInner> territoryControl = new ArrayList<>();

  private @Nullable Integer economicImpact;

  @Valid
  private List<@Valid ClanPerformance> topClans = new ArrayList<>();

  public WarAnalyticsResponse generatedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
    return this;
  }

  /**
   * Get generatedAt
   * @return generatedAt
   */
  @Valid 
  @Schema(name = "generatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedAt")
  public @Nullable OffsetDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  public WarAnalyticsResponse range(@Nullable String range) {
    this.range = range;
    return this;
  }

  /**
   * Get range
   * @return range
   */
  
  @Schema(name = "range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable String getRange() {
    return range;
  }

  public void setRange(@Nullable String range) {
    this.range = range;
  }

  public WarAnalyticsResponse warsPerWeek(@Nullable BigDecimal warsPerWeek) {
    this.warsPerWeek = warsPerWeek;
    return this;
  }

  /**
   * Get warsPerWeek
   * @return warsPerWeek
   */
  @Valid 
  @Schema(name = "warsPerWeek", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warsPerWeek")
  public @Nullable BigDecimal getWarsPerWeek() {
    return warsPerWeek;
  }

  public void setWarsPerWeek(@Nullable BigDecimal warsPerWeek) {
    this.warsPerWeek = warsPerWeek;
  }

  public WarAnalyticsResponse winRate(@Nullable WarAnalyticsResponseWinRate winRate) {
    this.winRate = winRate;
    return this;
  }

  /**
   * Get winRate
   * @return winRate
   */
  @Valid 
  @Schema(name = "winRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winRate")
  public @Nullable WarAnalyticsResponseWinRate getWinRate() {
    return winRate;
  }

  public void setWinRate(@Nullable WarAnalyticsResponseWinRate winRate) {
    this.winRate = winRate;
  }

  public WarAnalyticsResponse territoryControl(List<@Valid WarAnalyticsResponseTerritoryControlInner> territoryControl) {
    this.territoryControl = territoryControl;
    return this;
  }

  public WarAnalyticsResponse addTerritoryControlItem(WarAnalyticsResponseTerritoryControlInner territoryControlItem) {
    if (this.territoryControl == null) {
      this.territoryControl = new ArrayList<>();
    }
    this.territoryControl.add(territoryControlItem);
    return this;
  }

  /**
   * Get territoryControl
   * @return territoryControl
   */
  @Valid 
  @Schema(name = "territoryControl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territoryControl")
  public List<@Valid WarAnalyticsResponseTerritoryControlInner> getTerritoryControl() {
    return territoryControl;
  }

  public void setTerritoryControl(List<@Valid WarAnalyticsResponseTerritoryControlInner> territoryControl) {
    this.territoryControl = territoryControl;
  }

  public WarAnalyticsResponse economicImpact(@Nullable Integer economicImpact) {
    this.economicImpact = economicImpact;
    return this;
  }

  /**
   * Get economicImpact
   * @return economicImpact
   */
  
  @Schema(name = "economicImpact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economicImpact")
  public @Nullable Integer getEconomicImpact() {
    return economicImpact;
  }

  public void setEconomicImpact(@Nullable Integer economicImpact) {
    this.economicImpact = economicImpact;
  }

  public WarAnalyticsResponse topClans(List<@Valid ClanPerformance> topClans) {
    this.topClans = topClans;
    return this;
  }

  public WarAnalyticsResponse addTopClansItem(ClanPerformance topClansItem) {
    if (this.topClans == null) {
      this.topClans = new ArrayList<>();
    }
    this.topClans.add(topClansItem);
    return this;
  }

  /**
   * Get topClans
   * @return topClans
   */
  @Valid 
  @Schema(name = "topClans", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topClans")
  public List<@Valid ClanPerformance> getTopClans() {
    return topClans;
  }

  public void setTopClans(List<@Valid ClanPerformance> topClans) {
    this.topClans = topClans;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarAnalyticsResponse warAnalyticsResponse = (WarAnalyticsResponse) o;
    return Objects.equals(this.generatedAt, warAnalyticsResponse.generatedAt) &&
        Objects.equals(this.range, warAnalyticsResponse.range) &&
        Objects.equals(this.warsPerWeek, warAnalyticsResponse.warsPerWeek) &&
        Objects.equals(this.winRate, warAnalyticsResponse.winRate) &&
        Objects.equals(this.territoryControl, warAnalyticsResponse.territoryControl) &&
        Objects.equals(this.economicImpact, warAnalyticsResponse.economicImpact) &&
        Objects.equals(this.topClans, warAnalyticsResponse.topClans);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generatedAt, range, warsPerWeek, winRate, territoryControl, economicImpact, topClans);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarAnalyticsResponse {\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    warsPerWeek: ").append(toIndentedString(warsPerWeek)).append("\n");
    sb.append("    winRate: ").append(toIndentedString(winRate)).append("\n");
    sb.append("    territoryControl: ").append(toIndentedString(territoryControl)).append("\n");
    sb.append("    economicImpact: ").append(toIndentedString(economicImpact)).append("\n");
    sb.append("    topClans: ").append(toIndentedString(topClans)).append("\n");
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

