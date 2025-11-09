package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.MentorshipRelationship;
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
 * GetMentorshipHistory200Response
 */

@JsonTypeName("getMentorshipHistory_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetMentorshipHistory200Response {

  @Valid
  private List<@Valid MentorshipRelationship> asStudent = new ArrayList<>();

  @Valid
  private List<@Valid MentorshipRelationship> asMentor = new ArrayList<>();

  public GetMentorshipHistory200Response asStudent(List<@Valid MentorshipRelationship> asStudent) {
    this.asStudent = asStudent;
    return this;
  }

  public GetMentorshipHistory200Response addAsStudentItem(MentorshipRelationship asStudentItem) {
    if (this.asStudent == null) {
      this.asStudent = new ArrayList<>();
    }
    this.asStudent.add(asStudentItem);
    return this;
  }

  /**
   * Get asStudent
   * @return asStudent
   */
  @Valid 
  @Schema(name = "as_student", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("as_student")
  public List<@Valid MentorshipRelationship> getAsStudent() {
    return asStudent;
  }

  public void setAsStudent(List<@Valid MentorshipRelationship> asStudent) {
    this.asStudent = asStudent;
  }

  public GetMentorshipHistory200Response asMentor(List<@Valid MentorshipRelationship> asMentor) {
    this.asMentor = asMentor;
    return this;
  }

  public GetMentorshipHistory200Response addAsMentorItem(MentorshipRelationship asMentorItem) {
    if (this.asMentor == null) {
      this.asMentor = new ArrayList<>();
    }
    this.asMentor.add(asMentorItem);
    return this;
  }

  /**
   * Get asMentor
   * @return asMentor
   */
  @Valid 
  @Schema(name = "as_mentor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("as_mentor")
  public List<@Valid MentorshipRelationship> getAsMentor() {
    return asMentor;
  }

  public void setAsMentor(List<@Valid MentorshipRelationship> asMentor) {
    this.asMentor = asMentor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMentorshipHistory200Response getMentorshipHistory200Response = (GetMentorshipHistory200Response) o;
    return Objects.equals(this.asStudent, getMentorshipHistory200Response.asStudent) &&
        Objects.equals(this.asMentor, getMentorshipHistory200Response.asMentor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(asStudent, asMentor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMentorshipHistory200Response {\n");
    sb.append("    asStudent: ").append(toIndentedString(asStudent)).append("\n");
    sb.append("    asMentor: ").append(toIndentedString(asMentor)).append("\n");
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

