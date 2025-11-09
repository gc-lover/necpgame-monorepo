package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ValidationIssue;
import com.necpgame.worldservice.model.ValidationStatus;
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
 * FactionValidationResult
 */


public class FactionValidationResult {

  private ValidationStatus status;

  @Valid
  private List<String> blockedInvitees = new ArrayList<>();

  @Valid
  private List<@Valid ValidationIssue> issues = new ArrayList<>();

  public FactionValidationResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FactionValidationResult(ValidationStatus status) {
    this.status = status;
  }

  public FactionValidationResult status(ValidationStatus status) {
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
  public ValidationStatus getStatus() {
    return status;
  }

  public void setStatus(ValidationStatus status) {
    this.status = status;
  }

  public FactionValidationResult blockedInvitees(List<String> blockedInvitees) {
    this.blockedInvitees = blockedInvitees;
    return this;
  }

  public FactionValidationResult addBlockedInviteesItem(String blockedInviteesItem) {
    if (this.blockedInvitees == null) {
      this.blockedInvitees = new ArrayList<>();
    }
    this.blockedInvitees.add(blockedInviteesItem);
    return this;
  }

  /**
   * Get blockedInvitees
   * @return blockedInvitees
   */
  
  @Schema(name = "blockedInvitees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blockedInvitees")
  public List<String> getBlockedInvitees() {
    return blockedInvitees;
  }

  public void setBlockedInvitees(List<String> blockedInvitees) {
    this.blockedInvitees = blockedInvitees;
  }

  public FactionValidationResult issues(List<@Valid ValidationIssue> issues) {
    this.issues = issues;
    return this;
  }

  public FactionValidationResult addIssuesItem(ValidationIssue issuesItem) {
    if (this.issues == null) {
      this.issues = new ArrayList<>();
    }
    this.issues.add(issuesItem);
    return this;
  }

  /**
   * Get issues
   * @return issues
   */
  @Valid 
  @Schema(name = "issues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issues")
  public List<@Valid ValidationIssue> getIssues() {
    return issues;
  }

  public void setIssues(List<@Valid ValidationIssue> issues) {
    this.issues = issues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionValidationResult factionValidationResult = (FactionValidationResult) o;
    return Objects.equals(this.status, factionValidationResult.status) &&
        Objects.equals(this.blockedInvitees, factionValidationResult.blockedInvitees) &&
        Objects.equals(this.issues, factionValidationResult.issues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, blockedInvitees, issues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionValidationResult {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    blockedInvitees: ").append(toIndentedString(blockedInvitees)).append("\n");
    sb.append("    issues: ").append(toIndentedString(issues)).append("\n");
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

