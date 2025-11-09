package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ModerationCheckRequest
 */


public class ModerationCheckRequest {

  private String text;

  private String channelType;

  private @Nullable UUID playerId;

  public ModerationCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ModerationCheckRequest(String text, String channelType) {
    this.text = text;
    this.channelType = channelType;
  }

  public ModerationCheckRequest text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  @NotNull @Size(max = 2000) 
  @Schema(name = "text", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public ModerationCheckRequest channelType(String channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  @NotNull 
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelType")
  public String getChannelType() {
    return channelType;
  }

  public void setChannelType(String channelType) {
    this.channelType = channelType;
  }

  public ModerationCheckRequest playerId(@Nullable UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable UUID playerId) {
    this.playerId = playerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModerationCheckRequest moderationCheckRequest = (ModerationCheckRequest) o;
    return Objects.equals(this.text, moderationCheckRequest.text) &&
        Objects.equals(this.channelType, moderationCheckRequest.channelType) &&
        Objects.equals(this.playerId, moderationCheckRequest.playerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, channelType, playerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModerationCheckRequest {\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
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

