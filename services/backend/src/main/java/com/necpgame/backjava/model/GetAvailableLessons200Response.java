package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.Lesson;
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
 * GetAvailableLessons200Response
 */

@JsonTypeName("getAvailableLessons_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetAvailableLessons200Response {

  @Valid
  private List<@Valid Lesson> lessons = new ArrayList<>();

  public GetAvailableLessons200Response lessons(List<@Valid Lesson> lessons) {
    this.lessons = lessons;
    return this;
  }

  public GetAvailableLessons200Response addLessonsItem(Lesson lessonsItem) {
    if (this.lessons == null) {
      this.lessons = new ArrayList<>();
    }
    this.lessons.add(lessonsItem);
    return this;
  }

  /**
   * Get lessons
   * @return lessons
   */
  @Valid 
  @Schema(name = "lessons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lessons")
  public List<@Valid Lesson> getLessons() {
    return lessons;
  }

  public void setLessons(List<@Valid Lesson> lessons) {
    this.lessons = lessons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableLessons200Response getAvailableLessons200Response = (GetAvailableLessons200Response) o;
    return Objects.equals(this.lessons, getAvailableLessons200Response.lessons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lessons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableLessons200Response {\n");
    sb.append("    lessons: ").append(toIndentedString(lessons)).append("\n");
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

