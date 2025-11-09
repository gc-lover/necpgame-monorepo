package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.AuditMetadata;
import com.necpgame.socialservice.model.RelationshipTier;
import com.necpgame.socialservice.model.RelationshipType;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RelationshipSummary
 */


public class RelationshipSummary {

  private String relationshipId;

  private @Nullable String targetName;

  private RelationshipType type;

  private RelationshipTier tier;

  private Float progress;

  private Float moodContribution;

  private AuditMetadata audit;

  public RelationshipSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RelationshipSummary(String relationshipId, RelationshipType type, RelationshipTier tier, Float progress, Float moodContribution, AuditMetadata audit) {
    this.relationshipId = relationshipId;
    this.type = type;
    this.tier = tier;
    this.progress = progress;
    this.moodContribution = moodContribution;
    this.audit = audit;
  }

  public RelationshipSummary relationshipId(String relationshipId) {
    this.relationshipId = relationshipId;
    return this;
  }

  /**
   * Get relationshipId
   * @return relationshipId
   */
  @NotNull 
  @Schema(name = "relationshipId", example = "rel-rom-455", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("relationshipId")
  public String getRelationshipId() {
    return relationshipId;
  }

  public void setRelationshipId(String relationshipId) {
    this.relationshipId = relationshipId;
  }

  public RelationshipSummary targetName(@Nullable String targetName) {
    this.targetName = targetName;
    return this;
  }

  /**
   * Get targetName
   * @return targetName
   */
  
  @Schema(name = "targetName", example = "Aisha Frost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetName")
  public @Nullable String getTargetName() {
    return targetName;
  }

  public void setTargetName(@Nullable String targetName) {
    this.targetName = targetName;
  }

  public RelationshipSummary type(RelationshipType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull @Valid 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public RelationshipType getType() {
    return type;
  }

  public void setType(RelationshipType type) {
    this.type = type;
  }

  public RelationshipSummary tier(RelationshipTier tier) {
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
  public RelationshipTier getTier() {
    return tier;
  }

  public void setTier(RelationshipTier tier) {
    this.tier = tier;
  }

  public RelationshipSummary progress(Float progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress
   * minimum: 0
   * maximum: 1
   * @return progress
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "progress", example = "0.72", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("progress")
  public Float getProgress() {
    return progress;
  }

  public void setProgress(Float progress) {
    this.progress = progress;
  }

  public RelationshipSummary moodContribution(Float moodContribution) {
    this.moodContribution = moodContribution;
    return this;
  }

  /**
   * Get moodContribution
   * @return moodContribution
   */
  @NotNull 
  @Schema(name = "moodContribution", example = "3.8", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("moodContribution")
  public Float getMoodContribution() {
    return moodContribution;
  }

  public void setMoodContribution(Float moodContribution) {
    this.moodContribution = moodContribution;
  }

  public RelationshipSummary audit(AuditMetadata audit) {
    this.audit = audit;
    return this;
  }

  /**
   * Get audit
   * @return audit
   */
  @NotNull @Valid 
  @Schema(name = "audit", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("audit")
  public AuditMetadata getAudit() {
    return audit;
  }

  public void setAudit(AuditMetadata audit) {
    this.audit = audit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelationshipSummary relationshipSummary = (RelationshipSummary) o;
    return Objects.equals(this.relationshipId, relationshipSummary.relationshipId) &&
        Objects.equals(this.targetName, relationshipSummary.targetName) &&
        Objects.equals(this.type, relationshipSummary.type) &&
        Objects.equals(this.tier, relationshipSummary.tier) &&
        Objects.equals(this.progress, relationshipSummary.progress) &&
        Objects.equals(this.moodContribution, relationshipSummary.moodContribution) &&
        Objects.equals(this.audit, relationshipSummary.audit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, targetName, type, tier, progress, moodContribution, audit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelationshipSummary {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    targetName: ").append(toIndentedString(targetName)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    moodContribution: ").append(toIndentedString(moodContribution)).append("\n");
    sb.append("    audit: ").append(toIndentedString(audit)).append("\n");
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

