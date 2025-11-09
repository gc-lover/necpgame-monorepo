package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetWebSocketChannels200ResponseChannelsInner
 */

@JsonTypeName("getWebSocketChannels_200_response_channels_inner")

public class GetWebSocketChannels200ResponseChannelsInner {

  private @Nullable String channelId;

  private @Nullable String channelName;

  private @Nullable String description;

  @Valid
  private List<String> eventTypes = new ArrayList<>();

  public GetWebSocketChannels200ResponseChannelsInner channelId(@Nullable String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  
  @Schema(name = "channel_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel_id")
  public @Nullable String getChannelId() {
    return channelId;
  }

  public void setChannelId(@Nullable String channelId) {
    this.channelId = channelId;
  }

  public GetWebSocketChannels200ResponseChannelsInner channelName(@Nullable String channelName) {
    this.channelName = channelName;
    return this;
  }

  /**
   * Get channelName
   * @return channelName
   */
  
  @Schema(name = "channel_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel_name")
  public @Nullable String getChannelName() {
    return channelName;
  }

  public void setChannelName(@Nullable String channelName) {
    this.channelName = channelName;
  }

  public GetWebSocketChannels200ResponseChannelsInner description(@Nullable String description) {
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

  public GetWebSocketChannels200ResponseChannelsInner eventTypes(List<String> eventTypes) {
    this.eventTypes = eventTypes;
    return this;
  }

  public GetWebSocketChannels200ResponseChannelsInner addEventTypesItem(String eventTypesItem) {
    if (this.eventTypes == null) {
      this.eventTypes = new ArrayList<>();
    }
    this.eventTypes.add(eventTypesItem);
    return this;
  }

  /**
   * Get eventTypes
   * @return eventTypes
   */
  
  @Schema(name = "event_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_types")
  public List<String> getEventTypes() {
    return eventTypes;
  }

  public void setEventTypes(List<String> eventTypes) {
    this.eventTypes = eventTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetWebSocketChannels200ResponseChannelsInner getWebSocketChannels200ResponseChannelsInner = (GetWebSocketChannels200ResponseChannelsInner) o;
    return Objects.equals(this.channelId, getWebSocketChannels200ResponseChannelsInner.channelId) &&
        Objects.equals(this.channelName, getWebSocketChannels200ResponseChannelsInner.channelName) &&
        Objects.equals(this.description, getWebSocketChannels200ResponseChannelsInner.description) &&
        Objects.equals(this.eventTypes, getWebSocketChannels200ResponseChannelsInner.eventTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, channelName, description, eventTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetWebSocketChannels200ResponseChannelsInner {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    eventTypes: ").append(toIndentedString(eventTypes)).append("\n");
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

