package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.SocialBroadcastMemePayload;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * SocialBroadcast
 */


public class SocialBroadcast {

  private UUID eventId;

  /**
   * Gets or Sets channel
   */
  public enum ChannelEnum {
    CITY_BILLBOARD("CITY_BILLBOARD"),
    
    NIGHT_HUB("NIGHT_HUB"),
    
    HOLOCAST("HOLOCAST"),
    
    SOCIAL_FEED("SOCIAL_FEED");

    private final String value;

    ChannelEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ChannelEnum fromValue(String value) {
      for (ChannelEnum b : ChannelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ChannelEnum channel;

  private String message;

  private @Nullable SocialBroadcastMemePayload memePayload;

  @Valid
  private List<String> audienceSegments = new ArrayList<>();

  public SocialBroadcast() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialBroadcast(UUID eventId, ChannelEnum channel, String message) {
    this.eventId = eventId;
    this.channel = channel;
    this.message = message;
  }

  public SocialBroadcast eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull @Valid 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public SocialBroadcast channel(ChannelEnum channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  @NotNull 
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channel")
  public ChannelEnum getChannel() {
    return channel;
  }

  public void setChannel(ChannelEnum channel) {
    this.channel = channel;
  }

  public SocialBroadcast message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public SocialBroadcast memePayload(@Nullable SocialBroadcastMemePayload memePayload) {
    this.memePayload = memePayload;
    return this;
  }

  /**
   * Get memePayload
   * @return memePayload
   */
  @Valid 
  @Schema(name = "memePayload", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memePayload")
  public @Nullable SocialBroadcastMemePayload getMemePayload() {
    return memePayload;
  }

  public void setMemePayload(@Nullable SocialBroadcastMemePayload memePayload) {
    this.memePayload = memePayload;
  }

  public SocialBroadcast audienceSegments(List<String> audienceSegments) {
    this.audienceSegments = audienceSegments;
    return this;
  }

  public SocialBroadcast addAudienceSegmentsItem(String audienceSegmentsItem) {
    if (this.audienceSegments == null) {
      this.audienceSegments = new ArrayList<>();
    }
    this.audienceSegments.add(audienceSegmentsItem);
    return this;
  }

  /**
   * Get audienceSegments
   * @return audienceSegments
   */
  
  @Schema(name = "audienceSegments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audienceSegments")
  public List<String> getAudienceSegments() {
    return audienceSegments;
  }

  public void setAudienceSegments(List<String> audienceSegments) {
    this.audienceSegments = audienceSegments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialBroadcast socialBroadcast = (SocialBroadcast) o;
    return Objects.equals(this.eventId, socialBroadcast.eventId) &&
        Objects.equals(this.channel, socialBroadcast.channel) &&
        Objects.equals(this.message, socialBroadcast.message) &&
        Objects.equals(this.memePayload, socialBroadcast.memePayload) &&
        Objects.equals(this.audienceSegments, socialBroadcast.audienceSegments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, channel, message, memePayload, audienceSegments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialBroadcast {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    memePayload: ").append(toIndentedString(memePayload)).append("\n");
    sb.append("    audienceSegments: ").append(toIndentedString(audienceSegments)).append("\n");
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

