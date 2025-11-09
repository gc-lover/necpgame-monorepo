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
 * UpdateFeatureFlagRequest
 */

@JsonTypeName("updateFeatureFlag_request")

public class UpdateFeatureFlagRequest {

  private String flagName;

  private Boolean enabled;

  private @Nullable Integer rolloutPercentage;

  public UpdateFeatureFlagRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateFeatureFlagRequest(String flagName, Boolean enabled) {
    this.flagName = flagName;
    this.enabled = enabled;
  }

  public UpdateFeatureFlagRequest flagName(String flagName) {
    this.flagName = flagName;
    return this;
  }

  /**
   * Get flagName
   * @return flagName
   */
  @NotNull 
  @Schema(name = "flag_name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("flag_name")
  public String getFlagName() {
    return flagName;
  }

  public void setFlagName(String flagName) {
    this.flagName = flagName;
  }

  public UpdateFeatureFlagRequest enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  @NotNull 
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public UpdateFeatureFlagRequest rolloutPercentage(@Nullable Integer rolloutPercentage) {
    this.rolloutPercentage = rolloutPercentage;
    return this;
  }

  /**
   * Get rolloutPercentage
   * minimum: 0
   * maximum: 100
   * @return rolloutPercentage
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "rollout_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollout_percentage")
  public @Nullable Integer getRolloutPercentage() {
    return rolloutPercentage;
  }

  public void setRolloutPercentage(@Nullable Integer rolloutPercentage) {
    this.rolloutPercentage = rolloutPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateFeatureFlagRequest updateFeatureFlagRequest = (UpdateFeatureFlagRequest) o;
    return Objects.equals(this.flagName, updateFeatureFlagRequest.flagName) &&
        Objects.equals(this.enabled, updateFeatureFlagRequest.enabled) &&
        Objects.equals(this.rolloutPercentage, updateFeatureFlagRequest.rolloutPercentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flagName, enabled, rolloutPercentage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateFeatureFlagRequest {\n");
    sb.append("    flagName: ").append(toIndentedString(flagName)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    rolloutPercentage: ").append(toIndentedString(rolloutPercentage)).append("\n");
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

