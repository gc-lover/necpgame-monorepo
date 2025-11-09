package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.VoiceParticipantsResponseParticipantsInner;
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
 * VoiceParticipantsResponse
 */


public class VoiceParticipantsResponse {

  @Valid
  private List<@Valid VoiceParticipantsResponseParticipantsInner> participants = new ArrayList<>();

  public VoiceParticipantsResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceParticipantsResponse(List<@Valid VoiceParticipantsResponseParticipantsInner> participants) {
    this.participants = participants;
  }

  public VoiceParticipantsResponse participants(List<@Valid VoiceParticipantsResponseParticipantsInner> participants) {
    this.participants = participants;
    return this;
  }

  public VoiceParticipantsResponse addParticipantsItem(VoiceParticipantsResponseParticipantsInner participantsItem) {
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
  @NotNull @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("participants")
  public List<@Valid VoiceParticipantsResponseParticipantsInner> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid VoiceParticipantsResponseParticipantsInner> participants) {
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
    VoiceParticipantsResponse voiceParticipantsResponse = (VoiceParticipantsResponse) o;
    return Objects.equals(this.participants, voiceParticipantsResponse.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceParticipantsResponse {\n");
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

