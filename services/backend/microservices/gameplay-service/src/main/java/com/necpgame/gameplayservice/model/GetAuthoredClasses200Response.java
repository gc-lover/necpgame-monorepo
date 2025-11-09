package com.necpgame.gameplayservice.model;

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
 * GetAuthoredClasses200Response
 */

@JsonTypeName("getAuthoredClasses_200_response")

public class GetAuthoredClasses200Response {

  @Valid
  private List<Object> classes = new ArrayList<>();

  public GetAuthoredClasses200Response classes(List<Object> classes) {
    this.classes = classes;
    return this;
  }

  public GetAuthoredClasses200Response addClassesItem(Object classesItem) {
    if (this.classes == null) {
      this.classes = new ArrayList<>();
    }
    this.classes.add(classesItem);
    return this;
  }

  /**
   * Get classes
   * @return classes
   */
  
  @Schema(name = "classes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("classes")
  public List<Object> getClasses() {
    return classes;
  }

  public void setClasses(List<Object> classes) {
    this.classes = classes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAuthoredClasses200Response getAuthoredClasses200Response = (GetAuthoredClasses200Response) o;
    return Objects.equals(this.classes, getAuthoredClasses200Response.classes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAuthoredClasses200Response {\n");
    sb.append("    classes: ").append(toIndentedString(classes)).append("\n");
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

