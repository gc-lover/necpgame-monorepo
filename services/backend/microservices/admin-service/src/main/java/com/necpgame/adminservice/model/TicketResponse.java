package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.AttachmentMetadata;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * TicketResponse
 */


public class TicketResponse {

  private String responseId;

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

  private @Nullable Boolean isInternal;

  @Valid
  private List<@Valid AttachmentMetadata> attachments = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  public TicketResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TicketResponse(String responseId, AuthorTypeEnum authorType, String message, OffsetDateTime createdAt) {
    this.responseId = responseId;
    this.authorType = authorType;
    this.message = message;
    this.createdAt = createdAt;
  }

  public TicketResponse responseId(String responseId) {
    this.responseId = responseId;
    return this;
  }

  /**
   * Get responseId
   * @return responseId
   */
  @NotNull 
  @Schema(name = "responseId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("responseId")
  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }

  public TicketResponse authorType(AuthorTypeEnum authorType) {
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

  public TicketResponse authorId(@Nullable String authorId) {
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

  public TicketResponse message(String message) {
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

  public TicketResponse isInternal(@Nullable Boolean isInternal) {
    this.isInternal = isInternal;
    return this;
  }

  /**
   * Get isInternal
   * @return isInternal
   */
  
  @Schema(name = "isInternal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isInternal")
  public @Nullable Boolean getIsInternal() {
    return isInternal;
  }

  public void setIsInternal(@Nullable Boolean isInternal) {
    this.isInternal = isInternal;
  }

  public TicketResponse attachments(List<@Valid AttachmentMetadata> attachments) {
    this.attachments = attachments;
    return this;
  }

  public TicketResponse addAttachmentsItem(AttachmentMetadata attachmentsItem) {
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

  public TicketResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TicketResponse ticketResponse = (TicketResponse) o;
    return Objects.equals(this.responseId, ticketResponse.responseId) &&
        Objects.equals(this.authorType, ticketResponse.authorType) &&
        Objects.equals(this.authorId, ticketResponse.authorId) &&
        Objects.equals(this.message, ticketResponse.message) &&
        Objects.equals(this.isInternal, ticketResponse.isInternal) &&
        Objects.equals(this.attachments, ticketResponse.attachments) &&
        Objects.equals(this.createdAt, ticketResponse.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseId, authorType, authorId, message, isInternal, attachments, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TicketResponse {\n");
    sb.append("    responseId: ").append(toIndentedString(responseId)).append("\n");
    sb.append("    authorType: ").append(toIndentedString(authorType)).append("\n");
    sb.append("    authorId: ").append(toIndentedString(authorId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    isInternal: ").append(toIndentedString(isInternal)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

