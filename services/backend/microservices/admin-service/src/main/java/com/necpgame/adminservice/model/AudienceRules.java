package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * AudienceRules
 */


public class AudienceRules {

  @Valid
  private List<String> regions = new ArrayList<>();

  /**
   * Gets or Sets platforms
   */
  public enum PlatformsEnum {
    PC("pc"),
    
    CONSOLE("console"),
    
    MOBILE("mobile");

    private final String value;

    PlatformsEnum(String value) {
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
    public static PlatformsEnum fromValue(String value) {
      for (PlatformsEnum b : PlatformsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<PlatformsEnum> platforms = new ArrayList<>();

  private @Nullable Integer minLevel;

  private @Nullable Integer maxLevel;

  @Valid
  private List<String> subscriptionTags = new ArrayList<>();

  @Valid
  private List<String> excludeClans = new ArrayList<>();

  public AudienceRules regions(List<String> regions) {
    this.regions = regions;
    return this;
  }

  public AudienceRules addRegionsItem(String regionsItem) {
    if (this.regions == null) {
      this.regions = new ArrayList<>();
    }
    this.regions.add(regionsItem);
    return this;
  }

  /**
   * Get regions
   * @return regions
   */
  
  @Schema(name = "regions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regions")
  public List<String> getRegions() {
    return regions;
  }

  public void setRegions(List<String> regions) {
    this.regions = regions;
  }

  public AudienceRules platforms(List<PlatformsEnum> platforms) {
    this.platforms = platforms;
    return this;
  }

  public AudienceRules addPlatformsItem(PlatformsEnum platformsItem) {
    if (this.platforms == null) {
      this.platforms = new ArrayList<>();
    }
    this.platforms.add(platformsItem);
    return this;
  }

  /**
   * Get platforms
   * @return platforms
   */
  
  @Schema(name = "platforms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("platforms")
  public List<PlatformsEnum> getPlatforms() {
    return platforms;
  }

  public void setPlatforms(List<PlatformsEnum> platforms) {
    this.platforms = platforms;
  }

  public AudienceRules minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Get minLevel
   * minimum: 1
   * @return minLevel
   */
  @Min(value = 1) 
  @Schema(name = "minLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLevel")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public AudienceRules maxLevel(@Nullable Integer maxLevel) {
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

  public AudienceRules subscriptionTags(List<String> subscriptionTags) {
    this.subscriptionTags = subscriptionTags;
    return this;
  }

  public AudienceRules addSubscriptionTagsItem(String subscriptionTagsItem) {
    if (this.subscriptionTags == null) {
      this.subscriptionTags = new ArrayList<>();
    }
    this.subscriptionTags.add(subscriptionTagsItem);
    return this;
  }

  /**
   * Get subscriptionTags
   * @return subscriptionTags
   */
  
  @Schema(name = "subscriptionTags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subscriptionTags")
  public List<String> getSubscriptionTags() {
    return subscriptionTags;
  }

  public void setSubscriptionTags(List<String> subscriptionTags) {
    this.subscriptionTags = subscriptionTags;
  }

  public AudienceRules excludeClans(List<String> excludeClans) {
    this.excludeClans = excludeClans;
    return this;
  }

  public AudienceRules addExcludeClansItem(String excludeClansItem) {
    if (this.excludeClans == null) {
      this.excludeClans = new ArrayList<>();
    }
    this.excludeClans.add(excludeClansItem);
    return this;
  }

  /**
   * Get excludeClans
   * @return excludeClans
   */
  
  @Schema(name = "excludeClans", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("excludeClans")
  public List<String> getExcludeClans() {
    return excludeClans;
  }

  public void setExcludeClans(List<String> excludeClans) {
    this.excludeClans = excludeClans;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AudienceRules audienceRules = (AudienceRules) o;
    return Objects.equals(this.regions, audienceRules.regions) &&
        Objects.equals(this.platforms, audienceRules.platforms) &&
        Objects.equals(this.minLevel, audienceRules.minLevel) &&
        Objects.equals(this.maxLevel, audienceRules.maxLevel) &&
        Objects.equals(this.subscriptionTags, audienceRules.subscriptionTags) &&
        Objects.equals(this.excludeClans, audienceRules.excludeClans);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regions, platforms, minLevel, maxLevel, subscriptionTags, excludeClans);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AudienceRules {\n");
    sb.append("    regions: ").append(toIndentedString(regions)).append("\n");
    sb.append("    platforms: ").append(toIndentedString(platforms)).append("\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    maxLevel: ").append(toIndentedString(maxLevel)).append("\n");
    sb.append("    subscriptionTags: ").append(toIndentedString(subscriptionTags)).append("\n");
    sb.append("    excludeClans: ").append(toIndentedString(excludeClans)).append("\n");
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

