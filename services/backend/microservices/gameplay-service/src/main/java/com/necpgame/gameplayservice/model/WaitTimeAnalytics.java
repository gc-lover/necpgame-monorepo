package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ActivityType;
import com.necpgame.gameplayservice.model.QueueMode;
import com.necpgame.gameplayservice.model.WaitTimeAnalyticsSamplesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WaitTimeAnalytics
 */


public class WaitTimeAnalytics {

  /**
   * Gets or Sets window
   */
  public enum WindowEnum {
    LAST_5_M("LAST_5M"),
    
    LAST_15_M("LAST_15M"),
    
    HOURLY("HOURLY"),
    
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

  private @Nullable ActivityType activityType;

  private @Nullable QueueMode mode;

  private @Nullable String region;

  private Integer averageWaitSeconds;

  private @Nullable Integer percentile50;

  private @Nullable Integer percentile90;

  private Integer activeTickets;

  private @Nullable Float rangeExpansionsPerTicket;

  @Valid
  private Map<String, Integer> priorityDistribution = new HashMap<>();

  @Valid
  private List<@Valid WaitTimeAnalyticsSamplesInner> samples = new ArrayList<>();

  public WaitTimeAnalytics() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WaitTimeAnalytics(WindowEnum window, Integer averageWaitSeconds, Integer activeTickets) {
    this.window = window;
    this.averageWaitSeconds = averageWaitSeconds;
    this.activeTickets = activeTickets;
  }

  public WaitTimeAnalytics window(WindowEnum window) {
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

  public WaitTimeAnalytics activityType(@Nullable ActivityType activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  @Valid 
  @Schema(name = "activityType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activityType")
  public @Nullable ActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(@Nullable ActivityType activityType) {
    this.activityType = activityType;
  }

  public WaitTimeAnalytics mode(@Nullable QueueMode mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @Valid 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public @Nullable QueueMode getMode() {
    return mode;
  }

  public void setMode(@Nullable QueueMode mode) {
    this.mode = mode;
  }

  public WaitTimeAnalytics region(@Nullable String region) {
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

  public WaitTimeAnalytics averageWaitSeconds(Integer averageWaitSeconds) {
    this.averageWaitSeconds = averageWaitSeconds;
    return this;
  }

  /**
   * Get averageWaitSeconds
   * @return averageWaitSeconds
   */
  @NotNull 
  @Schema(name = "averageWaitSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("averageWaitSeconds")
  public Integer getAverageWaitSeconds() {
    return averageWaitSeconds;
  }

  public void setAverageWaitSeconds(Integer averageWaitSeconds) {
    this.averageWaitSeconds = averageWaitSeconds;
  }

  public WaitTimeAnalytics percentile50(@Nullable Integer percentile50) {
    this.percentile50 = percentile50;
    return this;
  }

  /**
   * Get percentile50
   * @return percentile50
   */
  
  @Schema(name = "percentile50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percentile50")
  public @Nullable Integer getPercentile50() {
    return percentile50;
  }

  public void setPercentile50(@Nullable Integer percentile50) {
    this.percentile50 = percentile50;
  }

  public WaitTimeAnalytics percentile90(@Nullable Integer percentile90) {
    this.percentile90 = percentile90;
    return this;
  }

  /**
   * Get percentile90
   * @return percentile90
   */
  
  @Schema(name = "percentile90", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("percentile90")
  public @Nullable Integer getPercentile90() {
    return percentile90;
  }

  public void setPercentile90(@Nullable Integer percentile90) {
    this.percentile90 = percentile90;
  }

  public WaitTimeAnalytics activeTickets(Integer activeTickets) {
    this.activeTickets = activeTickets;
    return this;
  }

  /**
   * Get activeTickets
   * @return activeTickets
   */
  @NotNull 
  @Schema(name = "activeTickets", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activeTickets")
  public Integer getActiveTickets() {
    return activeTickets;
  }

  public void setActiveTickets(Integer activeTickets) {
    this.activeTickets = activeTickets;
  }

  public WaitTimeAnalytics rangeExpansionsPerTicket(@Nullable Float rangeExpansionsPerTicket) {
    this.rangeExpansionsPerTicket = rangeExpansionsPerTicket;
    return this;
  }

  /**
   * Get rangeExpansionsPerTicket
   * @return rangeExpansionsPerTicket
   */
  
  @Schema(name = "rangeExpansionsPerTicket", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rangeExpansionsPerTicket")
  public @Nullable Float getRangeExpansionsPerTicket() {
    return rangeExpansionsPerTicket;
  }

  public void setRangeExpansionsPerTicket(@Nullable Float rangeExpansionsPerTicket) {
    this.rangeExpansionsPerTicket = rangeExpansionsPerTicket;
  }

  public WaitTimeAnalytics priorityDistribution(Map<String, Integer> priorityDistribution) {
    this.priorityDistribution = priorityDistribution;
    return this;
  }

  public WaitTimeAnalytics putPriorityDistributionItem(String key, Integer priorityDistributionItem) {
    if (this.priorityDistribution == null) {
      this.priorityDistribution = new HashMap<>();
    }
    this.priorityDistribution.put(key, priorityDistributionItem);
    return this;
  }

  /**
   * Get priorityDistribution
   * @return priorityDistribution
   */
  
  @Schema(name = "priorityDistribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priorityDistribution")
  public Map<String, Integer> getPriorityDistribution() {
    return priorityDistribution;
  }

  public void setPriorityDistribution(Map<String, Integer> priorityDistribution) {
    this.priorityDistribution = priorityDistribution;
  }

  public WaitTimeAnalytics samples(List<@Valid WaitTimeAnalyticsSamplesInner> samples) {
    this.samples = samples;
    return this;
  }

  public WaitTimeAnalytics addSamplesItem(WaitTimeAnalyticsSamplesInner samplesItem) {
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
  @Valid @Size(max = 100) 
  @Schema(name = "samples", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("samples")
  public List<@Valid WaitTimeAnalyticsSamplesInner> getSamples() {
    return samples;
  }

  public void setSamples(List<@Valid WaitTimeAnalyticsSamplesInner> samples) {
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
    WaitTimeAnalytics waitTimeAnalytics = (WaitTimeAnalytics) o;
    return Objects.equals(this.window, waitTimeAnalytics.window) &&
        Objects.equals(this.activityType, waitTimeAnalytics.activityType) &&
        Objects.equals(this.mode, waitTimeAnalytics.mode) &&
        Objects.equals(this.region, waitTimeAnalytics.region) &&
        Objects.equals(this.averageWaitSeconds, waitTimeAnalytics.averageWaitSeconds) &&
        Objects.equals(this.percentile50, waitTimeAnalytics.percentile50) &&
        Objects.equals(this.percentile90, waitTimeAnalytics.percentile90) &&
        Objects.equals(this.activeTickets, waitTimeAnalytics.activeTickets) &&
        Objects.equals(this.rangeExpansionsPerTicket, waitTimeAnalytics.rangeExpansionsPerTicket) &&
        Objects.equals(this.priorityDistribution, waitTimeAnalytics.priorityDistribution) &&
        Objects.equals(this.samples, waitTimeAnalytics.samples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(window, activityType, mode, region, averageWaitSeconds, percentile50, percentile90, activeTickets, rangeExpansionsPerTicket, priorityDistribution, samples);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WaitTimeAnalytics {\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    averageWaitSeconds: ").append(toIndentedString(averageWaitSeconds)).append("\n");
    sb.append("    percentile50: ").append(toIndentedString(percentile50)).append("\n");
    sb.append("    percentile90: ").append(toIndentedString(percentile90)).append("\n");
    sb.append("    activeTickets: ").append(toIndentedString(activeTickets)).append("\n");
    sb.append("    rangeExpansionsPerTicket: ").append(toIndentedString(rangeExpansionsPerTicket)).append("\n");
    sb.append("    priorityDistribution: ").append(toIndentedString(priorityDistribution)).append("\n");
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

