package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.AuditMetadata;
import com.necpgame.socialservice.model.SocialCampaign;
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
 * SocialCampaignStartedEvent
 */


public class SocialCampaignStartedEvent {

  private String eventId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime occurredAt;

  private SocialCampaign campaign;

  private Float expectedDelta;

  private @Nullable Boolean simulate;

  private @Nullable AuditMetadata approval;

  public SocialCampaignStartedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialCampaignStartedEvent(String eventId, OffsetDateTime occurredAt, SocialCampaign campaign, Float expectedDelta) {
    this.eventId = eventId;
    this.occurredAt = occurredAt;
    this.campaign = campaign;
    this.expectedDelta = expectedDelta;
  }

  public SocialCampaignStartedEvent eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public SocialCampaignStartedEvent occurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @NotNull @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("occurredAt")
  public OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  public SocialCampaignStartedEvent campaign(SocialCampaign campaign) {
    this.campaign = campaign;
    return this;
  }

  /**
   * Get campaign
   * @return campaign
   */
  @NotNull @Valid 
  @Schema(name = "campaign", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("campaign")
  public SocialCampaign getCampaign() {
    return campaign;
  }

  public void setCampaign(SocialCampaign campaign) {
    this.campaign = campaign;
  }

  public SocialCampaignStartedEvent expectedDelta(Float expectedDelta) {
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

  public SocialCampaignStartedEvent simulate(@Nullable Boolean simulate) {
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

  public SocialCampaignStartedEvent approval(@Nullable AuditMetadata approval) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialCampaignStartedEvent socialCampaignStartedEvent = (SocialCampaignStartedEvent) o;
    return Objects.equals(this.eventId, socialCampaignStartedEvent.eventId) &&
        Objects.equals(this.occurredAt, socialCampaignStartedEvent.occurredAt) &&
        Objects.equals(this.campaign, socialCampaignStartedEvent.campaign) &&
        Objects.equals(this.expectedDelta, socialCampaignStartedEvent.expectedDelta) &&
        Objects.equals(this.simulate, socialCampaignStartedEvent.simulate) &&
        Objects.equals(this.approval, socialCampaignStartedEvent.approval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, occurredAt, campaign, expectedDelta, simulate, approval);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialCampaignStartedEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    campaign: ").append(toIndentedString(campaign)).append("\n");
    sb.append("    expectedDelta: ").append(toIndentedString(expectedDelta)).append("\n");
    sb.append("    simulate: ").append(toIndentedString(simulate)).append("\n");
    sb.append("    approval: ").append(toIndentedString(approval)).append("\n");
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

