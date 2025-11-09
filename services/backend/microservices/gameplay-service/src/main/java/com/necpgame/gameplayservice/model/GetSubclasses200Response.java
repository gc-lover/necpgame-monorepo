package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Subclass;
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
 * GetSubclasses200Response
 */

@JsonTypeName("getSubclasses_200_response")

public class GetSubclasses200Response {

  private @Nullable String classId;

  @Valid
  private List<@Valid Subclass> subclasses = new ArrayList<>();

  public GetSubclasses200Response classId(@Nullable String classId) {
    this.classId = classId;
    return this;
  }

  /**
   * Get classId
   * @return classId
   */
  
  @Schema(name = "class_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_id")
  public @Nullable String getClassId() {
    return classId;
  }

  public void setClassId(@Nullable String classId) {
    this.classId = classId;
  }

  public GetSubclasses200Response subclasses(List<@Valid Subclass> subclasses) {
    this.subclasses = subclasses;
    return this;
  }

  public GetSubclasses200Response addSubclassesItem(Subclass subclassesItem) {
    if (this.subclasses == null) {
      this.subclasses = new ArrayList<>();
    }
    this.subclasses.add(subclassesItem);
    return this;
  }

  /**
   * Get subclasses
   * @return subclasses
   */
  @Valid 
  @Schema(name = "subclasses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subclasses")
  public List<@Valid Subclass> getSubclasses() {
    return subclasses;
  }

  public void setSubclasses(List<@Valid Subclass> subclasses) {
    this.subclasses = subclasses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSubclasses200Response getSubclasses200Response = (GetSubclasses200Response) o;
    return Objects.equals(this.classId, getSubclasses200Response.classId) &&
        Objects.equals(this.subclasses, getSubclasses200Response.subclasses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, subclasses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSubclasses200Response {\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    subclasses: ").append(toIndentedString(subclasses)).append("\n");
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

