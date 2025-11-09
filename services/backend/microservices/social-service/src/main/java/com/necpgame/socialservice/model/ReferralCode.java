package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ReferralStats;
import java.net.URI;
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
 * ReferralCode
 */


public class ReferralCode {

  private String code;

  private String playerId;

  private @Nullable URI shareUrl;

  private @Nullable URI qrcodeUrl;

  private @Nullable Integer usesCount;

  private @Nullable Integer maxUses;

  private @Nullable Boolean isActive;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable ReferralStats stats;

  public ReferralCode() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReferralCode(String code, String playerId, OffsetDateTime createdAt) {
    this.code = code;
    this.playerId = playerId;
    this.createdAt = createdAt;
  }

  public ReferralCode code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ReferralCode playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public ReferralCode shareUrl(@Nullable URI shareUrl) {
    this.shareUrl = shareUrl;
    return this;
  }

  /**
   * Get shareUrl
   * @return shareUrl
   */
  @Valid 
  @Schema(name = "shareUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shareUrl")
  public @Nullable URI getShareUrl() {
    return shareUrl;
  }

  public void setShareUrl(@Nullable URI shareUrl) {
    this.shareUrl = shareUrl;
  }

  public ReferralCode qrcodeUrl(@Nullable URI qrcodeUrl) {
    this.qrcodeUrl = qrcodeUrl;
    return this;
  }

  /**
   * Get qrcodeUrl
   * @return qrcodeUrl
   */
  @Valid 
  @Schema(name = "qrcodeUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("qrcodeUrl")
  public @Nullable URI getQrcodeUrl() {
    return qrcodeUrl;
  }

  public void setQrcodeUrl(@Nullable URI qrcodeUrl) {
    this.qrcodeUrl = qrcodeUrl;
  }

  public ReferralCode usesCount(@Nullable Integer usesCount) {
    this.usesCount = usesCount;
    return this;
  }

  /**
   * Get usesCount
   * @return usesCount
   */
  
  @Schema(name = "usesCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usesCount")
  public @Nullable Integer getUsesCount() {
    return usesCount;
  }

  public void setUsesCount(@Nullable Integer usesCount) {
    this.usesCount = usesCount;
  }

  public ReferralCode maxUses(@Nullable Integer maxUses) {
    this.maxUses = maxUses;
    return this;
  }

  /**
   * Get maxUses
   * @return maxUses
   */
  
  @Schema(name = "maxUses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxUses")
  public @Nullable Integer getMaxUses() {
    return maxUses;
  }

  public void setMaxUses(@Nullable Integer maxUses) {
    this.maxUses = maxUses;
  }

  public ReferralCode isActive(@Nullable Boolean isActive) {
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

  public ReferralCode createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ReferralCode expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public ReferralCode stats(@Nullable ReferralStats stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  @Valid 
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public @Nullable ReferralStats getStats() {
    return stats;
  }

  public void setStats(@Nullable ReferralStats stats) {
    this.stats = stats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferralCode referralCode = (ReferralCode) o;
    return Objects.equals(this.code, referralCode.code) &&
        Objects.equals(this.playerId, referralCode.playerId) &&
        Objects.equals(this.shareUrl, referralCode.shareUrl) &&
        Objects.equals(this.qrcodeUrl, referralCode.qrcodeUrl) &&
        Objects.equals(this.usesCount, referralCode.usesCount) &&
        Objects.equals(this.maxUses, referralCode.maxUses) &&
        Objects.equals(this.isActive, referralCode.isActive) &&
        Objects.equals(this.createdAt, referralCode.createdAt) &&
        Objects.equals(this.expiresAt, referralCode.expiresAt) &&
        Objects.equals(this.stats, referralCode.stats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, playerId, shareUrl, qrcodeUrl, usesCount, maxUses, isActive, createdAt, expiresAt, stats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferralCode {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    shareUrl: ").append(toIndentedString(shareUrl)).append("\n");
    sb.append("    qrcodeUrl: ").append(toIndentedString(qrcodeUrl)).append("\n");
    sb.append("    usesCount: ").append(toIndentedString(usesCount)).append("\n");
    sb.append("    maxUses: ").append(toIndentedString(maxUses)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
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

