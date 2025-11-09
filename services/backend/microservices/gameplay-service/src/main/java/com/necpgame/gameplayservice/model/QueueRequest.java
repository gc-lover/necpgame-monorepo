package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ActivityType;
import com.necpgame.gameplayservice.model.QueueMode;
import com.necpgame.gameplayservice.model.QueueRole;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * QueueRequest
 */


public class QueueRequest {

  private ActivityType activityType;

  private QueueMode mode;

  private @Nullable UUID partyId;

  private Integer partySize;

  private @Nullable QueueRole preferredRole;

  private Boolean canFill = false;

  private Integer minLevel;

  private Integer maxLevel;

  private @Nullable Integer estimatedSkill;

  private Integer ratingRange = 200;

  private Boolean allowCrossRegion = false;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private Map<String, String> metadata = new HashMap<>();

  public QueueRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueRequest(ActivityType activityType, QueueMode mode, Integer partySize, Integer minLevel, Integer maxLevel) {
    this.activityType = activityType;
    this.mode = mode;
    this.partySize = partySize;
    this.minLevel = minLevel;
    this.maxLevel = maxLevel;
  }

  public QueueRequest activityType(ActivityType activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  @NotNull @Valid 
  @Schema(name = "activityType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityType")
  public ActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityType activityType) {
    this.activityType = activityType;
  }

  public QueueRequest mode(QueueMode mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull @Valid 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public QueueMode getMode() {
    return mode;
  }

  public void setMode(QueueMode mode) {
    this.mode = mode;
  }

  public QueueRequest partyId(@Nullable UUID partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @Valid 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable UUID getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable UUID partyId) {
    this.partyId = partyId;
  }

  public QueueRequest partySize(Integer partySize) {
    this.partySize = partySize;
    return this;
  }

  /**
   * Get partySize
   * minimum: 1
   * maximum: 15
   * @return partySize
   */
  @NotNull @Min(value = 1) @Max(value = 15) 
  @Schema(name = "partySize", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("partySize")
  public Integer getPartySize() {
    return partySize;
  }

  public void setPartySize(Integer partySize) {
    this.partySize = partySize;
  }

  public QueueRequest preferredRole(@Nullable QueueRole preferredRole) {
    this.preferredRole = preferredRole;
    return this;
  }

  /**
   * Get preferredRole
   * @return preferredRole
   */
  @Valid 
  @Schema(name = "preferredRole", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredRole")
  public @Nullable QueueRole getPreferredRole() {
    return preferredRole;
  }

  public void setPreferredRole(@Nullable QueueRole preferredRole) {
    this.preferredRole = preferredRole;
  }

  public QueueRequest canFill(Boolean canFill) {
    this.canFill = canFill;
    return this;
  }

  /**
   * Get canFill
   * @return canFill
   */
  
  @Schema(name = "canFill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("canFill")
  public Boolean getCanFill() {
    return canFill;
  }

  public void setCanFill(Boolean canFill) {
    this.canFill = canFill;
  }

  public QueueRequest minLevel(Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Get minLevel
   * minimum: 1
   * maximum: 200
   * @return minLevel
   */
  @NotNull @Min(value = 1) @Max(value = 200) 
  @Schema(name = "minLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("minLevel")
  public Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(Integer minLevel) {
    this.minLevel = minLevel;
  }

  public QueueRequest maxLevel(Integer maxLevel) {
    this.maxLevel = maxLevel;
    return this;
  }

  /**
   * Get maxLevel
   * minimum: 1
   * maximum: 200
   * @return maxLevel
   */
  @NotNull @Min(value = 1) @Max(value = 200) 
  @Schema(name = "maxLevel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxLevel")
  public Integer getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(Integer maxLevel) {
    this.maxLevel = maxLevel;
  }

  public QueueRequest estimatedSkill(@Nullable Integer estimatedSkill) {
    this.estimatedSkill = estimatedSkill;
    return this;
  }

  /**
   * Get estimatedSkill
   * minimum: 0
   * @return estimatedSkill
   */
  @Min(value = 0) 
  @Schema(name = "estimatedSkill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedSkill")
  public @Nullable Integer getEstimatedSkill() {
    return estimatedSkill;
  }

  public void setEstimatedSkill(@Nullable Integer estimatedSkill) {
    this.estimatedSkill = estimatedSkill;
  }

  public QueueRequest ratingRange(Integer ratingRange) {
    this.ratingRange = ratingRange;
    return this;
  }

  /**
   * Get ratingRange
   * minimum: 50
   * maximum: 1000
   * @return ratingRange
   */
  @Min(value = 50) @Max(value = 1000) 
  @Schema(name = "ratingRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ratingRange")
  public Integer getRatingRange() {
    return ratingRange;
  }

  public void setRatingRange(Integer ratingRange) {
    this.ratingRange = ratingRange;
  }

  public QueueRequest allowCrossRegion(Boolean allowCrossRegion) {
    this.allowCrossRegion = allowCrossRegion;
    return this;
  }

  /**
   * Get allowCrossRegion
   * @return allowCrossRegion
   */
  
  @Schema(name = "allowCrossRegion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowCrossRegion")
  public Boolean getAllowCrossRegion() {
    return allowCrossRegion;
  }

  public void setAllowCrossRegion(Boolean allowCrossRegion) {
    this.allowCrossRegion = allowCrossRegion;
  }

  public QueueRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public QueueRequest metadata(Map<String, String> metadata) {
    this.metadata = metadata;
    return this;
  }

  public QueueRequest putMetadataItem(String key, String metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Дополнительные атрибуты (client build, platform).
   * @return metadata
   */
  
  @Schema(name = "metadata", description = "Дополнительные атрибуты (client build, platform).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, String> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueueRequest queueRequest = (QueueRequest) o;
    return Objects.equals(this.activityType, queueRequest.activityType) &&
        Objects.equals(this.mode, queueRequest.mode) &&
        Objects.equals(this.partyId, queueRequest.partyId) &&
        Objects.equals(this.partySize, queueRequest.partySize) &&
        Objects.equals(this.preferredRole, queueRequest.preferredRole) &&
        Objects.equals(this.canFill, queueRequest.canFill) &&
        Objects.equals(this.minLevel, queueRequest.minLevel) &&
        Objects.equals(this.maxLevel, queueRequest.maxLevel) &&
        Objects.equals(this.estimatedSkill, queueRequest.estimatedSkill) &&
        Objects.equals(this.ratingRange, queueRequest.ratingRange) &&
        Objects.equals(this.allowCrossRegion, queueRequest.allowCrossRegion) &&
        Objects.equals(this.expiresAt, queueRequest.expiresAt) &&
        Objects.equals(this.metadata, queueRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activityType, mode, partyId, partySize, preferredRole, canFill, minLevel, maxLevel, estimatedSkill, ratingRange, allowCrossRegion, expiresAt, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueRequest {\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    partySize: ").append(toIndentedString(partySize)).append("\n");
    sb.append("    preferredRole: ").append(toIndentedString(preferredRole)).append("\n");
    sb.append("    canFill: ").append(toIndentedString(canFill)).append("\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    maxLevel: ").append(toIndentedString(maxLevel)).append("\n");
    sb.append("    estimatedSkill: ").append(toIndentedString(estimatedSkill)).append("\n");
    sb.append("    ratingRange: ").append(toIndentedString(ratingRange)).append("\n");
    sb.append("    allowCrossRegion: ").append(toIndentedString(allowCrossRegion)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

