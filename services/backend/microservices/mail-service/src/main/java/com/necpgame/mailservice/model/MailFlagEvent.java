package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * MailFlagEvent
 */


public class MailFlagEvent {

  private @Nullable String mailId;

  private @Nullable String reporterId;

  private @Nullable String reason;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime reportedAt;

  public MailFlagEvent mailId(@Nullable String mailId) {
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

  public MailFlagEvent reporterId(@Nullable String reporterId) {
    this.reporterId = reporterId;
    return this;
  }

  /**
   * Get reporterId
   * @return reporterId
   */
  
  @Schema(name = "reporterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reporterId")
  public @Nullable String getReporterId() {
    return reporterId;
  }

  public void setReporterId(@Nullable String reporterId) {
    this.reporterId = reporterId;
  }

  public MailFlagEvent reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public MailFlagEvent reportedAt(@Nullable OffsetDateTime reportedAt) {
    this.reportedAt = reportedAt;
    return this;
  }

  /**
   * Get reportedAt
   * @return reportedAt
   */
  @Valid 
  @Schema(name = "reportedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reportedAt")
  public @Nullable OffsetDateTime getReportedAt() {
    return reportedAt;
  }

  public void setReportedAt(@Nullable OffsetDateTime reportedAt) {
    this.reportedAt = reportedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailFlagEvent mailFlagEvent = (MailFlagEvent) o;
    return Objects.equals(this.mailId, mailFlagEvent.mailId) &&
        Objects.equals(this.reporterId, mailFlagEvent.reporterId) &&
        Objects.equals(this.reason, mailFlagEvent.reason) &&
        Objects.equals(this.reportedAt, mailFlagEvent.reportedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailId, reporterId, reason, reportedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailFlagEvent {\n");
    sb.append("    mailId: ").append(toIndentedString(mailId)).append("\n");
    sb.append("    reporterId: ").append(toIndentedString(reporterId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    reportedAt: ").append(toIndentedString(reportedAt)).append("\n");
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

