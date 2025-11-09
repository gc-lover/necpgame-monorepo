package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * LeaveVoiceChannelRequest
 */

@JsonTypeName("leaveVoiceChannel_request")

public class LeaveVoiceChannelRequest {

  private UUID voiceSessionId;

  public LeaveVoiceChannelRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LeaveVoiceChannelRequest(UUID voiceSessionId) {
    this.voiceSessionId = voiceSessionId;
  }

  public LeaveVoiceChannelRequest voiceSessionId(UUID voiceSessionId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaveVoiceChannelRequest leaveVoiceChannelRequest = (LeaveVoiceChannelRequest) o;
    return Objects.equals(this.voiceSessionId, leaveVoiceChannelRequest.voiceSessionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(voiceSessionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaveVoiceChannelRequest {\n");
    sb.append("    voiceSessionId: ").append(toIndentedString(voiceSessionId)).append("\n");
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

