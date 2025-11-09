package com.necpgame.adminservice.model;

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
 * FeatureFlag
 */


public class FeatureFlag {

  private @Nullable String flagName;

  private @Nullable Boolean enabled;

  private @Nullable Integer rolloutPercentage;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public FeatureFlag flagName(@Nullable String flagName) {
    this.flagName = flagName;
    return this;
  }

  /**
   * Get flagName
   * @return flagName
   */
  
  @Schema(name = "flag_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flag_name")
  public @Nullable String getFlagName() {
    return flagName;
  }

  public void setFlagName(@Nullable String flagName) {
    this.flagName = flagName;
  }

  public FeatureFlag enabled(@Nullable Boolean enabled) {
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

  public FeatureFlag rolloutPercentage(@Nullable Integer rolloutPercentage) {
    this.rolloutPercentage = rolloutPercentage;
    return this;
  }

  /**
   * Get rolloutPercentage
   * @return rolloutPercentage
   */
  
  @Schema(name = "rollout_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollout_percentage")
  public @Nullable Integer getRolloutPercentage() {
    return rolloutPercentage;
  }

  public void setRolloutPercentage(@Nullable Integer rolloutPercentage) {
    this.rolloutPercentage = rolloutPercentage;
  }

  public FeatureFlag createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public FeatureFlag updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updated_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated_at")
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
    FeatureFlag featureFlag = (FeatureFlag) o;
    return Objects.equals(this.flagName, featureFlag.flagName) &&
        Objects.equals(this.enabled, featureFlag.enabled) &&
        Objects.equals(this.rolloutPercentage, featureFlag.rolloutPercentage) &&
        Objects.equals(this.createdAt, featureFlag.createdAt) &&
        Objects.equals(this.updatedAt, featureFlag.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flagName, enabled, rolloutPercentage, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FeatureFlag {\n");
    sb.append("    flagName: ").append(toIndentedString(flagName)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    rolloutPercentage: ").append(toIndentedString(rolloutPercentage)).append("\n");
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

