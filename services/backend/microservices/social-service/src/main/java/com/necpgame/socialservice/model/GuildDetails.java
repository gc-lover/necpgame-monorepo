package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * GuildDetails
 */


public class GuildDetails {

  private @Nullable String guildId;

  private @Nullable String name;

  private @Nullable String tag;

  private @Nullable Integer level;

  private @Nullable Integer memberCount;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @Valid
  private List<Object> members = new ArrayList<>();

  public GuildDetails guildId(@Nullable String guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  
  @Schema(name = "guild_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_id")
  public @Nullable String getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable String guildId) {
    this.guildId = guildId;
  }

  public GuildDetails name(@Nullable String name) {
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

  public GuildDetails tag(@Nullable String tag) {
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

  public GuildDetails level(@Nullable Integer level) {
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

  public GuildDetails memberCount(@Nullable Integer memberCount) {
    this.memberCount = memberCount;
    return this;
  }

  /**
   * Get memberCount
   * @return memberCount
   */
  
  @Schema(name = "member_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("member_count")
  public @Nullable Integer getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(@Nullable Integer memberCount) {
    this.memberCount = memberCount;
  }

  public GuildDetails createdAt(@Nullable OffsetDateTime createdAt) {
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

  public GuildDetails members(List<Object> members) {
    this.members = members;
    return this;
  }

  public GuildDetails addMembersItem(Object membersItem) {
    if (this.members == null) {
      this.members = new ArrayList<>();
    }
    this.members.add(membersItem);
    return this;
  }

  /**
   * Get members
   * @return members
   */
  
  @Schema(name = "members", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("members")
  public List<Object> getMembers() {
    return members;
  }

  public void setMembers(List<Object> members) {
    this.members = members;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildDetails guildDetails = (GuildDetails) o;
    return Objects.equals(this.guildId, guildDetails.guildId) &&
        Objects.equals(this.name, guildDetails.name) &&
        Objects.equals(this.tag, guildDetails.tag) &&
        Objects.equals(this.level, guildDetails.level) &&
        Objects.equals(this.memberCount, guildDetails.memberCount) &&
        Objects.equals(this.createdAt, guildDetails.createdAt) &&
        Objects.equals(this.members, guildDetails.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, name, tag, level, memberCount, createdAt, members);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildDetails {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    memberCount: ").append(toIndentedString(memberCount)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
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

