package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SurrenderRequestVotesInner
 */

@JsonTypeName("SurrenderRequest_votes_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SurrenderRequestVotesInner {

  private @Nullable String participantId;

  /**
   * Gets or Sets vote
   */
  public enum VoteEnum {
    TRUE("true"),
    
    FALSE("false"),
    
    ABSTAIN("ABSTAIN");

    private final String value;

    VoteEnum(String value) {
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
    public static VoteEnum fromValue(String value) {
      for (VoteEnum b : VoteEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VoteEnum vote;

  public SurrenderRequestVotesInner participantId(@Nullable String participantId) {
    this.participantId = participantId;
    return this;
  }

  /**
   * Get participantId
   * @return participantId
   */
  
  @Schema(name = "participantId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participantId")
  public @Nullable String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(@Nullable String participantId) {
    this.participantId = participantId;
  }

  public SurrenderRequestVotesInner vote(@Nullable VoteEnum vote) {
    this.vote = vote;
    return this;
  }

  /**
   * Get vote
   * @return vote
   */
  
  @Schema(name = "vote", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vote")
  public @Nullable VoteEnum getVote() {
    return vote;
  }

  public void setVote(@Nullable VoteEnum vote) {
    this.vote = vote;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurrenderRequestVotesInner surrenderRequestVotesInner = (SurrenderRequestVotesInner) o;
    return Objects.equals(this.participantId, surrenderRequestVotesInner.participantId) &&
        Objects.equals(this.vote, surrenderRequestVotesInner.vote);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participantId, vote);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurrenderRequestVotesInner {\n");
    sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");
    sb.append("    vote: ").append(toIndentedString(vote)).append("\n");
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

