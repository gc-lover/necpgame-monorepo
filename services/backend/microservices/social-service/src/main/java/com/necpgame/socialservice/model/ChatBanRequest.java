package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.BanSeverity;
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
 * ChatBanRequest
 */


public class ChatBanRequest {

  private UUID playerId;

  private @Nullable String channelType;

  private @Nullable String channelId;

  private String reason;

  private @Nullable Integer durationMinutes;

  private @Nullable BanSeverity severity;

  @Valid
  private List<URI> evidence = new ArrayList<>();

  public ChatBanRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatBanRequest(UUID playerId, String reason) {
    this.playerId = playerId;
    this.reason = reason;
  }

  public ChatBanRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public ChatBanRequest channelType(@Nullable String channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelType")
  public @Nullable String getChannelType() {
    return channelType;
  }

  public void setChannelType(@Nullable String channelType) {
    this.channelType = channelType;
  }

  public ChatBanRequest channelId(@Nullable String channelId) {
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

  public ChatBanRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public ChatBanRequest durationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * minimum: 1
   * @return durationMinutes
   */
  @Min(value = 1) 
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationMinutes")
  public @Nullable Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public ChatBanRequest severity(@Nullable BanSeverity severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @Valid 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable BanSeverity getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable BanSeverity severity) {
    this.severity = severity;
  }

  public ChatBanRequest evidence(List<URI> evidence) {
    this.evidence = evidence;
    return this;
  }

  public ChatBanRequest addEvidenceItem(URI evidenceItem) {
    if (this.evidence == null) {
      this.evidence = new ArrayList<>();
    }
    this.evidence.add(evidenceItem);
    return this;
  }

  /**
   * Get evidence
   * @return evidence
   */
  @Valid 
  @Schema(name = "evidence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evidence")
  public List<URI> getEvidence() {
    return evidence;
  }

  public void setEvidence(List<URI> evidence) {
    this.evidence = evidence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatBanRequest chatBanRequest = (ChatBanRequest) o;
    return Objects.equals(this.playerId, chatBanRequest.playerId) &&
        Objects.equals(this.channelType, chatBanRequest.channelType) &&
        Objects.equals(this.channelId, chatBanRequest.channelId) &&
        Objects.equals(this.reason, chatBanRequest.reason) &&
        Objects.equals(this.durationMinutes, chatBanRequest.durationMinutes) &&
        Objects.equals(this.severity, chatBanRequest.severity) &&
        Objects.equals(this.evidence, chatBanRequest.evidence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, channelType, channelId, reason, durationMinutes, severity, evidence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatBanRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    evidence: ").append(toIndentedString(evidence)).append("\n");
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

