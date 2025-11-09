package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.UserAnalyticsPopularFeaturesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UserAnalytics
 */


public class UserAnalytics {

  private @Nullable String timeRange;

  private @Nullable Integer dau;

  private @Nullable Integer mau;

  private @Nullable Integer newUsers;

  private @Nullable Integer totalUsers;

  @Valid
  private List<@Valid UserAnalyticsPopularFeaturesInner> popularFeatures = new ArrayList<>();

  public UserAnalytics timeRange(@Nullable String timeRange) {
    this.timeRange = timeRange;
    return this;
  }

  /**
   * Get timeRange
   * @return timeRange
   */
  
  @Schema(name = "time_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_range")
  public @Nullable String getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(@Nullable String timeRange) {
    this.timeRange = timeRange;
  }

  public UserAnalytics dau(@Nullable Integer dau) {
    this.dau = dau;
    return this;
  }

  /**
   * Daily Active Users
   * @return dau
   */
  
  @Schema(name = "dau", description = "Daily Active Users", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dau")
  public @Nullable Integer getDau() {
    return dau;
  }

  public void setDau(@Nullable Integer dau) {
    this.dau = dau;
  }

  public UserAnalytics mau(@Nullable Integer mau) {
    this.mau = mau;
    return this;
  }

  /**
   * Monthly Active Users
   * @return mau
   */
  
  @Schema(name = "mau", description = "Monthly Active Users", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mau")
  public @Nullable Integer getMau() {
    return mau;
  }

  public void setMau(@Nullable Integer mau) {
    this.mau = mau;
  }

  public UserAnalytics newUsers(@Nullable Integer newUsers) {
    this.newUsers = newUsers;
    return this;
  }

  /**
   * Get newUsers
   * @return newUsers
   */
  
  @Schema(name = "new_users", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_users")
  public @Nullable Integer getNewUsers() {
    return newUsers;
  }

  public void setNewUsers(@Nullable Integer newUsers) {
    this.newUsers = newUsers;
  }

  public UserAnalytics totalUsers(@Nullable Integer totalUsers) {
    this.totalUsers = totalUsers;
    return this;
  }

  /**
   * Get totalUsers
   * @return totalUsers
   */
  
  @Schema(name = "total_users", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_users")
  public @Nullable Integer getTotalUsers() {
    return totalUsers;
  }

  public void setTotalUsers(@Nullable Integer totalUsers) {
    this.totalUsers = totalUsers;
  }

  public UserAnalytics popularFeatures(List<@Valid UserAnalyticsPopularFeaturesInner> popularFeatures) {
    this.popularFeatures = popularFeatures;
    return this;
  }

  public UserAnalytics addPopularFeaturesItem(UserAnalyticsPopularFeaturesInner popularFeaturesItem) {
    if (this.popularFeatures == null) {
      this.popularFeatures = new ArrayList<>();
    }
    this.popularFeatures.add(popularFeaturesItem);
    return this;
  }

  /**
   * Get popularFeatures
   * @return popularFeatures
   */
  @Valid 
  @Schema(name = "popular_features", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("popular_features")
  public List<@Valid UserAnalyticsPopularFeaturesInner> getPopularFeatures() {
    return popularFeatures;
  }

  public void setPopularFeatures(List<@Valid UserAnalyticsPopularFeaturesInner> popularFeatures) {
    this.popularFeatures = popularFeatures;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAnalytics userAnalytics = (UserAnalytics) o;
    return Objects.equals(this.timeRange, userAnalytics.timeRange) &&
        Objects.equals(this.dau, userAnalytics.dau) &&
        Objects.equals(this.mau, userAnalytics.mau) &&
        Objects.equals(this.newUsers, userAnalytics.newUsers) &&
        Objects.equals(this.totalUsers, userAnalytics.totalUsers) &&
        Objects.equals(this.popularFeatures, userAnalytics.popularFeatures);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeRange, dau, mau, newUsers, totalUsers, popularFeatures);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAnalytics {\n");
    sb.append("    timeRange: ").append(toIndentedString(timeRange)).append("\n");
    sb.append("    dau: ").append(toIndentedString(dau)).append("\n");
    sb.append("    mau: ").append(toIndentedString(mau)).append("\n");
    sb.append("    newUsers: ").append(toIndentedString(newUsers)).append("\n");
    sb.append("    totalUsers: ").append(toIndentedString(totalUsers)).append("\n");
    sb.append("    popularFeatures: ").append(toIndentedString(popularFeatures)).append("\n");
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

