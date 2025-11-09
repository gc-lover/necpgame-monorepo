package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.AuditMetadata;
import com.necpgame.socialservice.model.SocialCampaignStatus;
import com.necpgame.socialservice.model.TrustForecast;
import com.necpgame.socialservice.model.WorldPulseLink;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SocialCampaignLaunchResponse
 */


public class SocialCampaignLaunchResponse {

  private String campaignId;

  private SocialCampaignStatus status;

  private @Nullable Boolean simulate;

  private Float expectedDelta;

  private WorldPulseLink worldPulsePreview;

  private @Nullable AuditMetadata approval;

  private @Nullable TrustForecast forecast;

  public SocialCampaignLaunchResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialCampaignLaunchResponse(String campaignId, SocialCampaignStatus status, Float expectedDelta, WorldPulseLink worldPulsePreview) {
    this.campaignId = campaignId;
    this.status = status;
    this.expectedDelta = expectedDelta;
    this.worldPulsePreview = worldPulsePreview;
  }

  public SocialCampaignLaunchResponse campaignId(String campaignId) {
    this.campaignId = campaignId;
    return this;
  }

  /**
   * Get campaignId
   * @return campaignId
   */
  @NotNull 
  @Schema(name = "campaignId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("campaignId")
  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public SocialCampaignLaunchResponse status(SocialCampaignStatus status) {
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
  public SocialCampaignStatus getStatus() {
    return status;
  }

  public void setStatus(SocialCampaignStatus status) {
    this.status = status;
  }

  public SocialCampaignLaunchResponse simulate(@Nullable Boolean simulate) {
    this.simulate = simulate;
    return this;
  }

  /**
   * Get simulate
   * @return simulate
   */
  
  @Schema(name = "simulate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("simulate")
  public @Nullable Boolean getSimulate() {
    return simulate;
  }

  public void setSimulate(@Nullable Boolean simulate) {
    this.simulate = simulate;
  }

  public SocialCampaignLaunchResponse expectedDelta(Float expectedDelta) {
    this.expectedDelta = expectedDelta;
    return this;
  }

  /**
   * Get expectedDelta
   * @return expectedDelta
   */
  @NotNull 
  @Schema(name = "expectedDelta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expectedDelta")
  public Float getExpectedDelta() {
    return expectedDelta;
  }

  public void setExpectedDelta(Float expectedDelta) {
    this.expectedDelta = expectedDelta;
  }

  public SocialCampaignLaunchResponse worldPulsePreview(WorldPulseLink worldPulsePreview) {
    this.worldPulsePreview = worldPulsePreview;
    return this;
  }

  /**
   * Get worldPulsePreview
   * @return worldPulsePreview
   */
  @NotNull @Valid 
  @Schema(name = "worldPulsePreview", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("worldPulsePreview")
  public WorldPulseLink getWorldPulsePreview() {
    return worldPulsePreview;
  }

  public void setWorldPulsePreview(WorldPulseLink worldPulsePreview) {
    this.worldPulsePreview = worldPulsePreview;
  }

  public SocialCampaignLaunchResponse approval(@Nullable AuditMetadata approval) {
    this.approval = approval;
    return this;
  }

  /**
   * Get approval
   * @return approval
   */
  @Valid 
  @Schema(name = "approval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approval")
  public @Nullable AuditMetadata getApproval() {
    return approval;
  }

  public void setApproval(@Nullable AuditMetadata approval) {
    this.approval = approval;
  }

  public SocialCampaignLaunchResponse forecast(@Nullable TrustForecast forecast) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialCampaignLaunchResponse socialCampaignLaunchResponse = (SocialCampaignLaunchResponse) o;
    return Objects.equals(this.campaignId, socialCampaignLaunchResponse.campaignId) &&
        Objects.equals(this.status, socialCampaignLaunchResponse.status) &&
        Objects.equals(this.simulate, socialCampaignLaunchResponse.simulate) &&
        Objects.equals(this.expectedDelta, socialCampaignLaunchResponse.expectedDelta) &&
        Objects.equals(this.worldPulsePreview, socialCampaignLaunchResponse.worldPulsePreview) &&
        Objects.equals(this.approval, socialCampaignLaunchResponse.approval) &&
        Objects.equals(this.forecast, socialCampaignLaunchResponse.forecast);
  }

  @Override
  public int hashCode() {
    return Objects.hash(campaignId, status, simulate, expectedDelta, worldPulsePreview, approval, forecast);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialCampaignLaunchResponse {\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    simulate: ").append(toIndentedString(simulate)).append("\n");
    sb.append("    expectedDelta: ").append(toIndentedString(expectedDelta)).append("\n");
    sb.append("    worldPulsePreview: ").append(toIndentedString(worldPulsePreview)).append("\n");
    sb.append("    approval: ").append(toIndentedString(approval)).append("\n");
    sb.append("    forecast: ").append(toIndentedString(forecast)).append("\n");
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

