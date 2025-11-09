package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetAvailableMentors200Response
 */

@JsonTypeName("getAvailableMentors_200_response")

public class GetAvailableMentors200Response {

  @Valid
  private List<Object> mentors = new ArrayList<>();

  public GetAvailableMentors200Response mentors(List<Object> mentors) {
    this.mentors = mentors;
    return this;
  }

  public GetAvailableMentors200Response addMentorsItem(Object mentorsItem) {
    if (this.mentors == null) {
      this.mentors = new ArrayList<>();
    }
    this.mentors.add(mentorsItem);
    return this;
  }

  /**
   * Get mentors
   * @return mentors
   */
  
  @Schema(name = "mentors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mentors")
  public List<Object> getMentors() {
    return mentors;
  }

  public void setMentors(List<Object> mentors) {
    this.mentors = mentors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableMentors200Response getAvailableMentors200Response = (GetAvailableMentors200Response) o;
    return Objects.equals(this.mentors, getAvailableMentors200Response.mentors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mentors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableMentors200Response {\n");
    sb.append("    mentors: ").append(toIndentedString(mentors)).append("\n");
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

