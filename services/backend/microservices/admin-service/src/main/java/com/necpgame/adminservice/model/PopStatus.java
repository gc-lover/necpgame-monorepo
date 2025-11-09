package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PopStatus
 */


public class PopStatus {

  private @Nullable String popId;

  private @Nullable String location;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    UP("up"),
    
    DOWN("down");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable BigDecimal requestsPerSecond;

  public PopStatus popId(@Nullable String popId) {
    this.popId = popId;
    return this;
  }

  /**
   * Get popId
   * @return popId
   */
  
  @Schema(name = "pop_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pop_id")
  public @Nullable String getPopId() {
    return popId;
  }

  public void setPopId(@Nullable String popId) {
    this.popId = popId;
  }

  public PopStatus location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public PopStatus status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public PopStatus requestsPerSecond(@Nullable BigDecimal requestsPerSecond) {
    this.requestsPerSecond = requestsPerSecond;
    return this;
  }

  /**
   * Get requestsPerSecond
   * @return requestsPerSecond
   */
  @Valid 
  @Schema(name = "requests_per_second", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requests_per_second")
  public @Nullable BigDecimal getRequestsPerSecond() {
    return requestsPerSecond;
  }

  public void setRequestsPerSecond(@Nullable BigDecimal requestsPerSecond) {
    this.requestsPerSecond = requestsPerSecond;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PopStatus popStatus = (PopStatus) o;
    return Objects.equals(this.popId, popStatus.popId) &&
        Objects.equals(this.location, popStatus.location) &&
        Objects.equals(this.status, popStatus.status) &&
        Objects.equals(this.requestsPerSecond, popStatus.requestsPerSecond);
  }

  @Override
  public int hashCode() {
    return Objects.hash(popId, location, status, requestsPerSecond);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PopStatus {\n");
    sb.append("    popId: ").append(toIndentedString(popId)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    requestsPerSecond: ").append(toIndentedString(requestsPerSecond)).append("\n");
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

