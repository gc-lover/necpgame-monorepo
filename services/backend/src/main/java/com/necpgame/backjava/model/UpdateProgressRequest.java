package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
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
 * UpdateProgressRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class UpdateProgressRequest {

  private UUID achievementId;

  private String eventType;

  private @Nullable Integer increment;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public UpdateProgressRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateProgressRequest(UUID achievementId, String eventType) {
    this.achievementId = achievementId;
    this.eventType = eventType;
  }

  public UpdateProgressRequest achievementId(UUID achievementId) {
    this.achievementId = achievementId;
    return this;
  }

  /**
   * Get achievementId
   * @return achievementId
   */
  @NotNull @Valid 
  @Schema(name = "achievement_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("achievement_id")
  public UUID getAchievementId() {
    return achievementId;
  }

  public void setAchievementId(UUID achievementId) {
    this.achievementId = achievementId;
  }

  public UpdateProgressRequest eventType(String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  @NotNull 
  @Schema(name = "event_type", example = "enemy_killed", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("event_type")
  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public UpdateProgressRequest increment(@Nullable Integer increment) {
    this.increment = increment;
    return this;
  }

  /**
   * Get increment
   * @return increment
   */
  
  @Schema(name = "increment", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("increment")
  public @Nullable Integer getIncrement() {
    return increment;
  }

  public void setIncrement(@Nullable Integer increment) {
    this.increment = increment;
  }

  public UpdateProgressRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public UpdateProgressRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", example = "{\"enemy_type\":\"boss\",\"location\":\"night_city\"}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
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
    UpdateProgressRequest updateProgressRequest = (UpdateProgressRequest) o;
    return Objects.equals(this.achievementId, updateProgressRequest.achievementId) &&
        Objects.equals(this.eventType, updateProgressRequest.eventType) &&
        Objects.equals(this.increment, updateProgressRequest.increment) &&
        Objects.equals(this.metadata, updateProgressRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(achievementId, eventType, increment, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateProgressRequest {\n");
    sb.append("    achievementId: ").append(toIndentedString(achievementId)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    increment: ").append(toIndentedString(increment)).append("\n");
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

