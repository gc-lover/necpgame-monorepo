package com.necpgame.backjava.model;

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
 * GuildDetail
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildDetail {

  private @Nullable String guildId;

  private @Nullable String name;

  private @Nullable String tag;

  private @Nullable String description;

  private @Nullable String language;

  private @Nullable String policy;

  private @Nullable String playstyle;

  private @Nullable Integer level;

  private @Nullable Integer xp;

  @Valid
  private List<String> perksUnlocked = new ArrayList<>();

  private @Nullable String shard;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable URI emblemUrl;

  @Valid
  private List<String> officers = new ArrayList<>();

  public GuildDetail guildId(@Nullable String guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildId")
  public @Nullable String getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable String guildId) {
    this.guildId = guildId;
  }

  public GuildDetail name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public GuildDetail tag(@Nullable String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Get tag
   * @return tag
   */
  
  @Schema(name = "tag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tag")
  public @Nullable String getTag() {
    return tag;
  }

  public void setTag(@Nullable String tag) {
    this.tag = tag;
  }

  public GuildDetail description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GuildDetail language(@Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  
  @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("language")
  public @Nullable String getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable String language) {
    this.language = language;
  }

  public GuildDetail policy(@Nullable String policy) {
    this.policy = policy;
    return this;
  }

  /**
   * Get policy
   * @return policy
   */
  
  @Schema(name = "policy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("policy")
  public @Nullable String getPolicy() {
    return policy;
  }

  public void setPolicy(@Nullable String policy) {
    this.policy = policy;
  }

  public GuildDetail playstyle(@Nullable String playstyle) {
    this.playstyle = playstyle;
    return this;
  }

  /**
   * Get playstyle
   * @return playstyle
   */
  
  @Schema(name = "playstyle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playstyle")
  public @Nullable String getPlaystyle() {
    return playstyle;
  }

  public void setPlaystyle(@Nullable String playstyle) {
    this.playstyle = playstyle;
  }

  public GuildDetail level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public GuildDetail xp(@Nullable Integer xp) {
    this.xp = xp;
    return this;
  }

  /**
   * Get xp
   * @return xp
   */
  
  @Schema(name = "xp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xp")
  public @Nullable Integer getXp() {
    return xp;
  }

  public void setXp(@Nullable Integer xp) {
    this.xp = xp;
  }

  public GuildDetail perksUnlocked(List<String> perksUnlocked) {
    this.perksUnlocked = perksUnlocked;
    return this;
  }

  public GuildDetail addPerksUnlockedItem(String perksUnlockedItem) {
    if (this.perksUnlocked == null) {
      this.perksUnlocked = new ArrayList<>();
    }
    this.perksUnlocked.add(perksUnlockedItem);
    return this;
  }

  /**
   * Get perksUnlocked
   * @return perksUnlocked
   */
  
  @Schema(name = "perksUnlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perksUnlocked")
  public List<String> getPerksUnlocked() {
    return perksUnlocked;
  }

  public void setPerksUnlocked(List<String> perksUnlocked) {
    this.perksUnlocked = perksUnlocked;
  }

  public GuildDetail shard(@Nullable String shard) {
    this.shard = shard;
    return this;
  }

  /**
   * Get shard
   * @return shard
   */
  
  @Schema(name = "shard", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shard")
  public @Nullable String getShard() {
    return shard;
  }

  public void setShard(@Nullable String shard) {
    this.shard = shard;
  }

  public GuildDetail createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public GuildDetail emblemUrl(@Nullable URI emblemUrl) {
    this.emblemUrl = emblemUrl;
    return this;
  }

  /**
   * Get emblemUrl
   * @return emblemUrl
   */
  @Valid 
  @Schema(name = "emblemUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emblemUrl")
  public @Nullable URI getEmblemUrl() {
    return emblemUrl;
  }

  public void setEmblemUrl(@Nullable URI emblemUrl) {
    this.emblemUrl = emblemUrl;
  }

  public GuildDetail officers(List<String> officers) {
    this.officers = officers;
    return this;
  }

  public GuildDetail addOfficersItem(String officersItem) {
    if (this.officers == null) {
      this.officers = new ArrayList<>();
    }
    this.officers.add(officersItem);
    return this;
  }

  /**
   * Get officers
   * @return officers
   */
  
  @Schema(name = "officers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("officers")
  public List<String> getOfficers() {
    return officers;
  }

  public void setOfficers(List<String> officers) {
    this.officers = officers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildDetail guildDetail = (GuildDetail) o;
    return Objects.equals(this.guildId, guildDetail.guildId) &&
        Objects.equals(this.name, guildDetail.name) &&
        Objects.equals(this.tag, guildDetail.tag) &&
        Objects.equals(this.description, guildDetail.description) &&
        Objects.equals(this.language, guildDetail.language) &&
        Objects.equals(this.policy, guildDetail.policy) &&
        Objects.equals(this.playstyle, guildDetail.playstyle) &&
        Objects.equals(this.level, guildDetail.level) &&
        Objects.equals(this.xp, guildDetail.xp) &&
        Objects.equals(this.perksUnlocked, guildDetail.perksUnlocked) &&
        Objects.equals(this.shard, guildDetail.shard) &&
        Objects.equals(this.createdAt, guildDetail.createdAt) &&
        Objects.equals(this.emblemUrl, guildDetail.emblemUrl) &&
        Objects.equals(this.officers, guildDetail.officers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, name, tag, description, language, policy, playstyle, level, xp, perksUnlocked, shard, createdAt, emblemUrl, officers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildDetail {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    playstyle: ").append(toIndentedString(playstyle)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    xp: ").append(toIndentedString(xp)).append("\n");
    sb.append("    perksUnlocked: ").append(toIndentedString(perksUnlocked)).append("\n");
    sb.append("    shard: ").append(toIndentedString(shard)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    emblemUrl: ").append(toIndentedString(emblemUrl)).append("\n");
    sb.append("    officers: ").append(toIndentedString(officers)).append("\n");
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

