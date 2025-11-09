package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.VoiceChannelType;
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
 * VoiceJoinRequest
 */


public class VoiceJoinRequest {

  private VoiceChannelType channelType;

  private String channelId;

  private String sdpOffer;

  @Valid
  private List<String> deviceCapabilities = new ArrayList<>();

  private @Nullable String resumeToken;

  public VoiceJoinRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceJoinRequest(VoiceChannelType channelType, String channelId, String sdpOffer) {
    this.channelType = channelType;
    this.channelId = channelId;
    this.sdpOffer = sdpOffer;
  }

  public VoiceJoinRequest channelType(VoiceChannelType channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  @NotNull @Valid 
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelType")
  public VoiceChannelType getChannelType() {
    return channelType;
  }

  public void setChannelType(VoiceChannelType channelType) {
    this.channelType = channelType;
  }

  public VoiceJoinRequest channelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  @NotNull 
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelId")
  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public VoiceJoinRequest sdpOffer(String sdpOffer) {
    this.sdpOffer = sdpOffer;
    return this;
  }

  /**
   * Get sdpOffer
   * @return sdpOffer
   */
  @NotNull 
  @Schema(name = "sdpOffer", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sdpOffer")
  public String getSdpOffer() {
    return sdpOffer;
  }

  public void setSdpOffer(String sdpOffer) {
    this.sdpOffer = sdpOffer;
  }

  public VoiceJoinRequest deviceCapabilities(List<String> deviceCapabilities) {
    this.deviceCapabilities = deviceCapabilities;
    return this;
  }

  public VoiceJoinRequest addDeviceCapabilitiesItem(String deviceCapabilitiesItem) {
    if (this.deviceCapabilities == null) {
      this.deviceCapabilities = new ArrayList<>();
    }
    this.deviceCapabilities.add(deviceCapabilitiesItem);
    return this;
  }

  /**
   * Get deviceCapabilities
   * @return deviceCapabilities
   */
  
  @Schema(name = "deviceCapabilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deviceCapabilities")
  public List<String> getDeviceCapabilities() {
    return deviceCapabilities;
  }

  public void setDeviceCapabilities(List<String> deviceCapabilities) {
    this.deviceCapabilities = deviceCapabilities;
  }

  public VoiceJoinRequest resumeToken(@Nullable String resumeToken) {
    this.resumeToken = resumeToken;
    return this;
  }

  /**
   * Get resumeToken
   * @return resumeToken
   */
  
  @Schema(name = "resumeToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resumeToken")
  public @Nullable String getResumeToken() {
    return resumeToken;
  }

  public void setResumeToken(@Nullable String resumeToken) {
    this.resumeToken = resumeToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceJoinRequest voiceJoinRequest = (VoiceJoinRequest) o;
    return Objects.equals(this.channelType, voiceJoinRequest.channelType) &&
        Objects.equals(this.channelId, voiceJoinRequest.channelId) &&
        Objects.equals(this.sdpOffer, voiceJoinRequest.sdpOffer) &&
        Objects.equals(this.deviceCapabilities, voiceJoinRequest.deviceCapabilities) &&
        Objects.equals(this.resumeToken, voiceJoinRequest.resumeToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelType, channelId, sdpOffer, deviceCapabilities, resumeToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceJoinRequest {\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    sdpOffer: ").append(toIndentedString(sdpOffer)).append("\n");
    sb.append("    deviceCapabilities: ").append(toIndentedString(deviceCapabilities)).append("\n");
    sb.append("    resumeToken: ").append(toIndentedString(resumeToken)).append("\n");
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

