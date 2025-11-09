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
 * AnalyticsResponseRarityDistributionInner
 */

@JsonTypeName("AnalyticsResponse_rarityDistribution_inner")

public class AnalyticsResponseRarityDistributionInner {

  private @Nullable String rarity;

  private @Nullable BigDecimal share;

  public AnalyticsResponseRarityDistributionInner rarity(@Nullable String rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable String getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable String rarity) {
    this.rarity = rarity;
  }

  public AnalyticsResponseRarityDistributionInner share(@Nullable BigDecimal share) {
    this.share = share;
    return this;
  }

  /**
   * Get share
   * @return share
   */
  @Valid 
  @Schema(name = "share", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("share")
  public @Nullable BigDecimal getShare() {
    return share;
  }

  public void setShare(@Nullable BigDecimal share) {
    this.share = share;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponseRarityDistributionInner analyticsResponseRarityDistributionInner = (AnalyticsResponseRarityDistributionInner) o;
    return Objects.equals(this.rarity, analyticsResponseRarityDistributionInner.rarity) &&
        Objects.equals(this.share, analyticsResponseRarityDistributionInner.share);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rarity, share);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponseRarityDistributionInner {\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    share: ").append(toIndentedString(share)).append("\n");
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

