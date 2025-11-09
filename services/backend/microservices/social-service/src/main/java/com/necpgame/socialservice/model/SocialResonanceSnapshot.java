package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.MoodState;
import com.necpgame.socialservice.model.ResonanceSourceBreakdown;
import com.necpgame.socialservice.model.SocialCampaign;
import com.necpgame.socialservice.model.TrustForecast;
import com.necpgame.socialservice.model.WorldPulseLink;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * SocialResonanceSnapshot
 */


public class SocialResonanceSnapshot {

  private Float trustIndex;

  private MoodState moodState;

  private WorldPulseLink worldPulse;

  @Valid
  private List<@Valid ResonanceSourceBreakdown> resonanceSources = new ArrayList<>();

  @Valid
  private List<@Valid SocialCampaign> activeCampaigns = new ArrayList<>();

  private @Nullable TrustForecast forecast;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  public SocialResonanceSnapshot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialResonanceSnapshot(Float trustIndex, MoodState moodState, WorldPulseLink worldPulse, List<@Valid ResonanceSourceBreakdown> resonanceSources, OffsetDateTime updatedAt) {
    this.trustIndex = trustIndex;
    this.moodState = moodState;
    this.worldPulse = worldPulse;
    this.resonanceSources = resonanceSources;
    this.updatedAt = updatedAt;
  }

  public SocialResonanceSnapshot trustIndex(Float trustIndex) {
    this.trustIndex = trustIndex;
    return this;
  }

  /**
   * Get trustIndex
   * minimum: 0
   * maximum: 100
   * @return trustIndex
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "trustIndex", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trustIndex")
  public Float getTrustIndex() {
    return trustIndex;
  }

  public void setTrustIndex(Float trustIndex) {
    this.trustIndex = trustIndex;
  }

  public SocialResonanceSnapshot moodState(MoodState moodState) {
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

  public SocialResonanceSnapshot worldPulse(WorldPulseLink worldPulse) {
    this.worldPulse = worldPulse;
    return this;
  }

  /**
   * Get worldPulse
   * @return worldPulse
   */
  @NotNull @Valid 
  @Schema(name = "worldPulse", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("worldPulse")
  public WorldPulseLink getWorldPulse() {
    return worldPulse;
  }

  public void setWorldPulse(WorldPulseLink worldPulse) {
    this.worldPulse = worldPulse;
  }

  public SocialResonanceSnapshot resonanceSources(List<@Valid ResonanceSourceBreakdown> resonanceSources) {
    this.resonanceSources = resonanceSources;
    return this;
  }

  public SocialResonanceSnapshot addResonanceSourcesItem(ResonanceSourceBreakdown resonanceSourcesItem) {
    if (this.resonanceSources == null) {
      this.resonanceSources = new ArrayList<>();
    }
    this.resonanceSources.add(resonanceSourcesItem);
    return this;
  }

  /**
   * Get resonanceSources
   * @return resonanceSources
   */
  @NotNull @Valid 
  @Schema(name = "resonanceSources", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resonanceSources")
  public List<@Valid ResonanceSourceBreakdown> getResonanceSources() {
    return resonanceSources;
  }

  public void setResonanceSources(List<@Valid ResonanceSourceBreakdown> resonanceSources) {
    this.resonanceSources = resonanceSources;
  }

  public SocialResonanceSnapshot activeCampaigns(List<@Valid SocialCampaign> activeCampaigns) {
    this.activeCampaigns = activeCampaigns;
    return this;
  }

  public SocialResonanceSnapshot addActiveCampaignsItem(SocialCampaign activeCampaignsItem) {
    if (this.activeCampaigns == null) {
      this.activeCampaigns = new ArrayList<>();
    }
    this.activeCampaigns.add(activeCampaignsItem);
    return this;
  }

  /**
   * Get activeCampaigns
   * @return activeCampaigns
   */
  @Valid 
  @Schema(name = "activeCampaigns", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeCampaigns")
  public List<@Valid SocialCampaign> getActiveCampaigns() {
    return activeCampaigns;
  }

  public void setActiveCampaigns(List<@Valid SocialCampaign> activeCampaigns) {
    this.activeCampaigns = activeCampaigns;
  }

  public SocialResonanceSnapshot forecast(@Nullable TrustForecast forecast) {
    this.forecast = forecast;
    return this;
  }

  /**
   * Get forecast
   * @return forecast
   */
  @Valid 
  @Schema(name = "forecast", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("forecast")
  public @Nullable TrustForecast getForecast() {
    return forecast;
  }

  public void setForecast(@Nullable TrustForecast forecast) {
    this.forecast = forecast;
  }

  public SocialResonanceSnapshot updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialResonanceSnapshot socialResonanceSnapshot = (SocialResonanceSnapshot) o;
    return Objects.equals(this.trustIndex, socialResonanceSnapshot.trustIndex) &&
        Objects.equals(this.moodState, socialResonanceSnapshot.moodState) &&
        Objects.equals(this.worldPulse, socialResonanceSnapshot.worldPulse) &&
        Objects.equals(this.resonanceSources, socialResonanceSnapshot.resonanceSources) &&
        Objects.equals(this.activeCampaigns, socialResonanceSnapshot.activeCampaigns) &&
        Objects.equals(this.forecast, socialResonanceSnapshot.forecast) &&
        Objects.equals(this.updatedAt, socialResonanceSnapshot.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trustIndex, moodState, worldPulse, resonanceSources, activeCampaigns, forecast, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialResonanceSnapshot {\n");
    sb.append("    trustIndex: ").append(toIndentedString(trustIndex)).append("\n");
    sb.append("    moodState: ").append(toIndentedString(moodState)).append("\n");
    sb.append("    worldPulse: ").append(toIndentedString(worldPulse)).append("\n");
    sb.append("    resonanceSources: ").append(toIndentedString(resonanceSources)).append("\n");
    sb.append("    activeCampaigns: ").append(toIndentedString(activeCampaigns)).append("\n");
    sb.append("    forecast: ").append(toIndentedString(forecast)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

