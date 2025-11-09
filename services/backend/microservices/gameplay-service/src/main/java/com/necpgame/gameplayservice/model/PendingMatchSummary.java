package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.MatchQualityReport;
import com.necpgame.gameplayservice.model.MatchStatus;
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
 * PendingMatchSummary
 */


public class PendingMatchSummary {

  private UUID matchId;

  private String mode;

  private MatchStatus status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable Integer queueSize;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedStartAt;

  private MatchQualityReport quality;

  /**
   * Gets or Sets latencyBucket
   */
  public enum LatencyBucketEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH");

    private final String value;

    LatencyBucketEnum(String value) {
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
    public static LatencyBucketEnum fromValue(String value) {
      for (LatencyBucketEnum b : LatencyBucketEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LatencyBucketEnum latencyBucket;

  public PendingMatchSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PendingMatchSummary(UUID matchId, String mode, MatchStatus status, MatchQualityReport quality) {
    this.matchId = matchId;
    this.mode = mode;
    this.status = status;
    this.quality = quality;
  }

  public PendingMatchSummary matchId(UUID matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  @NotNull @Valid 
  @Schema(name = "matchId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("matchId")
  public UUID getMatchId() {
    return matchId;
  }

  public void setMatchId(UUID matchId) {
    this.matchId = matchId;
  }

  public PendingMatchSummary mode(String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public PendingMatchSummary status(MatchStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public MatchStatus getStatus() {
    return status;
  }

  public void setStatus(MatchStatus status) {
    this.status = status;
  }

  public PendingMatchSummary createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PendingMatchSummary queueSize(@Nullable Integer queueSize) {
    this.queueSize = queueSize;
    return this;
  }

  /**
   * Get queueSize
   * @return queueSize
   */
  
  @Schema(name = "queueSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueSize")
  public @Nullable Integer getQueueSize() {
    return queueSize;
  }

  public void setQueueSize(@Nullable Integer queueSize) {
    this.queueSize = queueSize;
  }

  public PendingMatchSummary estimatedStartAt(@Nullable OffsetDateTime estimatedStartAt) {
    this.estimatedStartAt = estimatedStartAt;
    return this;
  }

  /**
   * Get estimatedStartAt
   * @return estimatedStartAt
   */
  @Valid 
  @Schema(name = "estimatedStartAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedStartAt")
  public @Nullable OffsetDateTime getEstimatedStartAt() {
    return estimatedStartAt;
  }

  public void setEstimatedStartAt(@Nullable OffsetDateTime estimatedStartAt) {
    this.estimatedStartAt = estimatedStartAt;
  }

  public PendingMatchSummary quality(MatchQualityReport quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * @return quality
   */
  @NotNull @Valid 
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quality")
  public MatchQualityReport getQuality() {
    return quality;
  }

  public void setQuality(MatchQualityReport quality) {
    this.quality = quality;
  }

  public PendingMatchSummary latencyBucket(@Nullable LatencyBucketEnum latencyBucket) {
    this.latencyBucket = latencyBucket;
    return this;
  }

  /**
   * Get latencyBucket
   * @return latencyBucket
   */
  
  @Schema(name = "latencyBucket", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyBucket")
  public @Nullable LatencyBucketEnum getLatencyBucket() {
    return latencyBucket;
  }

  public void setLatencyBucket(@Nullable LatencyBucketEnum latencyBucket) {
    this.latencyBucket = latencyBucket;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendingMatchSummary pendingMatchSummary = (PendingMatchSummary) o;
    return Objects.equals(this.matchId, pendingMatchSummary.matchId) &&
        Objects.equals(this.mode, pendingMatchSummary.mode) &&
        Objects.equals(this.status, pendingMatchSummary.status) &&
        Objects.equals(this.createdAt, pendingMatchSummary.createdAt) &&
        Objects.equals(this.queueSize, pendingMatchSummary.queueSize) &&
        Objects.equals(this.estimatedStartAt, pendingMatchSummary.estimatedStartAt) &&
        Objects.equals(this.quality, pendingMatchSummary.quality) &&
        Objects.equals(this.latencyBucket, pendingMatchSummary.latencyBucket);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchId, mode, status, createdAt, queueSize, estimatedStartAt, quality, latencyBucket);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendingMatchSummary {\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    queueSize: ").append(toIndentedString(queueSize)).append("\n");
    sb.append("    estimatedStartAt: ").append(toIndentedString(estimatedStartAt)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    latencyBucket: ").append(toIndentedString(latencyBucket)).append("\n");
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

