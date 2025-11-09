package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.SettlementUpdateRequestUpgradePlanCommitmentsInner;
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
 * SettlementUpdateRequestUpgradePlan
 */

@JsonTypeName("SettlementUpdateRequest_upgradePlan")

public class SettlementUpdateRequestUpgradePlan {

  /**
   * Gets or Sets targetStatus
   */
  public enum TargetStatusEnum {
    OUTPOST("outpost"),
    
    STRONGHOLD("stronghold"),
    
    CITY("city");

    private final String value;

    TargetStatusEnum(String value) {
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
    public static TargetStatusEnum fromValue(String value) {
      for (TargetStatusEnum b : TargetStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TargetStatusEnum targetStatus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expectedCompletion;

  @Valid
  private List<@Valid SettlementUpdateRequestUpgradePlanCommitmentsInner> commitments = new ArrayList<>();

  public SettlementUpdateRequestUpgradePlan targetStatus(@Nullable TargetStatusEnum targetStatus) {
    this.targetStatus = targetStatus;
    return this;
  }

  /**
   * Get targetStatus
   * @return targetStatus
   */
  
  @Schema(name = "targetStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetStatus")
  public @Nullable TargetStatusEnum getTargetStatus() {
    return targetStatus;
  }

  public void setTargetStatus(@Nullable TargetStatusEnum targetStatus) {
    this.targetStatus = targetStatus;
  }

  public SettlementUpdateRequestUpgradePlan expectedCompletion(@Nullable OffsetDateTime expectedCompletion) {
    this.expectedCompletion = expectedCompletion;
    return this;
  }

  /**
   * Get expectedCompletion
   * @return expectedCompletion
   */
  @Valid 
  @Schema(name = "expectedCompletion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedCompletion")
  public @Nullable OffsetDateTime getExpectedCompletion() {
    return expectedCompletion;
  }

  public void setExpectedCompletion(@Nullable OffsetDateTime expectedCompletion) {
    this.expectedCompletion = expectedCompletion;
  }

  public SettlementUpdateRequestUpgradePlan commitments(List<@Valid SettlementUpdateRequestUpgradePlanCommitmentsInner> commitments) {
    this.commitments = commitments;
    return this;
  }

  public SettlementUpdateRequestUpgradePlan addCommitmentsItem(SettlementUpdateRequestUpgradePlanCommitmentsInner commitmentsItem) {
    if (this.commitments == null) {
      this.commitments = new ArrayList<>();
    }
    this.commitments.add(commitmentsItem);
    return this;
  }

  /**
   * Get commitments
   * @return commitments
   */
  @Valid 
  @Schema(name = "commitments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commitments")
  public List<@Valid SettlementUpdateRequestUpgradePlanCommitmentsInner> getCommitments() {
    return commitments;
  }

  public void setCommitments(List<@Valid SettlementUpdateRequestUpgradePlanCommitmentsInner> commitments) {
    this.commitments = commitments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SettlementUpdateRequestUpgradePlan settlementUpdateRequestUpgradePlan = (SettlementUpdateRequestUpgradePlan) o;
    return Objects.equals(this.targetStatus, settlementUpdateRequestUpgradePlan.targetStatus) &&
        Objects.equals(this.expectedCompletion, settlementUpdateRequestUpgradePlan.expectedCompletion) &&
        Objects.equals(this.commitments, settlementUpdateRequestUpgradePlan.commitments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetStatus, expectedCompletion, commitments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SettlementUpdateRequestUpgradePlan {\n");
    sb.append("    targetStatus: ").append(toIndentedString(targetStatus)).append("\n");
    sb.append("    expectedCompletion: ").append(toIndentedString(expectedCompletion)).append("\n");
    sb.append("    commitments: ").append(toIndentedString(commitments)).append("\n");
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

