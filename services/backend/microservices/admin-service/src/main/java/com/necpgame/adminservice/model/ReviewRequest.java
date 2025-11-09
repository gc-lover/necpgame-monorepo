package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.UUID;
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
 * ReviewRequest
 */


public class ReviewRequest {

  private UUID reviewerId;

  /**
   * Gets or Sets decision
   */
  public enum DecisionEnum {
    CONFIRM("CONFIRM"),
    
    DISMISS("DISMISS"),
    
    ESCALATE("ESCALATE");

    private final String value;

    DecisionEnum(String value) {
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
    public static DecisionEnum fromValue(String value) {
      for (DecisionEnum b : DecisionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DecisionEnum decision;

  private @Nullable String notes;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    WARNING("WARNING"),
    
    TEMPORARY_BAN("TEMPORARY_BAN"),
    
    PERMANENT_BAN("PERMANENT_BAN"),
    
    NONE("NONE");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      return null;
    }
  }

  private JsonNullable<ActionEnum> action = JsonNullable.<ActionEnum>undefined();

  private JsonNullable<Integer> banDurationDays = JsonNullable.<Integer>undefined();

  public ReviewRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewRequest(UUID reviewerId, DecisionEnum decision) {
    this.reviewerId = reviewerId;
    this.decision = decision;
  }

  public ReviewRequest reviewerId(UUID reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

  /**
   * Get reviewerId
   * @return reviewerId
   */
  @NotNull @Valid 
  @Schema(name = "reviewer_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reviewer_id")
  public UUID getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(UUID reviewerId) {
    this.reviewerId = reviewerId;
  }

  public ReviewRequest decision(DecisionEnum decision) {
    this.decision = decision;
    return this;
  }

  /**
   * Get decision
   * @return decision
   */
  @NotNull 
  @Schema(name = "decision", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("decision")
  public DecisionEnum getDecision() {
    return decision;
  }

  public void setDecision(DecisionEnum decision) {
    this.decision = decision;
  }

  public ReviewRequest notes(@Nullable String notes) {
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

  public ReviewRequest action(ActionEnum action) {
    this.action = JsonNullable.of(action);
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public JsonNullable<ActionEnum> getAction() {
    return action;
  }

  public void setAction(JsonNullable<ActionEnum> action) {
    this.action = action;
  }

  public ReviewRequest banDurationDays(Integer banDurationDays) {
    this.banDurationDays = JsonNullable.of(banDurationDays);
    return this;
  }

  /**
   * Get banDurationDays
   * @return banDurationDays
   */
  
  @Schema(name = "ban_duration_days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ban_duration_days")
  public JsonNullable<Integer> getBanDurationDays() {
    return banDurationDays;
  }

  public void setBanDurationDays(JsonNullable<Integer> banDurationDays) {
    this.banDurationDays = banDurationDays;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewRequest reviewRequest = (ReviewRequest) o;
    return Objects.equals(this.reviewerId, reviewRequest.reviewerId) &&
        Objects.equals(this.decision, reviewRequest.decision) &&
        Objects.equals(this.notes, reviewRequest.notes) &&
        equalsNullable(this.action, reviewRequest.action) &&
        equalsNullable(this.banDurationDays, reviewRequest.banDurationDays);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(reviewerId, decision, notes, hashCodeNullable(action), hashCodeNullable(banDurationDays));
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
    sb.append("class ReviewRequest {\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    banDurationDays: ").append(toIndentedString(banDurationDays)).append("\n");
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

