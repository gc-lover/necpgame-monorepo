package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MaintenanceAuditEntryAttachmentsInner
 */

@JsonTypeName("MaintenanceAuditEntry_attachments_inner")

public class MaintenanceAuditEntryAttachmentsInner {

  private @Nullable UUID attachmentId;

  private @Nullable String filename;

  private @Nullable URI url;

  public MaintenanceAuditEntryAttachmentsInner attachmentId(@Nullable UUID attachmentId) {
    this.attachmentId = attachmentId;
    return this;
  }

  /**
   * Get attachmentId
   * @return attachmentId
   */
  @Valid 
  @Schema(name = "attachmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachmentId")
  public @Nullable UUID getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(@Nullable UUID attachmentId) {
    this.attachmentId = attachmentId;
  }

  public MaintenanceAuditEntryAttachmentsInner filename(@Nullable String filename) {
    this.filename = filename;
    return this;
  }

  /**
   * Get filename
   * @return filename
   */
  
  @Schema(name = "filename", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filename")
  public @Nullable String getFilename() {
    return filename;
  }

  public void setFilename(@Nullable String filename) {
    this.filename = filename;
  }

  public MaintenanceAuditEntryAttachmentsInner url(@Nullable URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   */
  @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("url")
  public @Nullable URI getUrl() {
    return url;
  }

  public void setUrl(@Nullable URI url) {
    this.url = url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceAuditEntryAttachmentsInner maintenanceAuditEntryAttachmentsInner = (MaintenanceAuditEntryAttachmentsInner) o;
    return Objects.equals(this.attachmentId, maintenanceAuditEntryAttachmentsInner.attachmentId) &&
        Objects.equals(this.filename, maintenanceAuditEntryAttachmentsInner.filename) &&
        Objects.equals(this.url, maintenanceAuditEntryAttachmentsInner.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentId, filename, url);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceAuditEntryAttachmentsInner {\n");
    sb.append("    attachmentId: ").append(toIndentedString(attachmentId)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

