package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.EventChannel;
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
 * GetEventChannels200Response
 */

@JsonTypeName("getEventChannels_200_response")

public class GetEventChannels200Response {

  @Valid
  private List<@Valid EventChannel> channels = new ArrayList<>();

  private @Nullable Integer totalChannels;

  public GetEventChannels200Response channels(List<@Valid EventChannel> channels) {
    this.channels = channels;
    return this;
  }

  public GetEventChannels200Response addChannelsItem(EventChannel channelsItem) {
    if (this.channels == null) {
      this.channels = new ArrayList<>();
    }
    this.channels.add(channelsItem);
    return this;
  }

  /**
   * Get channels
   * @return channels
   */
  @Valid 
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channels")
  public List<@Valid EventChannel> getChannels() {
    return channels;
  }

  public void setChannels(List<@Valid EventChannel> channels) {
    this.channels = channels;
  }

  public GetEventChannels200Response totalChannels(@Nullable Integer totalChannels) {
    this.totalChannels = totalChannels;
    return this;
  }

  /**
   * Get totalChannels
   * @return totalChannels
   */
  
  @Schema(name = "total_channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_channels")
  public @Nullable Integer getTotalChannels() {
    return totalChannels;
  }

  public void setTotalChannels(@Nullable Integer totalChannels) {
    this.totalChannels = totalChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEventChannels200Response getEventChannels200Response = (GetEventChannels200Response) o;
    return Objects.equals(this.channels, getEventChannels200Response.channels) &&
        Objects.equals(this.totalChannels, getEventChannels200Response.totalChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, totalChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEventChannels200Response {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    totalChannels: ").append(toIndentedString(totalChannels)).append("\n");
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

