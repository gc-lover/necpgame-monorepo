package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * MailMessage
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MailMessage {

  private @Nullable String mailId;

  private @Nullable String senderName;

  private @Nullable String subject;

  private @Nullable String body;

  private @Nullable Boolean hasAttachments;

  private @Nullable BigDecimal goldAttached;

  private @Nullable BigDecimal codAmount;

  private @Nullable Boolean isRead;

  private @Nullable Boolean isSystem;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public MailMessage mailId(@Nullable String mailId) {
    this.mailId = mailId;
    return this;
  }

  /**
   * Get mailId
   * @return mailId
   */
  
  @Schema(name = "mail_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mail_id")
  public @Nullable String getMailId() {
    return mailId;
  }

  public void setMailId(@Nullable String mailId) {
    this.mailId = mailId;
  }

  public MailMessage senderName(@Nullable String senderName) {
    this.senderName = senderName;
    return this;
  }

  /**
   * Get senderName
   * @return senderName
   */
  
  @Schema(name = "sender_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sender_name")
  public @Nullable String getSenderName() {
    return senderName;
  }

  public void setSenderName(@Nullable String senderName) {
    this.senderName = senderName;
  }

  public MailMessage subject(@Nullable String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
   */
  
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subject")
  public @Nullable String getSubject() {
    return subject;
  }

  public void setSubject(@Nullable String subject) {
    this.subject = subject;
  }

  public MailMessage body(@Nullable String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  
  @Schema(name = "body", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body")
  public @Nullable String getBody() {
    return body;
  }

  public void setBody(@Nullable String body) {
    this.body = body;
  }

  public MailMessage hasAttachments(@Nullable Boolean hasAttachments) {
    this.hasAttachments = hasAttachments;
    return this;
  }

  /**
   * Get hasAttachments
   * @return hasAttachments
   */
  
  @Schema(name = "has_attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_attachments")
  public @Nullable Boolean getHasAttachments() {
    return hasAttachments;
  }

  public void setHasAttachments(@Nullable Boolean hasAttachments) {
    this.hasAttachments = hasAttachments;
  }

  public MailMessage goldAttached(@Nullable BigDecimal goldAttached) {
    this.goldAttached = goldAttached;
    return this;
  }

  /**
   * Get goldAttached
   * @return goldAttached
   */
  @Valid 
  @Schema(name = "gold_attached", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gold_attached")
  public @Nullable BigDecimal getGoldAttached() {
    return goldAttached;
  }

  public void setGoldAttached(@Nullable BigDecimal goldAttached) {
    this.goldAttached = goldAttached;
  }

  public MailMessage codAmount(@Nullable BigDecimal codAmount) {
    this.codAmount = codAmount;
    return this;
  }

  /**
   * Get codAmount
   * @return codAmount
   */
  @Valid 
  @Schema(name = "cod_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cod_amount")
  public @Nullable BigDecimal getCodAmount() {
    return codAmount;
  }

  public void setCodAmount(@Nullable BigDecimal codAmount) {
    this.codAmount = codAmount;
  }

  public MailMessage isRead(@Nullable Boolean isRead) {
    this.isRead = isRead;
    return this;
  }

  /**
   * Get isRead
   * @return isRead
   */
  
  @Schema(name = "is_read", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_read")
  public @Nullable Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(@Nullable Boolean isRead) {
    this.isRead = isRead;
  }

  public MailMessage isSystem(@Nullable Boolean isSystem) {
    this.isSystem = isSystem;
    return this;
  }

  /**
   * Get isSystem
   * @return isSystem
   */
  
  @Schema(name = "is_system", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_system")
  public @Nullable Boolean getIsSystem() {
    return isSystem;
  }

  public void setIsSystem(@Nullable Boolean isSystem) {
    this.isSystem = isSystem;
  }

  public MailMessage createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public MailMessage expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailMessage mailMessage = (MailMessage) o;
    return Objects.equals(this.mailId, mailMessage.mailId) &&
        Objects.equals(this.senderName, mailMessage.senderName) &&
        Objects.equals(this.subject, mailMessage.subject) &&
        Objects.equals(this.body, mailMessage.body) &&
        Objects.equals(this.hasAttachments, mailMessage.hasAttachments) &&
        Objects.equals(this.goldAttached, mailMessage.goldAttached) &&
        Objects.equals(this.codAmount, mailMessage.codAmount) &&
        Objects.equals(this.isRead, mailMessage.isRead) &&
        Objects.equals(this.isSystem, mailMessage.isSystem) &&
        Objects.equals(this.createdAt, mailMessage.createdAt) &&
        Objects.equals(this.expiresAt, mailMessage.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailId, senderName, subject, body, hasAttachments, goldAttached, codAmount, isRead, isSystem, createdAt, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailMessage {\n");
    sb.append("    mailId: ").append(toIndentedString(mailId)).append("\n");
    sb.append("    senderName: ").append(toIndentedString(senderName)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    hasAttachments: ").append(toIndentedString(hasAttachments)).append("\n");
    sb.append("    goldAttached: ").append(toIndentedString(goldAttached)).append("\n");
    sb.append("    codAmount: ").append(toIndentedString(codAmount)).append("\n");
    sb.append("    isRead: ").append(toIndentedString(isRead)).append("\n");
    sb.append("    isSystem: ").append(toIndentedString(isSystem)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

