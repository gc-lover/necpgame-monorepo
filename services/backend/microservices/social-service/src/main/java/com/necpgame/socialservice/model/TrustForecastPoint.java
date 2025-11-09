package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.MoodState;
import com.necpgame.socialservice.model.WorldPulseLink;
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
 * TrustForecastPoint
 */


public class TrustForecastPoint {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Float expectedIndex;

  private MoodState moodState;

  private @Nullable Float confidence;

  private @Nullable WorldPulseLink worldPulseProjection;

  public TrustForecastPoint() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TrustForecastPoint(OffsetDateTime timestamp, Float expectedIndex, MoodState moodState) {
    this.timestamp = timestamp;
    this.expectedIndex = expectedIndex;
    this.moodState = moodState;
  }

  public TrustForecastPoint timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", example = "2077-05-19T12:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public TrustForecastPoint expectedIndex(Float expectedIndex) {
    this.expectedIndex = expectedIndex;
    return this;
  }

  /**
   * Get expectedIndex
   * minimum: 0
   * maximum: 100
   * @return expectedIndex
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "expectedIndex", example = "68.3", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expectedIndex")
  public Float getExpectedIndex() {
    return expectedIndex;
  }

  public void setExpectedIndex(Float expectedIndex) {
    this.expectedIndex = expectedIndex;
  }

  public TrustForecastPoint moodState(MoodState moodState) {
    this.moodState = moodState;
    return this;
  }

  /**
   * Get moodState
   * @return moodState
   */
  @NotNull @Valid 
  @Schema(name = "moodState", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("moodState")
  public MoodState getMoodState() {
    return moodState;
  }

  public void setMoodState(MoodState moodState) {
    this.moodState = moodState;
  }

  public TrustForecastPoint confidence(@Nullable Float confidence) {
    this.confidence = confidence;
    return this;
  }

  /**
   * Get confidence
   * minimum: 0
   * maximum: 1
   * @return confidence
   */
  @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "confidence", example = "0.7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("confidence")
  public @Nullable Float getConfidence() {
    return confidence;
  }

  public void setConfidence(@Nullable Float confidence) {
    this.confidence = confidence;
  }

  public TrustForecastPoint worldPulseProjection(@Nullable WorldPulseLink worldPulseProjection) {
    this.worldPulseProjection = worldPulseProjection;
    return this;
  }

  /**
   * Get worldPulseProjection
   * @return worldPulseProjection
   */
  @Valid 
  @Schema(name = "worldPulseProjection", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldPulseProjection")
  public @Nullable WorldPulseLink getWorldPulseProjection() {
    return worldPulseProjection;
  }

  public void setWorldPulseProjection(@Nullable WorldPulseLink worldPulseProjection) {
    this.worldPulseProjection = worldPulseProjection;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrustForecastPoint trustForecastPoint = (TrustForecastPoint) o;
    return Objects.equals(this.timestamp, trustForecastPoint.timestamp) &&
        Objects.equals(this.expectedIndex, trustForecastPoint.expectedIndex) &&
        Objects.equals(this.moodState, trustForecastPoint.moodState) &&
        Objects.equals(this.confidence, trustForecastPoint.confidence) &&
        Objects.equals(this.worldPulseProjection, trustForecastPoint.worldPulseProjection);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, expectedIndex, moodState, confidence, worldPulseProjection);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrustForecastPoint {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    expectedIndex: ").append(toIndentedString(expectedIndex)).append("\n");
    sb.append("    moodState: ").append(toIndentedString(moodState)).append("\n");
    sb.append("    confidence: ").append(toIndentedString(confidence)).append("\n");
    sb.append("    worldPulseProjection: ").append(toIndentedString(worldPulseProjection)).append("\n");
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

