package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EndpointInfoParametersInner
 */

@JsonTypeName("EndpointInfo_parameters_inner")

public class EndpointInfoParametersInner {

  private @Nullable String name;

  /**
   * Gets or Sets in
   */
  public enum InEnum {
    PATH("path"),
    
    QUERY("query"),
    
    HEADER("header"),
    
    BODY("body");

    private final String value;

    InEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InEnum fromValue(String value) {
      for (InEnum b : InEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable InEnum in;

  private @Nullable Boolean required;

  private @Nullable String type;

  public EndpointInfoParametersInner name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public EndpointInfoParametersInner in(@Nullable InEnum in) {
    this.in = in;
    return this;
  }

  /**
   * Get in
   * @return in
   */
  
  @Schema(name = "in", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("in")
  public @Nullable InEnum getIn() {
    return in;
  }

  public void setIn(@Nullable InEnum in) {
    this.in = in;
  }

  public EndpointInfoParametersInner required(@Nullable Boolean required) {
    this.required = required;
    return this;
  }

  /**
   * Get required
   * @return required
   */
  
  @Schema(name = "required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required")
  public @Nullable Boolean getRequired() {
    return required;
  }

  public void setRequired(@Nullable Boolean required) {
    this.required = required;
  }

  public EndpointInfoParametersInner type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointInfoParametersInner endpointInfoParametersInner = (EndpointInfoParametersInner) o;
    return Objects.equals(this.name, endpointInfoParametersInner.name) &&
        Objects.equals(this.in, endpointInfoParametersInner.in) &&
        Objects.equals(this.required, endpointInfoParametersInner.required) &&
        Objects.equals(this.type, endpointInfoParametersInner.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, in, required, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointInfoParametersInner {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    in: ").append(toIndentedString(in)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

