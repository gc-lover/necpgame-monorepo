package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.mailservice.model.Attachment;
import com.necpgame.mailservice.model.CODInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MailSendRequest
 */


public class MailSendRequest {

  @Valid
  private List<String> recipients = new ArrayList<>();

  private String subject;

  private String body;

  @Valid
  private List<@Valid Attachment> attachments = new ArrayList<>();

  private @Nullable CODInfo codInfo;

  private @Nullable String idempotencyKey;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public MailSendRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MailSendRequest(List<String> recipients, String subject, String body) {
    this.recipients = recipients;
    this.subject = subject;
    this.body = body;
  }

  public MailSendRequest recipients(List<String> recipients) {
    this.recipients = recipients;
    return this;
  }

  public MailSendRequest addRecipientsItem(String recipientsItem) {
    if (this.recipients == null) {
      this.recipients = new ArrayList<>();
    }
    this.recipients.add(recipientsItem);
    return this;
  }

  /**
   * Get recipients
   * @return recipients
   */
  @NotNull 
  @Schema(name = "recipients", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recipients")
  public List<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<String> recipients) {
    this.recipients = recipients;
  }

  public MailSendRequest subject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
   */
  @NotNull 
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public MailSendRequest body(String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  @NotNull 
  @Schema(name = "body", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("body")
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public MailSendRequest attachments(List<@Valid Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public MailSendRequest addAttachmentsItem(Attachment attachmentsItem) {
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
  public List<@Valid Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid Attachment> attachments) {
    this.attachments = attachments;
  }

  public MailSendRequest codInfo(@Nullable CODInfo codInfo) {
    this.codInfo = codInfo;
    return this;
  }

  /**
   * Get codInfo
   * @return codInfo
   */
  @Valid 
  @Schema(name = "codInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("codInfo")
  public @Nullable CODInfo getCodInfo() {
    return codInfo;
  }

  public void setCodInfo(@Nullable CODInfo codInfo) {
    this.codInfo = codInfo;
  }

  public MailSendRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  public MailSendRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public MailSendRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailSendRequest mailSendRequest = (MailSendRequest) o;
    return Objects.equals(this.recipients, mailSendRequest.recipients) &&
        Objects.equals(this.subject, mailSendRequest.subject) &&
        Objects.equals(this.body, mailSendRequest.body) &&
        Objects.equals(this.attachments, mailSendRequest.attachments) &&
        Objects.equals(this.codInfo, mailSendRequest.codInfo) &&
        Objects.equals(this.idempotencyKey, mailSendRequest.idempotencyKey) &&
        Objects.equals(this.metadata, mailSendRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipients, subject, body, attachments, codInfo, idempotencyKey, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailSendRequest {\n");
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    codInfo: ").append(toIndentedString(codInfo)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

