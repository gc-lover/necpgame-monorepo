package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * PlayerOrderAttachment
 */


public class PlayerOrderAttachment {

  private UUID attachmentId;

  private String name;

  private URI url;

  private String mimeType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime uploadedAt;

  public PlayerOrderAttachment() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderAttachment(UUID attachmentId, String name, URI url, String mimeType) {
    this.attachmentId = attachmentId;
    this.name = name;
    this.url = url;
    this.mimeType = mimeType;
  }

  public PlayerOrderAttachment attachmentId(UUID attachmentId) {
    this.attachmentId = attachmentId;
    return this;
  }

  /**
   * Get attachmentId
   * @return attachmentId
   */
  @NotNull @Valid 
  @Schema(name = "attachmentId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("attachmentId")
  public UUID getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(UUID attachmentId) {
    this.attachmentId = attachmentId;
  }

  public PlayerOrderAttachment name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlayerOrderAttachment url(URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   */
  @NotNull @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("url")
  public URI getUrl() {
    return url;
  }

  public void setUrl(URI url) {
    this.url = url;
  }

  public PlayerOrderAttachment mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Get mimeType
   * @return mimeType
   */
  @NotNull 
  @Schema(name = "mimeType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mimeType")
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public PlayerOrderAttachment uploadedAt(@Nullable OffsetDateTime uploadedAt) {
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
    PlayerOrderAttachment playerOrderAttachment = (PlayerOrderAttachment) o;
    return Objects.equals(this.attachmentId, playerOrderAttachment.attachmentId) &&
        Objects.equals(this.name, playerOrderAttachment.name) &&
        Objects.equals(this.url, playerOrderAttachment.url) &&
        Objects.equals(this.mimeType, playerOrderAttachment.mimeType) &&
        Objects.equals(this.uploadedAt, playerOrderAttachment.uploadedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentId, name, url, mimeType, uploadedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderAttachment {\n");
    sb.append("    attachmentId: ").append(toIndentedString(attachmentId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
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

