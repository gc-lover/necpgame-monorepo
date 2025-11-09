package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LeaderboardEntryGuild
 */

@JsonTypeName("LeaderboardEntry_guild")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LeaderboardEntryGuild {

  private @Nullable UUID guildId;

  private @Nullable String guildName;

  private @Nullable String guildTag;

  public LeaderboardEntryGuild guildId(@Nullable UUID guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @Valid 
  @Schema(name = "guild_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_id")
  public @Nullable UUID getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable UUID guildId) {
    this.guildId = guildId;
  }

  public LeaderboardEntryGuild guildName(@Nullable String guildName) {
    this.guildName = guildName;
    return this;
  }

  /**
   * Get guildName
   * @return guildName
   */
  
  @Schema(name = "guild_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_name")
  public @Nullable String getGuildName() {
    return guildName;
  }

  public void setGuildName(@Nullable String guildName) {
    this.guildName = guildName;
  }

  public LeaderboardEntryGuild guildTag(@Nullable String guildTag) {
    this.guildTag = guildTag;
    return this;
  }

  /**
   * Get guildTag
   * @return guildTag
   */
  
  @Schema(name = "guild_tag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_tag")
  public @Nullable String getGuildTag() {
    return guildTag;
  }

  public void setGuildTag(@Nullable String guildTag) {
    this.guildTag = guildTag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaderboardEntryGuild leaderboardEntryGuild = (LeaderboardEntryGuild) o;
    return Objects.equals(this.guildId, leaderboardEntryGuild.guildId) &&
        Objects.equals(this.guildName, leaderboardEntryGuild.guildName) &&
        Objects.equals(this.guildTag, leaderboardEntryGuild.guildTag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, guildName, guildTag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaderboardEntryGuild {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    guildName: ").append(toIndentedString(guildName)).append("\n");
    sb.append("    guildTag: ").append(toIndentedString(guildTag)).append("\n");
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

