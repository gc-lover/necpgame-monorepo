package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.authservice.model.GameCharacterClass;
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
 * GetCharacterClasses200Response
 */

@JsonTypeName("getCharacterClasses_200_response")

public class GetCharacterClasses200Response {

  @Valid
  private List<@Valid GameCharacterClass> classes = new ArrayList<>();

  public GetCharacterClasses200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetCharacterClasses200Response(List<@Valid GameCharacterClass> classes) {
    this.classes = classes;
  }

  public GetCharacterClasses200Response classes(List<@Valid GameCharacterClass> classes) {
    this.classes = classes;
    return this;
  }

  public GetCharacterClasses200Response addClassesItem(GameCharacterClass classesItem) {
    if (this.classes == null) {
      this.classes = new ArrayList<>();
    }
    this.classes.add(classesItem);
    return this;
  }

  /**
   * Список доступных классов
   * @return classes
   */
  @NotNull @Valid 
  @Schema(name = "classes", description = "Список доступных классов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("classes")
  public List<@Valid GameCharacterClass> getClasses() {
    return classes;
  }

  public void setClasses(List<@Valid GameCharacterClass> classes) {
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
    GetCharacterClasses200Response getCharacterClasses200Response = (GetCharacterClasses200Response) o;
    return Objects.equals(this.classes, getCharacterClasses200Response.classes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacterClasses200Response {\n");
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

