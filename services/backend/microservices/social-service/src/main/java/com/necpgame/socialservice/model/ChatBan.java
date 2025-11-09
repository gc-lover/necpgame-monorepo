package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.BanSeverity;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * ChatBan
 */


public class ChatBan {

  private UUID banId;

  private UUID playerId;

  private @Nullable String channelType;

  private @Nullable String channelId;

  private String reason;

  private UUID issuedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime issuedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable BanSeverity severity;

  private @Nullable Boolean isActive;

  public ChatBan() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatBan(UUID banId, UUID playerId, String reason, UUID issuedBy, OffsetDateTime issuedAt) {
    this.banId = banId;
    this.playerId = playerId;
    this.reason = reason;
    this.issuedBy = issuedBy;
    this.issuedAt = issuedAt;
  }

  public ChatBan banId(UUID banId) {
    this.banId = banId;
    return this;
  }

  /**
   * Get banId
   * @return banId
   */
  @NotNull @Valid 
  @Schema(name = "banId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("banId")
  public UUID getBanId() {
    return banId;
  }

  public void setBanId(UUID banId) {
    this.banId = banId;
  }

  public ChatBan playerId(UUID playerId) {
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

  public ChatBan channelType(@Nullable String channelType) {
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

  public ChatBan channelId(@Nullable String channelId) {
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

  public ChatBan reason(String reason) {
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

  public ChatBan issuedBy(UUID issuedBy) {
    this.issuedBy = issuedBy;
    return this;
  }

  /**
   * Get issuedBy
   * @return issuedBy
   */
  @NotNull @Valid 
  @Schema(name = "issuedBy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("issuedBy")
  public UUID getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(UUID issuedBy) {
    this.issuedBy = issuedBy;
  }

  public ChatBan issuedAt(OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
    return this;
  }

  /**
   * Get issuedAt
   * @return issuedAt
   */
  @NotNull @Valid 
  @Schema(name = "issuedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("issuedAt")
  public OffsetDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public ChatBan expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public ChatBan severity(@Nullable BanSeverity severity) {
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

  public ChatBan isActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Get isActive
   * @return isActive
   */
  
  @Schema(name = "isActive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isActive")
  public @Nullable Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatBan chatBan = (ChatBan) o;
    return Objects.equals(this.banId, chatBan.banId) &&
        Objects.equals(this.playerId, chatBan.playerId) &&
        Objects.equals(this.channelType, chatBan.channelType) &&
        Objects.equals(this.channelId, chatBan.channelId) &&
        Objects.equals(this.reason, chatBan.reason) &&
        Objects.equals(this.issuedBy, chatBan.issuedBy) &&
        Objects.equals(this.issuedAt, chatBan.issuedAt) &&
        Objects.equals(this.expiresAt, chatBan.expiresAt) &&
        Objects.equals(this.severity, chatBan.severity) &&
        Objects.equals(this.isActive, chatBan.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(banId, playerId, channelType, channelId, reason, issuedBy, issuedAt, expiresAt, severity, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatBan {\n");
    sb.append("    banId: ").append(toIndentedString(banId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    issuedBy: ").append(toIndentedString(issuedBy)).append("\n");
    sb.append("    issuedAt: ").append(toIndentedString(issuedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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

