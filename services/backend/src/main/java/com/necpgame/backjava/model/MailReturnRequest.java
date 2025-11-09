package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MailReturnRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MailReturnRequest {

  private @Nullable String reason;

  private Boolean includeAttachments = true;

  public MailReturnRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public MailReturnRequest includeAttachments(Boolean includeAttachments) {
    this.includeAttachments = includeAttachments;
    return this;
  }

  /**
   * Get includeAttachments
   * @return includeAttachments
   */
  
  @Schema(name = "includeAttachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeAttachments")
  public Boolean getIncludeAttachments() {
    return includeAttachments;
  }

  public void setIncludeAttachments(Boolean includeAttachments) {
    this.includeAttachments = includeAttachments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailReturnRequest mailReturnRequest = (MailReturnRequest) o;
    return Objects.equals(this.reason, mailReturnRequest.reason) &&
        Objects.equals(this.includeAttachments, mailReturnRequest.includeAttachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, includeAttachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailReturnRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    includeAttachments: ").append(toIndentedString(includeAttachments)).append("\n");
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

