package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BattlePassAnalyticsResponseRetentionMetrics
 */

@JsonTypeName("BattlePassAnalyticsResponse_retentionMetrics")

public class BattlePassAnalyticsResponseRetentionMetrics {

  private @Nullable BigDecimal day7;

  private @Nullable BigDecimal day30;

  private @Nullable BigDecimal premiumRetention;

  public BattlePassAnalyticsResponseRetentionMetrics day7(@Nullable BigDecimal day7) {
    this.day7 = day7;
    return this;
  }

  /**
   * Get day7
   * @return day7
   */
  @Valid 
  @Schema(name = "day7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("day7")
  public @Nullable BigDecimal getDay7() {
    return day7;
  }

  public void setDay7(@Nullable BigDecimal day7) {
    this.day7 = day7;
  }

  public BattlePassAnalyticsResponseRetentionMetrics day30(@Nullable BigDecimal day30) {
    this.day30 = day30;
    return this;
  }

  /**
   * Get day30
   * @return day30
   */
  @Valid 
  @Schema(name = "day30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("day30")
  public @Nullable BigDecimal getDay30() {
    return day30;
  }

  public void setDay30(@Nullable BigDecimal day30) {
    this.day30 = day30;
  }

  public BattlePassAnalyticsResponseRetentionMetrics premiumRetention(@Nullable BigDecimal premiumRetention) {
    this.premiumRetention = premiumRetention;
    return this;
  }

  /**
   * Get premiumRetention
   * @return premiumRetention
   */
  @Valid 
  @Schema(name = "premiumRetention", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumRetention")
  public @Nullable BigDecimal getPremiumRetention() {
    return premiumRetention;
  }

  public void setPremiumRetention(@Nullable BigDecimal premiumRetention) {
    this.premiumRetention = premiumRetention;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BattlePassAnalyticsResponseRetentionMetrics battlePassAnalyticsResponseRetentionMetrics = (BattlePassAnalyticsResponseRetentionMetrics) o;
    return Objects.equals(this.day7, battlePassAnalyticsResponseRetentionMetrics.day7) &&
        Objects.equals(this.day30, battlePassAnalyticsResponseRetentionMetrics.day30) &&
        Objects.equals(this.premiumRetention, battlePassAnalyticsResponseRetentionMetrics.premiumRetention);
  }

  @Override
  public int hashCode() {
    return Objects.hash(day7, day30, premiumRetention);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassAnalyticsResponseRetentionMetrics {\n");
    sb.append("    day7: ").append(toIndentedString(day7)).append("\n");
    sb.append("    day30: ").append(toIndentedString(day30)).append("\n");
    sb.append("    premiumRetention: ").append(toIndentedString(premiumRetention)).append("\n");
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

