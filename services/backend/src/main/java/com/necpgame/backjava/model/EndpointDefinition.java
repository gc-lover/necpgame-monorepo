package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * EndpointDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EndpointDefinition {

  private @Nullable String endpoint;

  /**
   * Gets or Sets method
   */
  public enum MethodEnum {
    GET("GET"),
    
    POST("POST"),
    
    PUT("PUT"),
    
    DELETE("DELETE"),
    
    PATCH("PATCH");

    private final String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String value) {
      for (MethodEnum b : MethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MethodEnum method;

  private @Nullable String category;

  private @Nullable Boolean requiredForMvp;

  private @Nullable Boolean implemented;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    CRITICAL("CRITICAL"),
    
    HIGH("HIGH"),
    
    MEDIUM("MEDIUM"),
    
    LOW("LOW");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PriorityEnum priority;

  public EndpointDefinition endpoint(@Nullable String endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  /**
   * Get endpoint
   * @return endpoint
   */
  
  @Schema(name = "endpoint", example = "/api/v1/auth/login", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endpoint")
  public @Nullable String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(@Nullable String endpoint) {
    this.endpoint = endpoint;
  }

  public EndpointDefinition method(@Nullable MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * Get method
   * @return method
   */
  
  @Schema(name = "method", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("method")
  public @Nullable MethodEnum getMethod() {
    return method;
  }

  public void setMethod(@Nullable MethodEnum method) {
    this.method = method;
  }

  public EndpointDefinition category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", example = "Authentication", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public EndpointDefinition requiredForMvp(@Nullable Boolean requiredForMvp) {
    this.requiredForMvp = requiredForMvp;
    return this;
  }

  /**
   * Get requiredForMvp
   * @return requiredForMvp
   */
  
  @Schema(name = "required_for_mvp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_for_mvp")
  public @Nullable Boolean getRequiredForMvp() {
    return requiredForMvp;
  }

  public void setRequiredForMvp(@Nullable Boolean requiredForMvp) {
    this.requiredForMvp = requiredForMvp;
  }

  public EndpointDefinition implemented(@Nullable Boolean implemented) {
    this.implemented = implemented;
    return this;
  }

  /**
   * Get implemented
   * @return implemented
   */
  
  @Schema(name = "implemented", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implemented")
  public @Nullable Boolean getImplemented() {
    return implemented;
  }

  public void setImplemented(@Nullable Boolean implemented) {
    this.implemented = implemented;
  }

  public EndpointDefinition priority(@Nullable PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(@Nullable PriorityEnum priority) {
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointDefinition endpointDefinition = (EndpointDefinition) o;
    return Objects.equals(this.endpoint, endpointDefinition.endpoint) &&
        Objects.equals(this.method, endpointDefinition.method) &&
        Objects.equals(this.category, endpointDefinition.category) &&
        Objects.equals(this.requiredForMvp, endpointDefinition.requiredForMvp) &&
        Objects.equals(this.implemented, endpointDefinition.implemented) &&
        Objects.equals(this.priority, endpointDefinition.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, method, category, requiredForMvp, implemented, priority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointDefinition {\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    requiredForMvp: ").append(toIndentedString(requiredForMvp)).append("\n");
    sb.append("    implemented: ").append(toIndentedString(implemented)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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

