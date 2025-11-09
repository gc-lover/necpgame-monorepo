package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
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
 * ModerationRuleSet
 */


public class ModerationRuleSet {

  @Valid
  private List<String> bannedWords = new ArrayList<>();

  @Valid
  private List<String> severeViolations = new ArrayList<>();

  @Valid
  private List<URI> urlWhitelist = new ArrayList<>();

  private @Nullable Integer capsThreshold;

  private @Nullable Integer repeatCharLimit;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public ModerationRuleSet() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ModerationRuleSet(List<String> bannedWords) {
    this.bannedWords = bannedWords;
  }

  public ModerationRuleSet bannedWords(List<String> bannedWords) {
    this.bannedWords = bannedWords;
    return this;
  }

  public ModerationRuleSet addBannedWordsItem(String bannedWordsItem) {
    if (this.bannedWords == null) {
      this.bannedWords = new ArrayList<>();
    }
    this.bannedWords.add(bannedWordsItem);
    return this;
  }

  /**
   * Get bannedWords
   * @return bannedWords
   */
  @NotNull 
  @Schema(name = "bannedWords", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bannedWords")
  public List<String> getBannedWords() {
    return bannedWords;
  }

  public void setBannedWords(List<String> bannedWords) {
    this.bannedWords = bannedWords;
  }

  public ModerationRuleSet severeViolations(List<String> severeViolations) {
    this.severeViolations = severeViolations;
    return this;
  }

  public ModerationRuleSet addSevereViolationsItem(String severeViolationsItem) {
    if (this.severeViolations == null) {
      this.severeViolations = new ArrayList<>();
    }
    this.severeViolations.add(severeViolationsItem);
    return this;
  }

  /**
   * Get severeViolations
   * @return severeViolations
   */
  
  @Schema(name = "severeViolations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severeViolations")
  public List<String> getSevereViolations() {
    return severeViolations;
  }

  public void setSevereViolations(List<String> severeViolations) {
    this.severeViolations = severeViolations;
  }

  public ModerationRuleSet urlWhitelist(List<URI> urlWhitelist) {
    this.urlWhitelist = urlWhitelist;
    return this;
  }

  public ModerationRuleSet addUrlWhitelistItem(URI urlWhitelistItem) {
    if (this.urlWhitelist == null) {
      this.urlWhitelist = new ArrayList<>();
    }
    this.urlWhitelist.add(urlWhitelistItem);
    return this;
  }

  /**
   * Get urlWhitelist
   * @return urlWhitelist
   */
  @Valid 
  @Schema(name = "urlWhitelist", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("urlWhitelist")
  public List<URI> getUrlWhitelist() {
    return urlWhitelist;
  }

  public void setUrlWhitelist(List<URI> urlWhitelist) {
    this.urlWhitelist = urlWhitelist;
  }

  public ModerationRuleSet capsThreshold(@Nullable Integer capsThreshold) {
    this.capsThreshold = capsThreshold;
    return this;
  }

  /**
   * Get capsThreshold
   * @return capsThreshold
   */
  
  @Schema(name = "capsThreshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capsThreshold")
  public @Nullable Integer getCapsThreshold() {
    return capsThreshold;
  }

  public void setCapsThreshold(@Nullable Integer capsThreshold) {
    this.capsThreshold = capsThreshold;
  }

  public ModerationRuleSet repeatCharLimit(@Nullable Integer repeatCharLimit) {
    this.repeatCharLimit = repeatCharLimit;
    return this;
  }

  /**
   * Get repeatCharLimit
   * @return repeatCharLimit
   */
  
  @Schema(name = "repeatCharLimit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("repeatCharLimit")
  public @Nullable Integer getRepeatCharLimit() {
    return repeatCharLimit;
  }

  public void setRepeatCharLimit(@Nullable Integer repeatCharLimit) {
    this.repeatCharLimit = repeatCharLimit;
  }

  public ModerationRuleSet updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
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
    ModerationRuleSet moderationRuleSet = (ModerationRuleSet) o;
    return Objects.equals(this.bannedWords, moderationRuleSet.bannedWords) &&
        Objects.equals(this.severeViolations, moderationRuleSet.severeViolations) &&
        Objects.equals(this.urlWhitelist, moderationRuleSet.urlWhitelist) &&
        Objects.equals(this.capsThreshold, moderationRuleSet.capsThreshold) &&
        Objects.equals(this.repeatCharLimit, moderationRuleSet.repeatCharLimit) &&
        Objects.equals(this.updatedAt, moderationRuleSet.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bannedWords, severeViolations, urlWhitelist, capsThreshold, repeatCharLimit, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModerationRuleSet {\n");
    sb.append("    bannedWords: ").append(toIndentedString(bannedWords)).append("\n");
    sb.append("    severeViolations: ").append(toIndentedString(severeViolations)).append("\n");
    sb.append("    urlWhitelist: ").append(toIndentedString(urlWhitelist)).append("\n");
    sb.append("    capsThreshold: ").append(toIndentedString(capsThreshold)).append("\n");
    sb.append("    repeatCharLimit: ").append(toIndentedString(repeatCharLimit)).append("\n");
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

