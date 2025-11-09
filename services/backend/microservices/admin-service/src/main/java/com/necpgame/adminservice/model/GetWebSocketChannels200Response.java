package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.GetWebSocketChannels200ResponseChannelsInner;
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
 * GetWebSocketChannels200Response
 */

@JsonTypeName("getWebSocketChannels_200_response")

public class GetWebSocketChannels200Response {

  @Valid
  private List<@Valid GetWebSocketChannels200ResponseChannelsInner> channels = new ArrayList<>();

  public GetWebSocketChannels200Response channels(List<@Valid GetWebSocketChannels200ResponseChannelsInner> channels) {
    this.channels = channels;
    return this;
  }

  public GetWebSocketChannels200Response addChannelsItem(GetWebSocketChannels200ResponseChannelsInner channelsItem) {
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
  public List<@Valid GetWebSocketChannels200ResponseChannelsInner> getChannels() {
    return channels;
  }

  public void setChannels(List<@Valid GetWebSocketChannels200ResponseChannelsInner> channels) {
    this.channels = channels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetWebSocketChannels200Response getWebSocketChannels200Response = (GetWebSocketChannels200Response) o;
    return Objects.equals(this.channels, getWebSocketChannels200Response.channels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetWebSocketChannels200Response {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
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

