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
 * AcceptMentorshipRequest
 */

@JsonTypeName("acceptMentorship_request")

public class AcceptMentorshipRequest {

  private String requestId;

  public AcceptMentorshipRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AcceptMentorshipRequest(String requestId) {
    this.requestId = requestId;
  }

  public AcceptMentorshipRequest requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Get requestId
   * @return requestId
   */
  @NotNull 
  @Schema(name = "request_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("request_id")
  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptMentorshipRequest acceptMentorshipRequest = (AcceptMentorshipRequest) o;
    return Objects.equals(this.requestId, acceptMentorshipRequest.requestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptMentorshipRequest {\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
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

