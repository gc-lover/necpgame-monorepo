package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.AttachmentMetadata;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AddResponseRequest
 */


public class AddResponseRequest {

  /**
   * Gets or Sets authorType
   */
  public enum AuthorTypeEnum {
    PLAYER("PLAYER"),
    
    AGENT("AGENT"),
    
    SYSTEM("SYSTEM");

    private final String value;

    AuthorTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AuthorTypeEnum fromValue(String value) {
      for (AuthorTypeEnum b : AuthorTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AuthorTypeEnum authorType;

  private @Nullable String authorId;

  private String message;

  private Boolean isInternal = false;

  @Valid
  private List<@Valid AttachmentMetadata> attachments = new ArrayList<>();

  public AddResponseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AddResponseRequest(AuthorTypeEnum authorType, String message) {
    this.authorType = authorType;
    this.message = message;
  }

  public AddResponseRequest authorType(AuthorTypeEnum authorType) {
    this.authorType = authorType;
    return this;
  }

  /**
   * Get authorType
   * @return authorType
   */
  @NotNull 
  @Schema(name = "authorType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("authorType")
  public AuthorTypeEnum getAuthorType() {
    return authorType;
  }

  public void setAuthorType(AuthorTypeEnum authorType) {
    this.authorType = authorType;
  }

  public AddResponseRequest authorId(@Nullable String authorId) {
    this.authorId = authorId;
    return this;
  }

  /**
   * Get authorId
   * @return authorId
   */
  
  @Schema(name = "authorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authorId")
  public @Nullable String getAuthorId() {
    return authorId;
  }

  public void setAuthorId(@Nullable String authorId) {
    this.authorId = authorId;
  }

  public AddResponseRequest message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public AddResponseRequest isInternal(Boolean isInternal) {
    this.isInternal = isInternal;
    return this;
  }

  /**
   * Get isInternal
   * @return isInternal
   */
  
  @Schema(name = "isInternal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isInternal")
  public Boolean getIsInternal() {
    return isInternal;
  }

  public void setIsInternal(Boolean isInternal) {
    this.isInternal = isInternal;
  }

  public AddResponseRequest attachments(List<@Valid AttachmentMetadata> attachments) {
    this.attachments = attachments;
    return this;
  }

  public AddResponseRequest addAttachmentsItem(AttachmentMetadata attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid AttachmentMetadata> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid AttachmentMetadata> attachments) {
    this.attachments = attachments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddResponseRequest addResponseRequest = (AddResponseRequest) o;
    return Objects.equals(this.authorType, addResponseRequest.authorType) &&
        Objects.equals(this.authorId, addResponseRequest.authorId) &&
        Objects.equals(this.message, addResponseRequest.message) &&
        Objects.equals(this.isInternal, addResponseRequest.isInternal) &&
        Objects.equals(this.attachments, addResponseRequest.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorType, authorId, message, isInternal, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddResponseRequest {\n");
    sb.append("    authorType: ").append(toIndentedString(authorType)).append("\n");
    sb.append("    authorId: ").append(toIndentedString(authorId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    isInternal: ").append(toIndentedString(isInternal)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
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

