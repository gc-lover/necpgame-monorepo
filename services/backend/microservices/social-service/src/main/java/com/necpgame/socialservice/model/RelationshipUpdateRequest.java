package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.AuditMetadata;
import com.necpgame.socialservice.model.RelationshipTier;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * RelationshipUpdateRequest
 */


public class RelationshipUpdateRequest {

  private String action;

  private String updatedBy;

  private @Nullable RelationshipTier targetTier;

  private JsonNullable<String> notes = JsonNullable.<String>undefined();

  private JsonNullable<String> approvalToken = JsonNullable.<String>undefined();

  private JsonNullable<Float> overrideDelta = JsonNullable.<Float>undefined();

  private @Nullable AuditMetadata audit;

  public RelationshipUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RelationshipUpdateRequest(String action, String updatedBy) {
    this.action = action;
    this.updatedBy = updatedBy;
  }

  public RelationshipUpdateRequest action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", example = "advance", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public RelationshipUpdateRequest updatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  /**
   * Get updatedBy
   * @return updatedBy
   */
  @NotNull 
  @Schema(name = "updatedBy", example = "player-9841", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedBy")
  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public RelationshipUpdateRequest targetTier(@Nullable RelationshipTier targetTier) {
    this.targetTier = targetTier;
    return this;
  }

  /**
   * Get targetTier
   * @return targetTier
   */
  @Valid 
  @Schema(name = "targetTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetTier")
  public @Nullable RelationshipTier getTargetTier() {
    return targetTier;
  }

  public void setTargetTier(@Nullable RelationshipTier targetTier) {
    this.targetTier = targetTier;
  }

  public RelationshipUpdateRequest notes(String notes) {
    this.notes = JsonNullable.of(notes);
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public JsonNullable<String> getNotes() {
    return notes;
  }

  public void setNotes(JsonNullable<String> notes) {
    this.notes = notes;
  }

  public RelationshipUpdateRequest approvalToken(String approvalToken) {
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

  public RelationshipUpdateRequest overrideDelta(Float overrideDelta) {
    this.overrideDelta = JsonNullable.of(overrideDelta);
    return this;
  }

  /**
   * Get overrideDelta
   * @return overrideDelta
   */
  
  @Schema(name = "overrideDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overrideDelta")
  public JsonNullable<Float> getOverrideDelta() {
    return overrideDelta;
  }

  public void setOverrideDelta(JsonNullable<Float> overrideDelta) {
    this.overrideDelta = overrideDelta;
  }

  public RelationshipUpdateRequest audit(@Nullable AuditMetadata audit) {
    this.audit = audit;
    return this;
  }

  /**
   * Get audit
   * @return audit
   */
  @Valid 
  @Schema(name = "audit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audit")
  public @Nullable AuditMetadata getAudit() {
    return audit;
  }

  public void setAudit(@Nullable AuditMetadata audit) {
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
    RelationshipUpdateRequest relationshipUpdateRequest = (RelationshipUpdateRequest) o;
    return Objects.equals(this.action, relationshipUpdateRequest.action) &&
        Objects.equals(this.updatedBy, relationshipUpdateRequest.updatedBy) &&
        Objects.equals(this.targetTier, relationshipUpdateRequest.targetTier) &&
        equalsNullable(this.notes, relationshipUpdateRequest.notes) &&
        equalsNullable(this.approvalToken, relationshipUpdateRequest.approvalToken) &&
        equalsNullable(this.overrideDelta, relationshipUpdateRequest.overrideDelta) &&
        Objects.equals(this.audit, relationshipUpdateRequest.audit);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, updatedBy, targetTier, hashCodeNullable(notes), hashCodeNullable(approvalToken), hashCodeNullable(overrideDelta), audit);
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
    sb.append("class RelationshipUpdateRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
    sb.append("    targetTier: ").append(toIndentedString(targetTier)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    approvalToken: ").append(toIndentedString(approvalToken)).append("\n");
    sb.append("    overrideDelta: ").append(toIndentedString(overrideDelta)).append("\n");
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

