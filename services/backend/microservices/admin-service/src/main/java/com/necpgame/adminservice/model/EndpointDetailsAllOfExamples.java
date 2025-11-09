package com.necpgame.adminservice.model;

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
 * EndpointDetailsAllOfExamples
 */

@JsonTypeName("EndpointDetails_allOf_examples")

public class EndpointDetailsAllOfExamples {

  private @Nullable Object request;

  private @Nullable Object response;

  public EndpointDetailsAllOfExamples request(@Nullable Object request) {
    this.request = request;
    return this;
  }

  /**
   * Get request
   * @return request
   */
  
  @Schema(name = "request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("request")
  public @Nullable Object getRequest() {
    return request;
  }

  public void setRequest(@Nullable Object request) {
    this.request = request;
  }

  public EndpointDetailsAllOfExamples response(@Nullable Object response) {
    this.response = response;
    return this;
  }

  /**
   * Get response
   * @return response
   */
  
  @Schema(name = "response", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("response")
  public @Nullable Object getResponse() {
    return response;
  }

  public void setResponse(@Nullable Object response) {
    this.response = response;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointDetailsAllOfExamples endpointDetailsAllOfExamples = (EndpointDetailsAllOfExamples) o;
    return Objects.equals(this.request, endpointDetailsAllOfExamples.request) &&
        Objects.equals(this.response, endpointDetailsAllOfExamples.response);
  }

  @Override
  public int hashCode() {
    return Objects.hash(request, response);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointDetailsAllOfExamples {\n");
    sb.append("    request: ").append(toIndentedString(request)).append("\n");
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
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

