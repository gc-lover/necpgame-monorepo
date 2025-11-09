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
 * AcceptMentorship200Response
 */

@JsonTypeName("acceptMentorship_200_response")

public class AcceptMentorship200Response {

  private @Nullable String mentorshipId;

  public AcceptMentorship200Response mentorshipId(@Nullable String mentorshipId) {
    this.mentorshipId = mentorshipId;
    return this;
  }

  /**
   * Get mentorshipId
   * @return mentorshipId
   */
  
  @Schema(name = "mentorship_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mentorship_id")
  public @Nullable String getMentorshipId() {
    return mentorshipId;
  }

  public void setMentorshipId(@Nullable String mentorshipId) {
    this.mentorshipId = mentorshipId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptMentorship200Response acceptMentorship200Response = (AcceptMentorship200Response) o;
    return Objects.equals(this.mentorshipId, acceptMentorship200Response.mentorshipId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mentorshipId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptMentorship200Response {\n");
    sb.append("    mentorshipId: ").append(toIndentedString(mentorshipId)).append("\n");
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

