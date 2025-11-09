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
 * ExecuteCommand200Response
 */

@JsonTypeName("executeCommand_200_response")

public class ExecuteCommand200Response {

  private @Nullable Boolean success;

  private @Nullable String result;

  private @Nullable Boolean messageSent;

  public ExecuteCommand200Response success(@Nullable Boolean success) {
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

  public ExecuteCommand200Response result(@Nullable String result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  
  @Schema(name = "result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result")
  public @Nullable String getResult() {
    return result;
  }

  public void setResult(@Nullable String result) {
    this.result = result;
  }

  public ExecuteCommand200Response messageSent(@Nullable Boolean messageSent) {
    this.messageSent = messageSent;
    return this;
  }

  /**
   * Get messageSent
   * @return messageSent
   */
  
  @Schema(name = "message_sent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message_sent")
  public @Nullable Boolean getMessageSent() {
    return messageSent;
  }

  public void setMessageSent(@Nullable Boolean messageSent) {
    this.messageSent = messageSent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecuteCommand200Response executeCommand200Response = (ExecuteCommand200Response) o;
    return Objects.equals(this.success, executeCommand200Response.success) &&
        Objects.equals(this.result, executeCommand200Response.result) &&
        Objects.equals(this.messageSent, executeCommand200Response.messageSent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, result, messageSent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecuteCommand200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    messageSent: ").append(toIndentedString(messageSent)).append("\n");
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

