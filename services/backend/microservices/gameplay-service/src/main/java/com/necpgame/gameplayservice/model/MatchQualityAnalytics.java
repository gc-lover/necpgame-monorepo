package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.MatchQualityAnalyticsSamplesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MatchQualityAnalytics
 */


public class MatchQualityAnalytics {

  /**
   * Gets or Sets window
   */
  public enum WindowEnum {
    LAST_15_M("LAST_15M"),
    
    LAST_HOUR("LAST_HOUR"),
    
    DAILY("DAILY");

    private final String value;

    WindowEnum(String value) {
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
    public static WindowEnum fromValue(String value) {
      for (WindowEnum b : WindowEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private WindowEnum window;

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    PVP_RANKED("PVP_RANKED"),
    
    PVP_CASUAL("PVP_CASUAL"),
    
    PVE_DUNGEON("PVE_DUNGEON"),
    
    RAID("RAID"),
    
    ARENA_EVENT("ARENA_EVENT");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModeEnum mode;

  private @Nullable String region;

  private @Nullable Float averageScore;

  private @Nullable Integer medianWaitSeconds;

  private @Nullable Integer percentile95LatencyMs;

  private @Nullable Integer matchesEvaluated;

  @Valid
  private List<@Valid MatchQualityAnalyticsSamplesInner> samples = new ArrayList<>();

  public MatchQualityAnalytics() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchQualityAnalytics(WindowEnum window, ModeEnum mode, List<@Valid MatchQualityAnalyticsSamplesInner> samples) {
    this.window = window;
    this.mode = mode;
    this.samples = samples;
  }

  public MatchQualityAnalytics window(WindowEnum window) {
    this.window = window;
    return this;
  }

  /**
   * Get window
   * @return window
   */
  @NotNull 
  @Schema(name = "window", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("window")
  public WindowEnum getWindow() {
    return window;
  }

  public void setWindow(WindowEnum window) {
    this.window = window;
  }

  public MatchQualityAnalytics mode(ModeEnum mode) {
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
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public MatchQualityAnalytics region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public MatchQualityAnalytics averageScore(@Nullable Float averageScore) {
    this.averageScore = averageScore;
    return this;
  }

  /**
   * Get averageScore
   * @return averageScore
   */
  
  @Schema(name = "averageScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageScore")
  public @Nullable Float getAverageScore() {
    return averageScore;
  }

  public void setAverageScore(@Nullable Float averageScore) {
    this.averageScore = averageScore;
  }

  public MatchQualityAnalytics medianWaitSeconds(@Nullable Integer medianWaitSeconds) {
    this.medianWaitSeconds = medianWaitSeconds;
    return this;
  }

  /**
   * Get medianWaitSeconds
   * @return medianWaitSeconds
   */
  
  @Schema(name = "medianWaitSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medianWaitSeconds")
  public @Nullable Integer getMedianWaitSeconds() {
    return medianWaitSeconds;
  }

  public void setMedianWaitSeconds(@Nullable Integer medianWaitSeconds) {
    this.medianWaitSeconds = medianWaitSeconds;
  }

  public MatchQualityAnalytics percentile95LatencyMs(@Nullable Integer percentile95LatencyMs) {
    this.percentile95LatencyMs = percentile95LatencyMs;
    return this;
  }

  /**
   * Get percentile95LatencyMs
   * @return percentile95LatencyMs
   */
  
  @Schema(name = "percentile95LatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percentile95LatencyMs")
  public @Nullable Integer getPercentile95LatencyMs() {
    return percentile95LatencyMs;
  }

  public void setPercentile95LatencyMs(@Nullable Integer percentile95LatencyMs) {
    this.percentile95LatencyMs = percentile95LatencyMs;
  }

  public MatchQualityAnalytics matchesEvaluated(@Nullable Integer matchesEvaluated) {
    this.matchesEvaluated = matchesEvaluated;
    return this;
  }

  /**
   * Get matchesEvaluated
   * @return matchesEvaluated
   */
  
  @Schema(name = "matchesEvaluated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matchesEvaluated")
  public @Nullable Integer getMatchesEvaluated() {
    return matchesEvaluated;
  }

  public void setMatchesEvaluated(@Nullable Integer matchesEvaluated) {
    this.matchesEvaluated = matchesEvaluated;
  }

  public MatchQualityAnalytics samples(List<@Valid MatchQualityAnalyticsSamplesInner> samples) {
    this.samples = samples;
    return this;
  }

  public MatchQualityAnalytics addSamplesItem(MatchQualityAnalyticsSamplesInner samplesItem) {
    if (this.samples == null) {
      this.samples = new ArrayList<>();
    }
    this.samples.add(samplesItem);
    return this;
  }

  /**
   * Get samples
   * @return samples
   */
  @NotNull @Valid @Size(max = 100) 
  @Schema(name = "samples", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("samples")
  public List<@Valid MatchQualityAnalyticsSamplesInner> getSamples() {
    return samples;
  }

  public void setSamples(List<@Valid MatchQualityAnalyticsSamplesInner> samples) {
    this.samples = samples;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchQualityAnalytics matchQualityAnalytics = (MatchQualityAnalytics) o;
    return Objects.equals(this.window, matchQualityAnalytics.window) &&
        Objects.equals(this.mode, matchQualityAnalytics.mode) &&
        Objects.equals(this.region, matchQualityAnalytics.region) &&
        Objects.equals(this.averageScore, matchQualityAnalytics.averageScore) &&
        Objects.equals(this.medianWaitSeconds, matchQualityAnalytics.medianWaitSeconds) &&
        Objects.equals(this.percentile95LatencyMs, matchQualityAnalytics.percentile95LatencyMs) &&
        Objects.equals(this.matchesEvaluated, matchQualityAnalytics.matchesEvaluated) &&
        Objects.equals(this.samples, matchQualityAnalytics.samples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(window, mode, region, averageScore, medianWaitSeconds, percentile95LatencyMs, matchesEvaluated, samples);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchQualityAnalytics {\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    averageScore: ").append(toIndentedString(averageScore)).append("\n");
    sb.append("    medianWaitSeconds: ").append(toIndentedString(medianWaitSeconds)).append("\n");
    sb.append("    percentile95LatencyMs: ").append(toIndentedString(percentile95LatencyMs)).append("\n");
    sb.append("    matchesEvaluated: ").append(toIndentedString(matchesEvaluated)).append("\n");
    sb.append("    samples: ").append(toIndentedString(samples)).append("\n");
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

