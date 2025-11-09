package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AttachmentMetadata
 */


public class AttachmentMetadata {

  private @Nullable String attachmentId;

  private @Nullable String filename;

  private @Nullable String mimeType;

  private @Nullable Integer sizeBytes;

  private @Nullable String uploadedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime uploadedAt;

  public AttachmentMetadata attachmentId(@Nullable String attachmentId) {
    this.attachmentId = attachmentId;
    return this;
  }

  /**
   * Get attachmentId
   * @return attachmentId
   */
  
  @Schema(name = "attachmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachmentId")
  public @Nullable String getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(@Nullable String attachmentId) {
    this.attachmentId = attachmentId;
  }

  public AttachmentMetadata filename(@Nullable String filename) {
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

  public AttachmentMetadata mimeType(@Nullable String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Get mimeType
   * @return mimeType
   */
  
  @Schema(name = "mimeType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mimeType")
  public @Nullable String getMimeType() {
    return mimeType;
  }

  public void setMimeType(@Nullable String mimeType) {
    this.mimeType = mimeType;
  }

  public AttachmentMetadata sizeBytes(@Nullable Integer sizeBytes) {
    this.sizeBytes = sizeBytes;
    return this;
  }

  /**
   * Get sizeBytes
   * @return sizeBytes
   */
  
  @Schema(name = "sizeBytes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sizeBytes")
  public @Nullable Integer getSizeBytes() {
    return sizeBytes;
  }

  public void setSizeBytes(@Nullable Integer sizeBytes) {
    this.sizeBytes = sizeBytes;
  }

  public AttachmentMetadata uploadedBy(@Nullable String uploadedBy) {
    this.uploadedBy = uploadedBy;
    return this;
  }

  /**
   * Get uploadedBy
   * @return uploadedBy
   */
  
  @Schema(name = "uploadedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uploadedBy")
  public @Nullable String getUploadedBy() {
    return uploadedBy;
  }

  public void setUploadedBy(@Nullable String uploadedBy) {
    this.uploadedBy = uploadedBy;
  }

  public AttachmentMetadata uploadedAt(@Nullable OffsetDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
    return this;
  }

  /**
   * Get uploadedAt
   * @return uploadedAt
   */
  @Valid 
  @Schema(name = "uploadedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uploadedAt")
  public @Nullable OffsetDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(@Nullable OffsetDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachmentMetadata attachmentMetadata = (AttachmentMetadata) o;
    return Objects.equals(this.attachmentId, attachmentMetadata.attachmentId) &&
        Objects.equals(this.filename, attachmentMetadata.filename) &&
        Objects.equals(this.mimeType, attachmentMetadata.mimeType) &&
        Objects.equals(this.sizeBytes, attachmentMetadata.sizeBytes) &&
        Objects.equals(this.uploadedBy, attachmentMetadata.uploadedBy) &&
        Objects.equals(this.uploadedAt, attachmentMetadata.uploadedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentId, filename, mimeType, sizeBytes, uploadedBy, uploadedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentMetadata {\n");
    sb.append("    attachmentId: ").append(toIndentedString(attachmentId)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    sizeBytes: ").append(toIndentedString(sizeBytes)).append("\n");
    sb.append("    uploadedBy: ").append(toIndentedString(uploadedBy)).append("\n");
    sb.append("    uploadedAt: ").append(toIndentedString(uploadedAt)).append("\n");
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

