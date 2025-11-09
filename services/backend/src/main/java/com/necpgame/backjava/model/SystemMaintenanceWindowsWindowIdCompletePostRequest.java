package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SystemMaintenanceWindowsWindowIdCompletePostRequest
 */

@JsonTypeName("_system_maintenance_windows__windowId__complete_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SystemMaintenanceWindowsWindowIdCompletePostRequest {

  private @Nullable URI postmortemUrl;

  private @Nullable String summary;

  private @Nullable Boolean attachAudit;

  public SystemMaintenanceWindowsWindowIdCompletePostRequest postmortemUrl(@Nullable URI postmortemUrl) {
    this.postmortemUrl = postmortemUrl;
    return this;
  }

  /**
   * Get postmortemUrl
   * @return postmortemUrl
   */
  @Valid 
  @Schema(name = "postmortemUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("postmortemUrl")
  public @Nullable URI getPostmortemUrl() {
    return postmortemUrl;
  }

  public void setPostmortemUrl(@Nullable URI postmortemUrl) {
    this.postmortemUrl = postmortemUrl;
  }

  public SystemMaintenanceWindowsWindowIdCompletePostRequest summary(@Nullable String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public @Nullable String getSummary() {
    return summary;
  }

  public void setSummary(@Nullable String summary) {
    this.summary = summary;
  }

  public SystemMaintenanceWindowsWindowIdCompletePostRequest attachAudit(@Nullable Boolean attachAudit) {
    this.attachAudit = attachAudit;
    return this;
  }

  /**
   * Get attachAudit
   * @return attachAudit
   */
  
  @Schema(name = "attachAudit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachAudit")
  public @Nullable Boolean getAttachAudit() {
    return attachAudit;
  }

  public void setAttachAudit(@Nullable Boolean attachAudit) {
    this.attachAudit = attachAudit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMaintenanceWindowsWindowIdCompletePostRequest systemMaintenanceWindowsWindowIdCompletePostRequest = (SystemMaintenanceWindowsWindowIdCompletePostRequest) o;
    return Objects.equals(this.postmortemUrl, systemMaintenanceWindowsWindowIdCompletePostRequest.postmortemUrl) &&
        Objects.equals(this.summary, systemMaintenanceWindowsWindowIdCompletePostRequest.summary) &&
        Objects.equals(this.attachAudit, systemMaintenanceWindowsWindowIdCompletePostRequest.attachAudit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postmortemUrl, summary, attachAudit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMaintenanceWindowsWindowIdCompletePostRequest {\n");
    sb.append("    postmortemUrl: ").append(toIndentedString(postmortemUrl)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    attachAudit: ").append(toIndentedString(attachAudit)).append("\n");
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

