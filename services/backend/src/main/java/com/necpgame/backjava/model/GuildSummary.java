package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildSummary
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildSummary {

  private @Nullable String guildId;

  private @Nullable String name;

  private @Nullable String tag;

  private @Nullable Integer level;

  private @Nullable Integer members;

  private @Nullable String shard;

  private @Nullable URI emblemUrl;

  public GuildSummary guildId(@Nullable String guildId) {
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

  public GuildSummary name(@Nullable String name) {
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

  public GuildSummary tag(@Nullable String tag) {
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

  public GuildSummary level(@Nullable Integer level) {
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

  public GuildSummary members(@Nullable Integer members) {
    this.members = members;
    return this;
  }

  /**
   * Get members
   * @return members
   */
  
  @Schema(name = "members", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("members")
  public @Nullable Integer getMembers() {
    return members;
  }

  public void setMembers(@Nullable Integer members) {
    this.members = members;
  }

  public GuildSummary shard(@Nullable String shard) {
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

  public GuildSummary emblemUrl(@Nullable URI emblemUrl) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildSummary guildSummary = (GuildSummary) o;
    return Objects.equals(this.guildId, guildSummary.guildId) &&
        Objects.equals(this.name, guildSummary.name) &&
        Objects.equals(this.tag, guildSummary.tag) &&
        Objects.equals(this.level, guildSummary.level) &&
        Objects.equals(this.members, guildSummary.members) &&
        Objects.equals(this.shard, guildSummary.shard) &&
        Objects.equals(this.emblemUrl, guildSummary.emblemUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, name, tag, level, members, shard, emblemUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildSummary {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
    sb.append("    shard: ").append(toIndentedString(shard)).append("\n");
    sb.append("    emblemUrl: ").append(toIndentedString(emblemUrl)).append("\n");
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

