package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReportReason;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ChatReportRequest
 */


public class ChatReportRequest {

  private UUID reporterId;

  private @Nullable UUID messageId;

  private @Nullable String channelId;

  private UUID accusedPlayerId;

  private ReportReason reason;

  @Valid
  private List<URI> evidenceUrls = new ArrayList<>();

  private @Nullable String comment;

  public ChatReportRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatReportRequest(UUID reporterId, UUID accusedPlayerId, ReportReason reason) {
    this.reporterId = reporterId;
    this.accusedPlayerId = accusedPlayerId;
    this.reason = reason;
  }

  public ChatReportRequest reporterId(UUID reporterId) {
    this.reporterId = reporterId;
    return this;
  }

  /**
   * Get reporterId
   * @return reporterId
   */
  @NotNull @Valid 
  @Schema(name = "reporterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reporterId")
  public UUID getReporterId() {
    return reporterId;
  }

  public void setReporterId(UUID reporterId) {
    this.reporterId = reporterId;
  }

  public ChatReportRequest messageId(@Nullable UUID messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Get messageId
   * @return messageId
   */
  @Valid 
  @Schema(name = "messageId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("messageId")
  public @Nullable UUID getMessageId() {
    return messageId;
  }

  public void setMessageId(@Nullable UUID messageId) {
    this.messageId = messageId;
  }

  public ChatReportRequest channelId(@Nullable String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelId")
  public @Nullable String getChannelId() {
    return channelId;
  }

  public void setChannelId(@Nullable String channelId) {
    this.channelId = channelId;
  }

  public ChatReportRequest accusedPlayerId(UUID accusedPlayerId) {
    this.accusedPlayerId = accusedPlayerId;
    return this;
  }

  /**
   * Get accusedPlayerId
   * @return accusedPlayerId
   */
  @NotNull @Valid 
  @Schema(name = "accusedPlayerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accusedPlayerId")
  public UUID getAccusedPlayerId() {
    return accusedPlayerId;
  }

  public void setAccusedPlayerId(UUID accusedPlayerId) {
    this.accusedPlayerId = accusedPlayerId;
  }

  public ChatReportRequest reason(ReportReason reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull @Valid 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public ReportReason getReason() {
    return reason;
  }

  public void setReason(ReportReason reason) {
    this.reason = reason;
  }

  public ChatReportRequest evidenceUrls(List<URI> evidenceUrls) {
    this.evidenceUrls = evidenceUrls;
    return this;
  }

  public ChatReportRequest addEvidenceUrlsItem(URI evidenceUrlsItem) {
    if (this.evidenceUrls == null) {
      this.evidenceUrls = new ArrayList<>();
    }
    this.evidenceUrls.add(evidenceUrlsItem);
    return this;
  }

  /**
   * Get evidenceUrls
   * @return evidenceUrls
   */
  @Valid 
  @Schema(name = "evidenceUrls", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evidenceUrls")
  public List<URI> getEvidenceUrls() {
    return evidenceUrls;
  }

  public void setEvidenceUrls(List<URI> evidenceUrls) {
    this.evidenceUrls = evidenceUrls;
  }

  public ChatReportRequest comment(@Nullable String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  @Size(max = 500) 
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public @Nullable String getComment() {
    return comment;
  }

  public void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatReportRequest chatReportRequest = (ChatReportRequest) o;
    return Objects.equals(this.reporterId, chatReportRequest.reporterId) &&
        Objects.equals(this.messageId, chatReportRequest.messageId) &&
        Objects.equals(this.channelId, chatReportRequest.channelId) &&
        Objects.equals(this.accusedPlayerId, chatReportRequest.accusedPlayerId) &&
        Objects.equals(this.reason, chatReportRequest.reason) &&
        Objects.equals(this.evidenceUrls, chatReportRequest.evidenceUrls) &&
        Objects.equals(this.comment, chatReportRequest.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reporterId, messageId, channelId, accusedPlayerId, reason, evidenceUrls, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatReportRequest {\n");
    sb.append("    reporterId: ").append(toIndentedString(reporterId)).append("\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    accusedPlayerId: ").append(toIndentedString(accusedPlayerId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    evidenceUrls: ").append(toIndentedString(evidenceUrls)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

