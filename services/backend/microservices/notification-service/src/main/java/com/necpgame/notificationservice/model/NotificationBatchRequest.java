package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.notificationservice.model.NotificationBatchRequestRecipientsInner;
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
 * NotificationBatchRequest
 */


public class NotificationBatchRequest {

  private String batchId;

  private String templateId;

  @Valid
  private List<String> segments = new ArrayList<>();

  @Valid
  private List<@Valid NotificationBatchRequestRecipientsInner> recipients = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime scheduledAt;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("LOW"),
    
    NORMAL("NORMAL"),
    
    HIGH("HIGH"),
    
    CRITICAL("CRITICAL");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PriorityEnum priority;

  private @Nullable String idempotencyKey;

  public NotificationBatchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationBatchRequest(String batchId, String templateId) {
    this.batchId = batchId;
    this.templateId = templateId;
  }

  public NotificationBatchRequest batchId(String batchId) {
    this.batchId = batchId;
    return this;
  }

  /**
   * Get batchId
   * @return batchId
   */
  @NotNull 
  @Schema(name = "batchId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("batchId")
  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  public NotificationBatchRequest templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  @NotNull 
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateId")
  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public NotificationBatchRequest segments(List<String> segments) {
    this.segments = segments;
    return this;
  }

  public NotificationBatchRequest addSegmentsItem(String segmentsItem) {
    if (this.segments == null) {
      this.segments = new ArrayList<>();
    }
    this.segments.add(segmentsItem);
    return this;
  }

  /**
   * Get segments
   * @return segments
   */
  
  @Schema(name = "segments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("segments")
  public List<String> getSegments() {
    return segments;
  }

  public void setSegments(List<String> segments) {
    this.segments = segments;
  }

  public NotificationBatchRequest recipients(List<@Valid NotificationBatchRequestRecipientsInner> recipients) {
    this.recipients = recipients;
    return this;
  }

  public NotificationBatchRequest addRecipientsItem(NotificationBatchRequestRecipientsInner recipientsItem) {
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
  @Valid 
  @Schema(name = "recipients", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipients")
  public List<@Valid NotificationBatchRequestRecipientsInner> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<@Valid NotificationBatchRequestRecipientsInner> recipients) {
    this.recipients = recipients;
  }

  public NotificationBatchRequest scheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
    return this;
  }

  /**
   * Get scheduledAt
   * @return scheduledAt
   */
  @Valid 
  @Schema(name = "scheduledAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledAt")
  public @Nullable OffsetDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  public NotificationBatchRequest priority(@Nullable PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(@Nullable PriorityEnum priority) {
    this.priority = priority;
  }

  public NotificationBatchRequest idempotencyKey(@Nullable String idempotencyKey) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationBatchRequest notificationBatchRequest = (NotificationBatchRequest) o;
    return Objects.equals(this.batchId, notificationBatchRequest.batchId) &&
        Objects.equals(this.templateId, notificationBatchRequest.templateId) &&
        Objects.equals(this.segments, notificationBatchRequest.segments) &&
        Objects.equals(this.recipients, notificationBatchRequest.recipients) &&
        Objects.equals(this.scheduledAt, notificationBatchRequest.scheduledAt) &&
        Objects.equals(this.priority, notificationBatchRequest.priority) &&
        Objects.equals(this.idempotencyKey, notificationBatchRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(batchId, templateId, segments, recipients, scheduledAt, priority, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationBatchRequest {\n");
    sb.append("    batchId: ").append(toIndentedString(batchId)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    scheduledAt: ").append(toIndentedString(scheduledAt)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

