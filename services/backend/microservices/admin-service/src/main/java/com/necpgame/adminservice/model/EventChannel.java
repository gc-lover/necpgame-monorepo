package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.EventChannelEventTypesInner;
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
 * EventChannel
 */


public class EventChannel {

  private @Nullable String channelId;

  private @Nullable String channelName;

  private @Nullable String description;

  @Valid
  private List<String> publishers = new ArrayList<>();

  @Valid
  private List<String> subscribers = new ArrayList<>();

  @Valid
  private List<@Valid EventChannelEventTypesInner> eventTypes = new ArrayList<>();

  public EventChannel channelId(@Nullable String channelId) {
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

  public EventChannel channelName(@Nullable String channelName) {
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

  public EventChannel description(@Nullable String description) {
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

  public EventChannel publishers(List<String> publishers) {
    this.publishers = publishers;
    return this;
  }

  public EventChannel addPublishersItem(String publishersItem) {
    if (this.publishers == null) {
      this.publishers = new ArrayList<>();
    }
    this.publishers.add(publishersItem);
    return this;
  }

  /**
   * Сервисы-publishers
   * @return publishers
   */
  
  @Schema(name = "publishers", description = "Сервисы-publishers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publishers")
  public List<String> getPublishers() {
    return publishers;
  }

  public void setPublishers(List<String> publishers) {
    this.publishers = publishers;
  }

  public EventChannel subscribers(List<String> subscribers) {
    this.subscribers = subscribers;
    return this;
  }

  public EventChannel addSubscribersItem(String subscribersItem) {
    if (this.subscribers == null) {
      this.subscribers = new ArrayList<>();
    }
    this.subscribers.add(subscribersItem);
    return this;
  }

  /**
   * Сервисы-subscribers
   * @return subscribers
   */
  
  @Schema(name = "subscribers", description = "Сервисы-subscribers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subscribers")
  public List<String> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(List<String> subscribers) {
    this.subscribers = subscribers;
  }

  public EventChannel eventTypes(List<@Valid EventChannelEventTypesInner> eventTypes) {
    this.eventTypes = eventTypes;
    return this;
  }

  public EventChannel addEventTypesItem(EventChannelEventTypesInner eventTypesItem) {
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
  @Valid 
  @Schema(name = "event_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_types")
  public List<@Valid EventChannelEventTypesInner> getEventTypes() {
    return eventTypes;
  }

  public void setEventTypes(List<@Valid EventChannelEventTypesInner> eventTypes) {
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
    EventChannel eventChannel = (EventChannel) o;
    return Objects.equals(this.channelId, eventChannel.channelId) &&
        Objects.equals(this.channelName, eventChannel.channelName) &&
        Objects.equals(this.description, eventChannel.description) &&
        Objects.equals(this.publishers, eventChannel.publishers) &&
        Objects.equals(this.subscribers, eventChannel.subscribers) &&
        Objects.equals(this.eventTypes, eventChannel.eventTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, channelName, description, publishers, subscribers, eventTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventChannel {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    publishers: ").append(toIndentedString(publishers)).append("\n");
    sb.append("    subscribers: ").append(toIndentedString(subscribers)).append("\n");
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

