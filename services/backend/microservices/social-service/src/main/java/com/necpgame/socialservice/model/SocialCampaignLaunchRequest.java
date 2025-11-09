package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SocialCampaignLaunchRequest
 */


public class SocialCampaignLaunchRequest {

  private String campaignId;

  private String initiatedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> scheduledAt = JsonNullable.<OffsetDateTime>undefined();

  private JsonNullable<String> approvalToken = JsonNullable.<String>undefined();

  private JsonNullable<String> crisisFallbackPlan = JsonNullable.<String>undefined();

  public SocialCampaignLaunchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialCampaignLaunchRequest(String campaignId, String initiatedBy) {
    this.campaignId = campaignId;
    this.initiatedBy = initiatedBy;
  }

  public SocialCampaignLaunchRequest campaignId(String campaignId) {
    this.campaignId = campaignId;
    return this;
  }

  /**
   * Get campaignId
   * @return campaignId
   */
  @NotNull 
  @Schema(name = "campaignId", example = "soc-camp-b42", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("campaignId")
  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public SocialCampaignLaunchRequest initiatedBy(String initiatedBy) {
    this.initiatedBy = initiatedBy;
    return this;
  }

  /**
   * Get initiatedBy
   * @return initiatedBy
   */
  @NotNull 
  @Schema(name = "initiatedBy", example = "guild-emberfox", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("initiatedBy")
  public String getInitiatedBy() {
    return initiatedBy;
  }

  public void setInitiatedBy(String initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  public SocialCampaignLaunchRequest scheduledAt(OffsetDateTime scheduledAt) {
    this.scheduledAt = JsonNullable.of(scheduledAt);
    return this;
  }

  /**
   * Get scheduledAt
   * @return scheduledAt
   */
  @Valid 
  @Schema(name = "scheduledAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledAt")
  public JsonNullable<OffsetDateTime> getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(JsonNullable<OffsetDateTime> scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  public SocialCampaignLaunchRequest approvalToken(String approvalToken) {
    this.approvalToken = JsonNullable.of(approvalToken);
    return this;
  }

  /**
   * Get approvalToken
   * @return approvalToken
   */
  
  @Schema(name = "approvalToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvalToken")
  public JsonNullable<String> getApprovalToken() {
    return approvalToken;
  }

  public void setApprovalToken(JsonNullable<String> approvalToken) {
    this.approvalToken = approvalToken;
  }

  public SocialCampaignLaunchRequest crisisFallbackPlan(String crisisFallbackPlan) {
    this.crisisFallbackPlan = JsonNullable.of(crisisFallbackPlan);
    return this;
  }

  /**
   * Get crisisFallbackPlan
   * @return crisisFallbackPlan
   */
  
  @Schema(name = "crisisFallbackPlan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crisisFallbackPlan")
  public JsonNullable<String> getCrisisFallbackPlan() {
    return crisisFallbackPlan;
  }

  public void setCrisisFallbackPlan(JsonNullable<String> crisisFallbackPlan) {
    this.crisisFallbackPlan = crisisFallbackPlan;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialCampaignLaunchRequest socialCampaignLaunchRequest = (SocialCampaignLaunchRequest) o;
    return Objects.equals(this.campaignId, socialCampaignLaunchRequest.campaignId) &&
        Objects.equals(this.initiatedBy, socialCampaignLaunchRequest.initiatedBy) &&
        equalsNullable(this.scheduledAt, socialCampaignLaunchRequest.scheduledAt) &&
        equalsNullable(this.approvalToken, socialCampaignLaunchRequest.approvalToken) &&
        equalsNullable(this.crisisFallbackPlan, socialCampaignLaunchRequest.crisisFallbackPlan);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(campaignId, initiatedBy, hashCodeNullable(scheduledAt), hashCodeNullable(approvalToken), hashCodeNullable(crisisFallbackPlan));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialCampaignLaunchRequest {\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
    sb.append("    initiatedBy: ").append(toIndentedString(initiatedBy)).append("\n");
    sb.append("    scheduledAt: ").append(toIndentedString(scheduledAt)).append("\n");
    sb.append("    approvalToken: ").append(toIndentedString(approvalToken)).append("\n");
    sb.append("    crisisFallbackPlan: ").append(toIndentedString(crisisFallbackPlan)).append("\n");
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

