package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GuildLeaderboardEntry;
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
 * GuildLeaderboardResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildLeaderboardResponse {

  private @Nullable String category;

  @Valid
  private List<@Valid GuildLeaderboardEntry> entries = new ArrayList<>();

  private @Nullable Integer totalGuilds;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public GuildLeaderboardResponse category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public GuildLeaderboardResponse entries(List<@Valid GuildLeaderboardEntry> entries) {
    this.entries = entries;
    return this;
  }

  public GuildLeaderboardResponse addEntriesItem(GuildLeaderboardEntry entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @Valid 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entries")
  public List<@Valid GuildLeaderboardEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid GuildLeaderboardEntry> entries) {
    this.entries = entries;
  }

  public GuildLeaderboardResponse totalGuilds(@Nullable Integer totalGuilds) {
    this.totalGuilds = totalGuilds;
    return this;
  }

  /**
   * Get totalGuilds
   * @return totalGuilds
   */
  
  @Schema(name = "total_guilds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_guilds")
  public @Nullable Integer getTotalGuilds() {
    return totalGuilds;
  }

  public void setTotalGuilds(@Nullable Integer totalGuilds) {
    this.totalGuilds = totalGuilds;
  }

  public GuildLeaderboardResponse updatedAt(@Nullable OffsetDateTime updatedAt) {
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
    GuildLeaderboardResponse guildLeaderboardResponse = (GuildLeaderboardResponse) o;
    return Objects.equals(this.category, guildLeaderboardResponse.category) &&
        Objects.equals(this.entries, guildLeaderboardResponse.entries) &&
        Objects.equals(this.totalGuilds, guildLeaderboardResponse.totalGuilds) &&
        Objects.equals(this.updatedAt, guildLeaderboardResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, entries, totalGuilds, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildLeaderboardResponse {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
    sb.append("    totalGuilds: ").append(toIndentedString(totalGuilds)).append("\n");
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

