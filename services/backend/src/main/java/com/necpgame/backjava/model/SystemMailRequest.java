package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Attachment;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * SystemMailRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SystemMailRequest {

  private String templateId;

  private @Nullable String locale;

  @Valid
  private List<String> recipients = new ArrayList<>();

  @Valid
  private List<String> segments = new ArrayList<>();

  private @Nullable String subjectOverrides;

  @Valid
  private Map<String, Object> bodyVariables = new HashMap<>();

  @Valid
  private List<@Valid Attachment> attachments = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime scheduleAt;

  public SystemMailRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SystemMailRequest(String templateId, List<String> recipients) {
    this.templateId = templateId;
    this.recipients = recipients;
  }

  public SystemMailRequest templateId(String templateId) {
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

  public SystemMailRequest locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public SystemMailRequest recipients(List<String> recipients) {
    this.recipients = recipients;
    return this;
  }

  public SystemMailRequest addRecipientsItem(String recipientsItem) {
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

  public SystemMailRequest segments(List<String> segments) {
    this.segments = segments;
    return this;
  }

  public SystemMailRequest addSegmentsItem(String segmentsItem) {
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

  public SystemMailRequest subjectOverrides(@Nullable String subjectOverrides) {
    this.subjectOverrides = subjectOverrides;
    return this;
  }

  /**
   * Get subjectOverrides
   * @return subjectOverrides
   */
  
  @Schema(name = "subjectOverrides", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subjectOverrides")
  public @Nullable String getSubjectOverrides() {
    return subjectOverrides;
  }

  public void setSubjectOverrides(@Nullable String subjectOverrides) {
    this.subjectOverrides = subjectOverrides;
  }

  public SystemMailRequest bodyVariables(Map<String, Object> bodyVariables) {
    this.bodyVariables = bodyVariables;
    return this;
  }

  public SystemMailRequest putBodyVariablesItem(String key, Object bodyVariablesItem) {
    if (this.bodyVariables == null) {
      this.bodyVariables = new HashMap<>();
    }
    this.bodyVariables.put(key, bodyVariablesItem);
    return this;
  }

  /**
   * Get bodyVariables
   * @return bodyVariables
   */
  
  @Schema(name = "bodyVariables", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bodyVariables")
  public Map<String, Object> getBodyVariables() {
    return bodyVariables;
  }

  public void setBodyVariables(Map<String, Object> bodyVariables) {
    this.bodyVariables = bodyVariables;
  }

  public SystemMailRequest attachments(List<@Valid Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public SystemMailRequest addAttachmentsItem(Attachment attachmentsItem) {
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

  public SystemMailRequest scheduleAt(@Nullable OffsetDateTime scheduleAt) {
    this.scheduleAt = scheduleAt;
    return this;
  }

  /**
   * Get scheduleAt
   * @return scheduleAt
   */
  @Valid 
  @Schema(name = "scheduleAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduleAt")
  public @Nullable OffsetDateTime getScheduleAt() {
    return scheduleAt;
  }

  public void setScheduleAt(@Nullable OffsetDateTime scheduleAt) {
    this.scheduleAt = scheduleAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMailRequest systemMailRequest = (SystemMailRequest) o;
    return Objects.equals(this.templateId, systemMailRequest.templateId) &&
        Objects.equals(this.locale, systemMailRequest.locale) &&
        Objects.equals(this.recipients, systemMailRequest.recipients) &&
        Objects.equals(this.segments, systemMailRequest.segments) &&
        Objects.equals(this.subjectOverrides, systemMailRequest.subjectOverrides) &&
        Objects.equals(this.bodyVariables, systemMailRequest.bodyVariables) &&
        Objects.equals(this.attachments, systemMailRequest.attachments) &&
        Objects.equals(this.scheduleAt, systemMailRequest.scheduleAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, locale, recipients, segments, subjectOverrides, bodyVariables, attachments, scheduleAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMailRequest {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
    sb.append("    subjectOverrides: ").append(toIndentedString(subjectOverrides)).append("\n");
    sb.append("    bodyVariables: ").append(toIndentedString(bodyVariables)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    scheduleAt: ").append(toIndentedString(scheduleAt)).append("\n");
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

