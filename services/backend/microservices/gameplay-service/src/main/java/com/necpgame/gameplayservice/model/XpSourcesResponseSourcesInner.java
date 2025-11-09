package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * XpSourcesResponseSourcesInner
 */

@JsonTypeName("XpSourcesResponse_sources_inner")

public class XpSourcesResponseSourcesInner {

  private @Nullable String source;

  private @Nullable Integer baseAmount;

  private @Nullable Integer dailyLimit;

  private @Nullable Boolean enabled;

  public XpSourcesResponseSourcesInner source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  public XpSourcesResponseSourcesInner baseAmount(@Nullable Integer baseAmount) {
    this.baseAmount = baseAmount;
    return this;
  }

  /**
   * Get baseAmount
   * @return baseAmount
   */
  
  @Schema(name = "baseAmount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("baseAmount")
  public @Nullable Integer getBaseAmount() {
    return baseAmount;
  }

  public void setBaseAmount(@Nullable Integer baseAmount) {
    this.baseAmount = baseAmount;
  }

  public XpSourcesResponseSourcesInner dailyLimit(@Nullable Integer dailyLimit) {
    this.dailyLimit = dailyLimit;
    return this;
  }

  /**
   * Get dailyLimit
   * @return dailyLimit
   */
  
  @Schema(name = "dailyLimit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dailyLimit")
  public @Nullable Integer getDailyLimit() {
    return dailyLimit;
  }

  public void setDailyLimit(@Nullable Integer dailyLimit) {
    this.dailyLimit = dailyLimit;
  }

  public XpSourcesResponseSourcesInner enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XpSourcesResponseSourcesInner xpSourcesResponseSourcesInner = (XpSourcesResponseSourcesInner) o;
    return Objects.equals(this.source, xpSourcesResponseSourcesInner.source) &&
        Objects.equals(this.baseAmount, xpSourcesResponseSourcesInner.baseAmount) &&
        Objects.equals(this.dailyLimit, xpSourcesResponseSourcesInner.dailyLimit) &&
        Objects.equals(this.enabled, xpSourcesResponseSourcesInner.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, baseAmount, dailyLimit, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XpSourcesResponseSourcesInner {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    baseAmount: ").append(toIndentedString(baseAmount)).append("\n");
    sb.append("    dailyLimit: ").append(toIndentedString(dailyLimit)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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

