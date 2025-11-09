package com.necpgame.socialservice.model;

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
 * MessageMetadataAttachmentsInner
 */

@JsonTypeName("MessageMetadata_attachments_inner")

public class MessageMetadataAttachmentsInner {

  private @Nullable UUID attachmentId;

  private @Nullable URI url;

  private @Nullable String type;

  public MessageMetadataAttachmentsInner attachmentId(@Nullable UUID attachmentId) {
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

  public MessageMetadataAttachmentsInner url(@Nullable URI url) {
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

  public MessageMetadataAttachmentsInner type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageMetadataAttachmentsInner messageMetadataAttachmentsInner = (MessageMetadataAttachmentsInner) o;
    return Objects.equals(this.attachmentId, messageMetadataAttachmentsInner.attachmentId) &&
        Objects.equals(this.url, messageMetadataAttachmentsInner.url) &&
        Objects.equals(this.type, messageMetadataAttachmentsInner.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentId, url, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessageMetadataAttachmentsInner {\n");
    sb.append("    attachmentId: ").append(toIndentedString(attachmentId)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

