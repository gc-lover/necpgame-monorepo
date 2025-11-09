package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.AnalyticsResponseFeedbackStats;
import java.math.BigDecimal;
import java.util.HashMap;
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
 * AnalyticsResponse
 */


public class AnalyticsResponse {

  private @Nullable String announcementId;

  private @Nullable String range;

  @Valid
  private Map<String, Integer> viewsByChannel = new HashMap<>();

  private @Nullable BigDecimal openRate;

  private @Nullable BigDecimal clickThroughRate;

  private @Nullable Integer ackCount;

  private @Nullable Integer unsubscribeCount;

  private @Nullable AnalyticsResponseFeedbackStats feedbackStats;

  public AnalyticsResponse announcementId(@Nullable String announcementId) {
    this.announcementId = announcementId;
    return this;
  }

  /**
   * Get announcementId
   * @return announcementId
   */
  
  @Schema(name = "announcementId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("announcementId")
  public @Nullable String getAnnouncementId() {
    return announcementId;
  }

  public void setAnnouncementId(@Nullable String announcementId) {
    this.announcementId = announcementId;
  }

  public AnalyticsResponse range(@Nullable String range) {
    this.range = range;
    return this;
  }

  /**
   * Get range
   * @return range
   */
  
  @Schema(name = "range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable String getRange() {
    return range;
  }

  public void setRange(@Nullable String range) {
    this.range = range;
  }

  public AnalyticsResponse viewsByChannel(Map<String, Integer> viewsByChannel) {
    this.viewsByChannel = viewsByChannel;
    return this;
  }

  public AnalyticsResponse putViewsByChannelItem(String key, Integer viewsByChannelItem) {
    if (this.viewsByChannel == null) {
      this.viewsByChannel = new HashMap<>();
    }
    this.viewsByChannel.put(key, viewsByChannelItem);
    return this;
  }

  /**
   * Get viewsByChannel
   * @return viewsByChannel
   */
  
  @Schema(name = "viewsByChannel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("viewsByChannel")
  public Map<String, Integer> getViewsByChannel() {
    return viewsByChannel;
  }

  public void setViewsByChannel(Map<String, Integer> viewsByChannel) {
    this.viewsByChannel = viewsByChannel;
  }

  public AnalyticsResponse openRate(@Nullable BigDecimal openRate) {
    this.openRate = openRate;
    return this;
  }

  /**
   * Get openRate
   * @return openRate
   */
  @Valid 
  @Schema(name = "openRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("openRate")
  public @Nullable BigDecimal getOpenRate() {
    return openRate;
  }

  public void setOpenRate(@Nullable BigDecimal openRate) {
    this.openRate = openRate;
  }

  public AnalyticsResponse clickThroughRate(@Nullable BigDecimal clickThroughRate) {
    this.clickThroughRate = clickThroughRate;
    return this;
  }

  /**
   * Get clickThroughRate
   * @return clickThroughRate
   */
  @Valid 
  @Schema(name = "clickThroughRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clickThroughRate")
  public @Nullable BigDecimal getClickThroughRate() {
    return clickThroughRate;
  }

  public void setClickThroughRate(@Nullable BigDecimal clickThroughRate) {
    this.clickThroughRate = clickThroughRate;
  }

  public AnalyticsResponse ackCount(@Nullable Integer ackCount) {
    this.ackCount = ackCount;
    return this;
  }

  /**
   * Get ackCount
   * @return ackCount
   */
  
  @Schema(name = "ackCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ackCount")
  public @Nullable Integer getAckCount() {
    return ackCount;
  }

  public void setAckCount(@Nullable Integer ackCount) {
    this.ackCount = ackCount;
  }

  public AnalyticsResponse unsubscribeCount(@Nullable Integer unsubscribeCount) {
    this.unsubscribeCount = unsubscribeCount;
    return this;
  }

  /**
   * Get unsubscribeCount
   * @return unsubscribeCount
   */
  
  @Schema(name = "unsubscribeCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unsubscribeCount")
  public @Nullable Integer getUnsubscribeCount() {
    return unsubscribeCount;
  }

  public void setUnsubscribeCount(@Nullable Integer unsubscribeCount) {
    this.unsubscribeCount = unsubscribeCount;
  }

  public AnalyticsResponse feedbackStats(@Nullable AnalyticsResponseFeedbackStats feedbackStats) {
    this.feedbackStats = feedbackStats;
    return this;
  }

  /**
   * Get feedbackStats
   * @return feedbackStats
   */
  @Valid 
  @Schema(name = "feedbackStats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("feedbackStats")
  public @Nullable AnalyticsResponseFeedbackStats getFeedbackStats() {
    return feedbackStats;
  }

  public void setFeedbackStats(@Nullable AnalyticsResponseFeedbackStats feedbackStats) {
    this.feedbackStats = feedbackStats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponse analyticsResponse = (AnalyticsResponse) o;
    return Objects.equals(this.announcementId, analyticsResponse.announcementId) &&
        Objects.equals(this.range, analyticsResponse.range) &&
        Objects.equals(this.viewsByChannel, analyticsResponse.viewsByChannel) &&
        Objects.equals(this.openRate, analyticsResponse.openRate) &&
        Objects.equals(this.clickThroughRate, analyticsResponse.clickThroughRate) &&
        Objects.equals(this.ackCount, analyticsResponse.ackCount) &&
        Objects.equals(this.unsubscribeCount, analyticsResponse.unsubscribeCount) &&
        Objects.equals(this.feedbackStats, analyticsResponse.feedbackStats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(announcementId, range, viewsByChannel, openRate, clickThroughRate, ackCount, unsubscribeCount, feedbackStats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponse {\n");
    sb.append("    announcementId: ").append(toIndentedString(announcementId)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    viewsByChannel: ").append(toIndentedString(viewsByChannel)).append("\n");
    sb.append("    openRate: ").append(toIndentedString(openRate)).append("\n");
    sb.append("    clickThroughRate: ").append(toIndentedString(clickThroughRate)).append("\n");
    sb.append("    ackCount: ").append(toIndentedString(ackCount)).append("\n");
    sb.append("    unsubscribeCount: ").append(toIndentedString(unsubscribeCount)).append("\n");
    sb.append("    feedbackStats: ").append(toIndentedString(feedbackStats)).append("\n");
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

