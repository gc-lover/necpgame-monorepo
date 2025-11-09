package com.necpgame.adminservice.model;

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
 * UserAnalyticsPopularFeaturesInner
 */

@JsonTypeName("UserAnalytics_popular_features_inner")

public class UserAnalyticsPopularFeaturesInner {

  private @Nullable String feature;

  private @Nullable Integer usageCount;

  public UserAnalyticsPopularFeaturesInner feature(@Nullable String feature) {
    this.feature = feature;
    return this;
  }

  /**
   * Get feature
   * @return feature
   */
  
  @Schema(name = "feature", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("feature")
  public @Nullable String getFeature() {
    return feature;
  }

  public void setFeature(@Nullable String feature) {
    this.feature = feature;
  }

  public UserAnalyticsPopularFeaturesInner usageCount(@Nullable Integer usageCount) {
    this.usageCount = usageCount;
    return this;
  }

  /**
   * Get usageCount
   * @return usageCount
   */
  
  @Schema(name = "usage_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usage_count")
  public @Nullable Integer getUsageCount() {
    return usageCount;
  }

  public void setUsageCount(@Nullable Integer usageCount) {
    this.usageCount = usageCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAnalyticsPopularFeaturesInner userAnalyticsPopularFeaturesInner = (UserAnalyticsPopularFeaturesInner) o;
    return Objects.equals(this.feature, userAnalyticsPopularFeaturesInner.feature) &&
        Objects.equals(this.usageCount, userAnalyticsPopularFeaturesInner.usageCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(feature, usageCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAnalyticsPopularFeaturesInner {\n");
    sb.append("    feature: ").append(toIndentedString(feature)).append("\n");
    sb.append("    usageCount: ").append(toIndentedString(usageCount)).append("\n");
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

