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
 * RequestMentorshipRequest
 */

@JsonTypeName("requestMentorship_request")

public class RequestMentorshipRequest {

  private String menteeId;

  private String mentorId;

  public RequestMentorshipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RequestMentorshipRequest(String menteeId, String mentorId) {
    this.menteeId = menteeId;
    this.mentorId = mentorId;
  }

  public RequestMentorshipRequest menteeId(String menteeId) {
    this.menteeId = menteeId;
    return this;
  }

  /**
   * Get menteeId
   * @return menteeId
   */
  @NotNull 
  @Schema(name = "mentee_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mentee_id")
  public String getMenteeId() {
    return menteeId;
  }

  public void setMenteeId(String menteeId) {
    this.menteeId = menteeId;
  }

  public RequestMentorshipRequest mentorId(String mentorId) {
    this.mentorId = mentorId;
    return this;
  }

  /**
   * Get mentorId
   * @return mentorId
   */
  @NotNull 
  @Schema(name = "mentor_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mentor_id")
  public String getMentorId() {
    return mentorId;
  }

  public void setMentorId(String mentorId) {
    this.mentorId = mentorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestMentorshipRequest requestMentorshipRequest = (RequestMentorshipRequest) o;
    return Objects.equals(this.menteeId, requestMentorshipRequest.menteeId) &&
        Objects.equals(this.mentorId, requestMentorshipRequest.mentorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(menteeId, mentorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestMentorshipRequest {\n");
    sb.append("    menteeId: ").append(toIndentedString(menteeId)).append("\n");
    sb.append("    mentorId: ").append(toIndentedString(mentorId)).append("\n");
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

