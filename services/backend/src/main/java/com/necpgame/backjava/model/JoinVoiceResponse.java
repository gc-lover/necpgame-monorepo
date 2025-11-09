package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.IceServer;
import com.necpgame.backjava.model.VoiceChannel;
import com.necpgame.backjava.model.VoiceParticipant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * JoinVoiceResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class JoinVoiceResponse {

  private String token;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime expiresAt;

  private VoiceChannel channel;

  @Valid
  private List<@Valid IceServer> iceServers = new ArrayList<>();

  @Valid
  private List<@Valid VoiceParticipant> participants = new ArrayList<>();

  public JoinVoiceResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public JoinVoiceResponse(String token, OffsetDateTime expiresAt, VoiceChannel channel) {
    this.token = token;
    this.expiresAt = expiresAt;
    this.channel = channel;
  }

  public JoinVoiceResponse token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   */
  @NotNull 
  @Schema(name = "token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public JoinVoiceResponse expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @NotNull @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expiresAt")
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public JoinVoiceResponse channel(VoiceChannel channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  @NotNull @Valid 
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channel")
  public VoiceChannel getChannel() {
    return channel;
  }

  public void setChannel(VoiceChannel channel) {
    this.channel = channel;
  }

  public JoinVoiceResponse iceServers(List<@Valid IceServer> iceServers) {
    this.iceServers = iceServers;
    return this;
  }

  public JoinVoiceResponse addIceServersItem(IceServer iceServersItem) {
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

  public JoinVoiceResponse participants(List<@Valid VoiceParticipant> participants) {
    this.participants = participants;
    return this;
  }

  public JoinVoiceResponse addParticipantsItem(VoiceParticipant participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<@Valid VoiceParticipant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid VoiceParticipant> participants) {
    this.participants = participants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JoinVoiceResponse joinVoiceResponse = (JoinVoiceResponse) o;
    return Objects.equals(this.token, joinVoiceResponse.token) &&
        Objects.equals(this.expiresAt, joinVoiceResponse.expiresAt) &&
        Objects.equals(this.channel, joinVoiceResponse.channel) &&
        Objects.equals(this.iceServers, joinVoiceResponse.iceServers) &&
        Objects.equals(this.participants, joinVoiceResponse.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, expiresAt, channel, iceServers, participants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinVoiceResponse {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    iceServers: ").append(toIndentedString(iceServers)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
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

