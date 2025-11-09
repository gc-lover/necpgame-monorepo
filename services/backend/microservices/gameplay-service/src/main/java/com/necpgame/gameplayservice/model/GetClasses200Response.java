package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.GameClass;
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
 * GetClasses200Response
 */

@JsonTypeName("getClasses_200_response")

public class GetClasses200Response {

  @Valid
  private List<@Valid GameClass> classes = new ArrayList<>();

  private @Nullable Integer total;

  public GetClasses200Response classes(List<@Valid GameClass> classes) {
    this.classes = classes;
    return this;
  }

  public GetClasses200Response addClassesItem(GameClass classesItem) {
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
  @Valid 
  @Schema(name = "classes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("classes")
  public List<@Valid GameClass> getClasses() {
    return classes;
  }

  public void setClasses(List<@Valid GameClass> classes) {
    this.classes = classes;
  }

  public GetClasses200Response total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetClasses200Response getClasses200Response = (GetClasses200Response) o;
    return Objects.equals(this.classes, getClasses200Response.classes) &&
        Objects.equals(this.total, getClasses200Response.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classes, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetClasses200Response {\n");
    sb.append("    classes: ").append(toIndentedString(classes)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

