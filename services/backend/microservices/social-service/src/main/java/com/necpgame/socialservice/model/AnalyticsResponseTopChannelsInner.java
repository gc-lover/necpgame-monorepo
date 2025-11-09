package com.necpgame.socialservice.model;

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
 * AnalyticsResponseTopChannelsInner
 */

@JsonTypeName("AnalyticsResponse_topChannels_inner")

public class AnalyticsResponseTopChannelsInner {

  private @Nullable String channel;

  private @Nullable Integer completed;

  public AnalyticsResponseTopChannelsInner channel(@Nullable String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel")
  public @Nullable String getChannel() {
    return channel;
  }

  public void setChannel(@Nullable String channel) {
    this.channel = channel;
  }

  public AnalyticsResponseTopChannelsInner completed(@Nullable Integer completed) {
    this.completed = completed;
    return this;
  }

  /**
   * Get completed
   * @return completed
   */
  
  @Schema(name = "completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed")
  public @Nullable Integer getCompleted() {
    return completed;
  }

  public void setCompleted(@Nullable Integer completed) {
    this.completed = completed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponseTopChannelsInner analyticsResponseTopChannelsInner = (AnalyticsResponseTopChannelsInner) o;
    return Objects.equals(this.channel, analyticsResponseTopChannelsInner.channel) &&
        Objects.equals(this.completed, analyticsResponseTopChannelsInner.completed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channel, completed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponseTopChannelsInner {\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
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

