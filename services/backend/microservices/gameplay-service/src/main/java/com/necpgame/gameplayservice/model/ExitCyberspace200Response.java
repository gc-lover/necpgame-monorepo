package com.necpgame.gameplayservice.model;

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
 * ExitCyberspace200Response
 */

@JsonTypeName("exitCyberspace_200_response")

public class ExitCyberspace200Response {

  private @Nullable Boolean success;

  private @Nullable String realWorldLocation;

  public ExitCyberspace200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public ExitCyberspace200Response realWorldLocation(@Nullable String realWorldLocation) {
    this.realWorldLocation = realWorldLocation;
    return this;
  }

  /**
   * Get realWorldLocation
   * @return realWorldLocation
   */
  
  @Schema(name = "real_world_location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("real_world_location")
  public @Nullable String getRealWorldLocation() {
    return realWorldLocation;
  }

  public void setRealWorldLocation(@Nullable String realWorldLocation) {
    this.realWorldLocation = realWorldLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExitCyberspace200Response exitCyberspace200Response = (ExitCyberspace200Response) o;
    return Objects.equals(this.success, exitCyberspace200Response.success) &&
        Objects.equals(this.realWorldLocation, exitCyberspace200Response.realWorldLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, realWorldLocation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExitCyberspace200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    realWorldLocation: ").append(toIndentedString(realWorldLocation)).append("\n");
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

