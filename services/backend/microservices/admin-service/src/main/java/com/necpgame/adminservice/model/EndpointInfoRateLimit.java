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
 * EndpointInfoRateLimit
 */

@JsonTypeName("EndpointInfo_rate_limit")

public class EndpointInfoRateLimit {

  private @Nullable Integer requests;

  private @Nullable String window;

  public EndpointInfoRateLimit requests(@Nullable Integer requests) {
    this.requests = requests;
    return this;
  }

  /**
   * Get requests
   * @return requests
   */
  
  @Schema(name = "requests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requests")
  public @Nullable Integer getRequests() {
    return requests;
  }

  public void setRequests(@Nullable Integer requests) {
    this.requests = requests;
  }

  public EndpointInfoRateLimit window(@Nullable String window) {
    this.window = window;
    return this;
  }

  /**
   * Get window
   * @return window
   */
  
  @Schema(name = "window", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("window")
  public @Nullable String getWindow() {
    return window;
  }

  public void setWindow(@Nullable String window) {
    this.window = window;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointInfoRateLimit endpointInfoRateLimit = (EndpointInfoRateLimit) o;
    return Objects.equals(this.requests, endpointInfoRateLimit.requests) &&
        Objects.equals(this.window, endpointInfoRateLimit.window);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requests, window);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointInfoRateLimit {\n");
    sb.append("    requests: ").append(toIndentedString(requests)).append("\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
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

