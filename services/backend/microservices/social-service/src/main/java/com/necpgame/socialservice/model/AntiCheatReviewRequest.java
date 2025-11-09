package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * AntiCheatReviewRequest
 */


public class AntiCheatReviewRequest {

  @Valid
  private List<String> referralIds = new ArrayList<>();

  /**
   * Gets or Sets suspicionType
   */
  public enum SuspicionTypeEnum {
    SELF_REFERRAL("self_referral"),
    
    MASS_ACCOUNTS("mass_accounts"),
    
    BOT_BEHAVIOR("bot_behavior");

    private final String value;

    SuspicionTypeEnum(String value) {
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
    public static SuspicionTypeEnum fromValue(String value) {
      for (SuspicionTypeEnum b : SuspicionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SuspicionTypeEnum suspicionType;

  private @Nullable String notes;

  public AntiCheatReviewRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AntiCheatReviewRequest(List<String> referralIds, SuspicionTypeEnum suspicionType) {
    this.referralIds = referralIds;
    this.suspicionType = suspicionType;
  }

  public AntiCheatReviewRequest referralIds(List<String> referralIds) {
    this.referralIds = referralIds;
    return this;
  }

  public AntiCheatReviewRequest addReferralIdsItem(String referralIdsItem) {
    if (this.referralIds == null) {
      this.referralIds = new ArrayList<>();
    }
    this.referralIds.add(referralIdsItem);
    return this;
  }

  /**
   * Get referralIds
   * @return referralIds
   */
  @NotNull 
  @Schema(name = "referralIds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referralIds")
  public List<String> getReferralIds() {
    return referralIds;
  }

  public void setReferralIds(List<String> referralIds) {
    this.referralIds = referralIds;
  }

  public AntiCheatReviewRequest suspicionType(SuspicionTypeEnum suspicionType) {
    this.suspicionType = suspicionType;
    return this;
  }

  /**
   * Get suspicionType
   * @return suspicionType
   */
  @NotNull 
  @Schema(name = "suspicionType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("suspicionType")
  public SuspicionTypeEnum getSuspicionType() {
    return suspicionType;
  }

  public void setSuspicionType(SuspicionTypeEnum suspicionType) {
    this.suspicionType = suspicionType;
  }

  public AntiCheatReviewRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AntiCheatReviewRequest antiCheatReviewRequest = (AntiCheatReviewRequest) o;
    return Objects.equals(this.referralIds, antiCheatReviewRequest.referralIds) &&
        Objects.equals(this.suspicionType, antiCheatReviewRequest.suspicionType) &&
        Objects.equals(this.notes, antiCheatReviewRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referralIds, suspicionType, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AntiCheatReviewRequest {\n");
    sb.append("    referralIds: ").append(toIndentedString(referralIds)).append("\n");
    sb.append("    suspicionType: ").append(toIndentedString(suspicionType)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

