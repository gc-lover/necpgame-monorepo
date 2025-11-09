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
 * ChatCommandRequestContext
 */

@JsonTypeName("ChatCommandRequest_context")

public class ChatCommandRequestContext {

  private @Nullable String channelType;

  private @Nullable String zoneId;

  private @Nullable Boolean isVoice;

  public ChatCommandRequestContext channelType(@Nullable String channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelType")
  public @Nullable String getChannelType() {
    return channelType;
  }

  public void setChannelType(@Nullable String channelType) {
    this.channelType = channelType;
  }

  public ChatCommandRequestContext zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zoneId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zoneId")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public ChatCommandRequestContext isVoice(@Nullable Boolean isVoice) {
    this.isVoice = isVoice;
    return this;
  }

  /**
   * Get isVoice
   * @return isVoice
   */
  
  @Schema(name = "isVoice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isVoice")
  public @Nullable Boolean getIsVoice() {
    return isVoice;
  }

  public void setIsVoice(@Nullable Boolean isVoice) {
    this.isVoice = isVoice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatCommandRequestContext chatCommandRequestContext = (ChatCommandRequestContext) o;
    return Objects.equals(this.channelType, chatCommandRequestContext.channelType) &&
        Objects.equals(this.zoneId, chatCommandRequestContext.zoneId) &&
        Objects.equals(this.isVoice, chatCommandRequestContext.isVoice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelType, zoneId, isVoice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatCommandRequestContext {\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    isVoice: ").append(toIndentedString(isVoice)).append("\n");
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

