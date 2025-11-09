package com.necpgame.socialservice.model;

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
 * InteractWithNPC200Response
 */

@JsonTypeName("interactWithNPC_200_response")

public class InteractWithNPC200Response {

  private @Nullable Boolean success;

  private @Nullable Object result;

  public InteractWithNPC200Response success(@Nullable Boolean success) {
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

  public InteractWithNPC200Response result(@Nullable Object result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  
  @Schema(name = "result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result")
  public @Nullable Object getResult() {
    return result;
  }

  public void setResult(@Nullable Object result) {
    this.result = result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InteractWithNPC200Response interactWithNPC200Response = (InteractWithNPC200Response) o;
    return Objects.equals(this.success, interactWithNPC200Response.success) &&
        Objects.equals(this.result, interactWithNPC200Response.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, result);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InteractWithNPC200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
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

