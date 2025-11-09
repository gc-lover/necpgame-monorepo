package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.AuditMetadata;
import com.necpgame.socialservice.model.CampaignEffect;
import com.necpgame.socialservice.model.CampaignTier;
import com.necpgame.socialservice.model.SocialCampaignCategory;
import com.necpgame.socialservice.model.SocialCampaignCost;
import com.necpgame.socialservice.model.SocialCampaignRequirement;
import com.necpgame.socialservice.model.SocialCampaignStatus;
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
 * SocialCampaign
 */


public class SocialCampaign {

  private String campaignId;

  private String name;

  private @Nullable String description;

  private SocialCampaignCategory category;

  private CampaignTier tier;

  private SocialCampaignStatus status;

  private Float expectedDelta;

  private Integer cooldownHours;

  private @Nullable Integer durationHours;

  private SocialCampaignCost cost;

  @Valid
  private List<@Valid SocialCampaignRequirement> requirements = new ArrayList<>();

  @Valid
  private List<@Valid CampaignEffect> effects = new ArrayList<>();

  private @Nullable AuditMetadata approval;

  public SocialCampaign() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialCampaign(String campaignId, String name, SocialCampaignCategory category, CampaignTier tier, SocialCampaignStatus status, Float expectedDelta, Integer cooldownHours, SocialCampaignCost cost, List<@Valid CampaignEffect> effects) {
    this.campaignId = campaignId;
    this.name = name;
    this.category = category;
    this.tier = tier;
    this.status = status;
    this.expectedDelta = expectedDelta;
    this.cooldownHours = cooldownHours;
    this.cost = cost;
    this.effects = effects;
  }

  public SocialCampaign campaignId(String campaignId) {
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

  public SocialCampaign name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Night City Mutual Aid", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SocialCampaign description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public SocialCampaign category(SocialCampaignCategory category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull @Valid 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public SocialCampaignCategory getCategory() {
    return category;
  }

  public void setCategory(SocialCampaignCategory category) {
    this.category = category;
  }

  public SocialCampaign tier(CampaignTier tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  @NotNull @Valid 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tier")
  public CampaignTier getTier() {
    return tier;
  }

  public void setTier(CampaignTier tier) {
    this.tier = tier;
  }

  public SocialCampaign status(SocialCampaignStatus status) {
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

  public SocialCampaign expectedDelta(Float expectedDelta) {
    this.expectedDelta = expectedDelta;
    return this;
  }

  /**
   * Get expectedDelta
   * @return expectedDelta
   */
  @NotNull 
  @Schema(name = "expectedDelta", example = "9.4", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expectedDelta")
  public Float getExpectedDelta() {
    return expectedDelta;
  }

  public void setExpectedDelta(Float expectedDelta) {
    this.expectedDelta = expectedDelta;
  }

  public SocialCampaign cooldownHours(Integer cooldownHours) {
    this.cooldownHours = cooldownHours;
    return this;
  }

  /**
   * Get cooldownHours
   * minimum: 0
   * @return cooldownHours
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "cooldownHours", example = "72", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cooldownHours")
  public Integer getCooldownHours() {
    return cooldownHours;
  }

  public void setCooldownHours(Integer cooldownHours) {
    this.cooldownHours = cooldownHours;
  }

  public SocialCampaign durationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Get durationHours
   * minimum: 1
   * @return durationHours
   */
  @Min(value = 1) 
  @Schema(name = "durationHours", example = "24", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationHours")
  public @Nullable Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
  }

  public SocialCampaign cost(SocialCampaignCost cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  @NotNull @Valid 
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public SocialCampaignCost getCost() {
    return cost;
  }

  public void setCost(SocialCampaignCost cost) {
    this.cost = cost;
  }

  public SocialCampaign requirements(List<@Valid SocialCampaignRequirement> requirements) {
    this.requirements = requirements;
    return this;
  }

  public SocialCampaign addRequirementsItem(SocialCampaignRequirement requirementsItem) {
    if (this.requirements == null) {
      this.requirements = new ArrayList<>();
    }
    this.requirements.add(requirementsItem);
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public List<@Valid SocialCampaignRequirement> getRequirements() {
    return requirements;
  }

  public void setRequirements(List<@Valid SocialCampaignRequirement> requirements) {
    this.requirements = requirements;
  }

  public SocialCampaign effects(List<@Valid CampaignEffect> effects) {
    this.effects = effects;
    return this;
  }

  public SocialCampaign addEffectsItem(CampaignEffect effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Get effects
   * @return effects
   */
  @NotNull @Valid 
  @Schema(name = "effects", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effects")
  public List<@Valid CampaignEffect> getEffects() {
    return effects;
  }

  public void setEffects(List<@Valid CampaignEffect> effects) {
    this.effects = effects;
  }

  public SocialCampaign approval(@Nullable AuditMetadata approval) {
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
    SocialCampaign socialCampaign = (SocialCampaign) o;
    return Objects.equals(this.campaignId, socialCampaign.campaignId) &&
        Objects.equals(this.name, socialCampaign.name) &&
        Objects.equals(this.description, socialCampaign.description) &&
        Objects.equals(this.category, socialCampaign.category) &&
        Objects.equals(this.tier, socialCampaign.tier) &&
        Objects.equals(this.status, socialCampaign.status) &&
        Objects.equals(this.expectedDelta, socialCampaign.expectedDelta) &&
        Objects.equals(this.cooldownHours, socialCampaign.cooldownHours) &&
        Objects.equals(this.durationHours, socialCampaign.durationHours) &&
        Objects.equals(this.cost, socialCampaign.cost) &&
        Objects.equals(this.requirements, socialCampaign.requirements) &&
        Objects.equals(this.effects, socialCampaign.effects) &&
        Objects.equals(this.approval, socialCampaign.approval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(campaignId, name, description, category, tier, status, expectedDelta, cooldownHours, durationHours, cost, requirements, effects, approval);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialCampaign {\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    expectedDelta: ").append(toIndentedString(expectedDelta)).append("\n");
    sb.append("    cooldownHours: ").append(toIndentedString(cooldownHours)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
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

