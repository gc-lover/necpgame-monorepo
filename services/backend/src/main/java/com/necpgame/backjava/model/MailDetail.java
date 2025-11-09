package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.Attachment;
import com.necpgame.backjava.model.CODInfo;
import com.necpgame.backjava.model.MailHistoryEntry;
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
 * MailDetail
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MailDetail {

  private @Nullable String mailId;

  private @Nullable String senderId;

  private @Nullable String senderName;

  private @Nullable String recipientId;

  private @Nullable String recipientName;

  private @Nullable String subject;

  private @Nullable String body;

  /**
   * Gets or Sets channel
   */
  public enum ChannelEnum {
    PLAYER("PLAYER"),
    
    SYSTEM("SYSTEM"),
    
    GM("GM");

    private final String value;

    ChannelEnum(String value) {
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
    public static ChannelEnum fromValue(String value) {
      for (ChannelEnum b : ChannelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ChannelEnum channel;

  private @Nullable Boolean isCOD;

  private @Nullable CODInfo codInfo;

  @Valid
  private List<@Valid Attachment> attachments = new ArrayList<>();

  private @Nullable String status;

  @Valid
  private List<@Valid MailHistoryEntry> history = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime sentAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public MailDetail mailId(@Nullable String mailId) {
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

  public MailDetail senderId(@Nullable String senderId) {
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

  public MailDetail senderName(@Nullable String senderName) {
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

  public MailDetail recipientId(@Nullable String recipientId) {
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

  public MailDetail recipientName(@Nullable String recipientName) {
    this.recipientName = recipientName;
    return this;
  }

  /**
   * Get recipientName
   * @return recipientName
   */
  
  @Schema(name = "recipientName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipientName")
  public @Nullable String getRecipientName() {
    return recipientName;
  }

  public void setRecipientName(@Nullable String recipientName) {
    this.recipientName = recipientName;
  }

  public MailDetail subject(@Nullable String subject) {
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

  public MailDetail body(@Nullable String body) {
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

  public MailDetail channel(@Nullable ChannelEnum channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel")
  public @Nullable ChannelEnum getChannel() {
    return channel;
  }

  public void setChannel(@Nullable ChannelEnum channel) {
    this.channel = channel;
  }

  public MailDetail isCOD(@Nullable Boolean isCOD) {
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

  public MailDetail codInfo(@Nullable CODInfo codInfo) {
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

  public MailDetail attachments(List<@Valid Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public MailDetail addAttachmentsItem(Attachment attachmentsItem) {
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

  public MailDetail status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public MailDetail history(List<@Valid MailHistoryEntry> history) {
    this.history = history;
    return this;
  }

  public MailDetail addHistoryItem(MailHistoryEntry historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * Get history
   * @return history
   */
  @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<@Valid MailHistoryEntry> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid MailHistoryEntry> history) {
    this.history = history;
  }

  public MailDetail sentAt(@Nullable OffsetDateTime sentAt) {
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

  public MailDetail expiresAt(@Nullable OffsetDateTime expiresAt) {
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
    MailDetail mailDetail = (MailDetail) o;
    return Objects.equals(this.mailId, mailDetail.mailId) &&
        Objects.equals(this.senderId, mailDetail.senderId) &&
        Objects.equals(this.senderName, mailDetail.senderName) &&
        Objects.equals(this.recipientId, mailDetail.recipientId) &&
        Objects.equals(this.recipientName, mailDetail.recipientName) &&
        Objects.equals(this.subject, mailDetail.subject) &&
        Objects.equals(this.body, mailDetail.body) &&
        Objects.equals(this.channel, mailDetail.channel) &&
        Objects.equals(this.isCOD, mailDetail.isCOD) &&
        Objects.equals(this.codInfo, mailDetail.codInfo) &&
        Objects.equals(this.attachments, mailDetail.attachments) &&
        Objects.equals(this.status, mailDetail.status) &&
        Objects.equals(this.history, mailDetail.history) &&
        Objects.equals(this.sentAt, mailDetail.sentAt) &&
        Objects.equals(this.expiresAt, mailDetail.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailId, senderId, senderName, recipientId, recipientName, subject, body, channel, isCOD, codInfo, attachments, status, history, sentAt, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailDetail {\n");
    sb.append("    mailId: ").append(toIndentedString(mailId)).append("\n");
    sb.append("    senderId: ").append(toIndentedString(senderId)).append("\n");
    sb.append("    senderName: ").append(toIndentedString(senderName)).append("\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    recipientName: ").append(toIndentedString(recipientName)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    isCOD: ").append(toIndentedString(isCOD)).append("\n");
    sb.append("    codInfo: ").append(toIndentedString(codInfo)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
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

