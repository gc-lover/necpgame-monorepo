package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.IceServer;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * VoiceJoinResponse
 */


public class VoiceJoinResponse {

  private UUID voiceSessionId;

  private String sdpAnswer;

  @Valid
  private List<@Valid IceServer> iceServers = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public VoiceJoinResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceJoinResponse(UUID voiceSessionId, String sdpAnswer) {
    this.voiceSessionId = voiceSessionId;
    this.sdpAnswer = sdpAnswer;
  }

  public VoiceJoinResponse voiceSessionId(UUID voiceSessionId) {
    this.voiceSessionId = voiceSessionId;
    return this;
  }

  /**
   * Get voiceSessionId
   * @return voiceSessionId
   */
  @NotNull @Valid 
  @Schema(name = "voiceSessionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("voiceSessionId")
  public UUID getVoiceSessionId() {
    return voiceSessionId;
  }

  public void setVoiceSessionId(UUID voiceSessionId) {
    this.voiceSessionId = voiceSessionId;
  }

  public VoiceJoinResponse sdpAnswer(String sdpAnswer) {
    this.sdpAnswer = sdpAnswer;
    return this;
  }

  /**
   * Get sdpAnswer
   * @return sdpAnswer
   */
  @NotNull 
  @Schema(name = "sdpAnswer", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sdpAnswer")
  public String getSdpAnswer() {
    return sdpAnswer;
  }

  public void setSdpAnswer(String sdpAnswer) {
    this.sdpAnswer = sdpAnswer;
  }

  public VoiceJoinResponse iceServers(List<@Valid IceServer> iceServers) {
    this.iceServers = iceServers;
    return this;
  }

  public VoiceJoinResponse addIceServersItem(IceServer iceServersItem) {
    if (this.iceServers == null) {
      this.iceServers = new ArrayList<>();
    }
    this.iceServers.add(iceServersItem);
    return this;
  }

  /**
   * Get iceServers
   * @return iceServers
   */
  @Valid 
  @Schema(name = "iceServers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("iceServers")
  public List<@Valid IceServer> getIceServers() {
    return iceServers;
  }

  public void setIceServers(List<@Valid IceServer> iceServers) {
    this.iceServers = iceServers;
  }

  public VoiceJoinResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceJoinResponse voiceJoinResponse = (VoiceJoinResponse) o;
    return Objects.equals(this.voiceSessionId, voiceJoinResponse.voiceSessionId) &&
        Objects.equals(this.sdpAnswer, voiceJoinResponse.sdpAnswer) &&
        Objects.equals(this.iceServers, voiceJoinResponse.iceServers) &&
        Objects.equals(this.expiresAt, voiceJoinResponse.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(voiceSessionId, sdpAnswer, iceServers, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceJoinResponse {\n");
    sb.append("    voiceSessionId: ").append(toIndentedString(voiceSessionId)).append("\n");
    sb.append("    sdpAnswer: ").append(toIndentedString(sdpAnswer)).append("\n");
    sb.append("    iceServers: ").append(toIndentedString(iceServers)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

