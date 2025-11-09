package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * SmartLootSetting
 */


public class SmartLootSetting {

  @Valid
  private List<String> classPreferences = new ArrayList<>();

  private @Nullable String minRarity;

  private @Nullable Boolean autoAssignLegendary;

  @Valid
  private List<String> blacklist = new ArrayList<>();

  public SmartLootSetting classPreferences(List<String> classPreferences) {
    this.classPreferences = classPreferences;
    return this;
  }

  public SmartLootSetting addClassPreferencesItem(String classPreferencesItem) {
    if (this.classPreferences == null) {
      this.classPreferences = new ArrayList<>();
    }
    this.classPreferences.add(classPreferencesItem);
    return this;
  }

  /**
   * Get classPreferences
   * @return classPreferences
   */
  
  @Schema(name = "classPreferences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("classPreferences")
  public List<String> getClassPreferences() {
    return classPreferences;
  }

  public void setClassPreferences(List<String> classPreferences) {
    this.classPreferences = classPreferences;
  }

  public SmartLootSetting minRarity(@Nullable String minRarity) {
    this.minRarity = minRarity;
    return this;
  }

  /**
   * Get minRarity
   * @return minRarity
   */
  
  @Schema(name = "minRarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minRarity")
  public @Nullable String getMinRarity() {
    return minRarity;
  }

  public void setMinRarity(@Nullable String minRarity) {
    this.minRarity = minRarity;
  }

  public SmartLootSetting autoAssignLegendary(@Nullable Boolean autoAssignLegendary) {
    this.autoAssignLegendary = autoAssignLegendary;
    return this;
  }

  /**
   * Get autoAssignLegendary
   * @return autoAssignLegendary
   */
  
  @Schema(name = "autoAssignLegendary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoAssignLegendary")
  public @Nullable Boolean getAutoAssignLegendary() {
    return autoAssignLegendary;
  }

  public void setAutoAssignLegendary(@Nullable Boolean autoAssignLegendary) {
    this.autoAssignLegendary = autoAssignLegendary;
  }

  public SmartLootSetting blacklist(List<String> blacklist) {
    this.blacklist = blacklist;
    return this;
  }

  public SmartLootSetting addBlacklistItem(String blacklistItem) {
    if (this.blacklist == null) {
      this.blacklist = new ArrayList<>();
    }
    this.blacklist.add(blacklistItem);
    return this;
  }

  /**
   * Get blacklist
   * @return blacklist
   */
  
  @Schema(name = "blacklist", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blacklist")
  public List<String> getBlacklist() {
    return blacklist;
  }

  public void setBlacklist(List<String> blacklist) {
    this.blacklist = blacklist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmartLootSetting smartLootSetting = (SmartLootSetting) o;
    return Objects.equals(this.classPreferences, smartLootSetting.classPreferences) &&
        Objects.equals(this.minRarity, smartLootSetting.minRarity) &&
        Objects.equals(this.autoAssignLegendary, smartLootSetting.autoAssignLegendary) &&
        Objects.equals(this.blacklist, smartLootSetting.blacklist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classPreferences, minRarity, autoAssignLegendary, blacklist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmartLootSetting {\n");
    sb.append("    classPreferences: ").append(toIndentedString(classPreferences)).append("\n");
    sb.append("    minRarity: ").append(toIndentedString(minRarity)).append("\n");
    sb.append("    autoAssignLegendary: ").append(toIndentedString(autoAssignLegendary)).append("\n");
    sb.append("    blacklist: ").append(toIndentedString(blacklist)).append("\n");
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

