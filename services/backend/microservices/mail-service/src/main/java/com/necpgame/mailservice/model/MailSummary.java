package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * MailSummary
 */


public class MailSummary {

  private @Nullable String mailId;

  private @Nullable String senderId;

  private @Nullable String senderName;

  private @Nullable String recipientId;

  private @Nullable String subject;

  private @Nullable Boolean hasAttachments;

  private @Nullable Boolean isCOD;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SENT("SENT"),
    
    DELIVERED("DELIVERED"),
    
    READ("READ"),
    
    CLAIMED("CLAIMED"),
    
    RETURNED("RETURNED"),
    
    EXPIRED("EXPIRED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime sentAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public MailSummary mailId(@Nullable String mailId) {
    this.mailId = mailId;
    return this;
  }

  /**
   * Get mailId
   * @return mailId
   */
  
  @Schema(name = "mailId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mailId")
  public @Nullable String getMailId() {
    return mailId;
  }

  public void setMailId(@Nullable String mailId) {
    this.mailId = mailId;
  }

  public MailSummary senderId(@Nullable String senderId) {
    this.senderId = senderId;
    return this;
  }

  /**
   * Get senderId
   * @return senderId
   */
  
  @Schema(name = "senderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("senderId")
  public @Nullable String getSenderId() {
    return senderId;
  }

  public void setSenderId(@Nullable String senderId) {
    this.senderId = senderId;
  }

  public MailSummary senderName(@Nullable String senderName) {
    this.senderName = senderName;
    return this;
  }

  /**
   * Get senderName
   * @return senderName
   */
  
  @Schema(name = "senderName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("senderName")
  public @Nullable String getSenderName() {
    return senderName;
  }

  public void setSenderName(@Nullable String senderName) {
    this.senderName = senderName;
  }

  public MailSummary recipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
    return this;
  }

  /**
   * Get recipientId
   * @return recipientId
   */
  
  @Schema(name = "recipientId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipientId")
  public @Nullable String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
  }

  public MailSummary subject(@Nullable String subject) {
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

  public MailSummary hasAttachments(@Nullable Boolean hasAttachments) {
    this.hasAttachments = hasAttachments;
    return this;
  }

  /**
   * Get hasAttachments
   * @return hasAttachments
   */
  
  @Schema(name = "hasAttachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasAttachments")
  public @Nullable Boolean getHasAttachments() {
    return hasAttachments;
  }

  public void setHasAttachments(@Nullable Boolean hasAttachments) {
    this.hasAttachments = hasAttachments;
  }

  public MailSummary isCOD(@Nullable Boolean isCOD) {
    this.isCOD = isCOD;
    return this;
  }

  /**
   * Get isCOD
   * @return isCOD
   */
  
  @Schema(name = "isCOD", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isCOD")
  public @Nullable Boolean getIsCOD() {
    return isCOD;
  }

  public void setIsCOD(@Nullable Boolean isCOD) {
    this.isCOD = isCOD;
  }

  public MailSummary status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public MailSummary sentAt(@Nullable OffsetDateTime sentAt) {
    this.sentAt = sentAt;
    return this;
  }

  /**
   * Get sentAt
   * @return sentAt
   */
  @Valid 
  @Schema(name = "sentAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sentAt")
  public @Nullable OffsetDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(@Nullable OffsetDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public MailSummary expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
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
    MailSummary mailSummary = (MailSummary) o;
    return Objects.equals(this.mailId, mailSummary.mailId) &&
        Objects.equals(this.senderId, mailSummary.senderId) &&
        Objects.equals(this.senderName, mailSummary.senderName) &&
        Objects.equals(this.recipientId, mailSummary.recipientId) &&
        Objects.equals(this.subject, mailSummary.subject) &&
        Objects.equals(this.hasAttachments, mailSummary.hasAttachments) &&
        Objects.equals(this.isCOD, mailSummary.isCOD) &&
        Objects.equals(this.status, mailSummary.status) &&
        Objects.equals(this.sentAt, mailSummary.sentAt) &&
        Objects.equals(this.expiresAt, mailSummary.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailId, senderId, senderName, recipientId, subject, hasAttachments, isCOD, status, sentAt, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailSummary {\n");
    sb.append("    mailId: ").append(toIndentedString(mailId)).append("\n");
    sb.append("    senderId: ").append(toIndentedString(senderId)).append("\n");
    sb.append("    senderName: ").append(toIndentedString(senderName)).append("\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    hasAttachments: ").append(toIndentedString(hasAttachments)).append("\n");
    sb.append("    isCOD: ").append(toIndentedString(isCOD)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    sentAt: ").append(toIndentedString(sentAt)).append("\n");
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

