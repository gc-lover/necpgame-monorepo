package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.LobbyParticipant;
import com.necpgame.socialservice.model.Subchannel;
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
 * JoinLobbyResponse
 */


public class JoinLobbyResponse {

  private @Nullable LobbyParticipant participant;

  @Valid
  private List<@Valid Subchannel> subchannels = new ArrayList<>();

  public JoinLobbyResponse participant(@Nullable LobbyParticipant participant) {
    this.participant = participant;
    return this;
  }

  /**
   * Get participant
   * @return participant
   */
  @Valid 
  @Schema(name = "participant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participant")
  public @Nullable LobbyParticipant getParticipant() {
    return participant;
  }

  public void setParticipant(@Nullable LobbyParticipant participant) {
    this.participant = participant;
  }

  public JoinLobbyResponse subchannels(List<@Valid Subchannel> subchannels) {
    this.subchannels = subchannels;
    return this;
  }

  public JoinLobbyResponse addSubchannelsItem(Subchannel subchannelsItem) {
    if (this.subchannels == null) {
      this.subchannels = new ArrayList<>();
    }
    this.subchannels.add(subchannelsItem);
    return this;
  }

  /**
   * Get subchannels
   * @return subchannels
   */
  @Valid 
  @Schema(name = "subchannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subchannels")
  public List<@Valid Subchannel> getSubchannels() {
    return subchannels;
  }

  public void setSubchannels(List<@Valid Subchannel> subchannels) {
    this.subchannels = subchannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JoinLobbyResponse joinLobbyResponse = (JoinLobbyResponse) o;
    return Objects.equals(this.participant, joinLobbyResponse.participant) &&
        Objects.equals(this.subchannels, joinLobbyResponse.subchannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participant, subchannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinLobbyResponse {\n");
    sb.append("    participant: ").append(toIndentedString(participant)).append("\n");
    sb.append("    subchannels: ").append(toIndentedString(subchannels)).append("\n");
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

