package com.necpgame.backjava.model;

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
 * ExecuteStateOperation200Response
 */

@JsonTypeName("executeStateOperation_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExecuteStateOperation200Response {

  private @Nullable Boolean success;

  private @Nullable Integer newVersion;

  public ExecuteStateOperation200Response success(@Nullable Boolean success) {
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

  public ExecuteStateOperation200Response newVersion(@Nullable Integer newVersion) {
    this.newVersion = newVersion;
    return this;
  }

  /**
   * Get newVersion
   * @return newVersion
   */
  
  @Schema(name = "new_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_version")
  public @Nullable Integer getNewVersion() {
    return newVersion;
  }

  public void setNewVersion(@Nullable Integer newVersion) {
    this.newVersion = newVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecuteStateOperation200Response executeStateOperation200Response = (ExecuteStateOperation200Response) o;
    return Objects.equals(this.success, executeStateOperation200Response.success) &&
        Objects.equals(this.newVersion, executeStateOperation200Response.newVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, newVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecuteStateOperation200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    newVersion: ").append(toIndentedString(newVersion)).append("\n");
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

