package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import com.necpgame.backjava.model.EventResultRewards;
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
 * EventResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:13.174724+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class EventResult {

  private Boolean success;

  private String outcome;

  private JsonNullable<EventResultRewards> rewards = JsonNullable.<EventResultRewards>undefined();

  private JsonNullable<Object> penalties = JsonNullable.<Object>undefined();

  public EventResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EventResult(Boolean success, String outcome) {
    this.success = success;
    this.outcome = outcome;
  }

  public EventResult success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  @NotNull 
  @Schema(name = "success", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public EventResult outcome(String outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  @NotNull 
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("outcome")
  public String getOutcome() {
    return outcome;
  }

  public void setOutcome(String outcome) {
    this.outcome = outcome;
  }

  public EventResult rewards(EventResultRewards rewards) {
    this.rewards = JsonNullable.of(rewards);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public JsonNullable<EventResultRewards> getRewards() {
    return rewards;
  }

  public void setRewards(JsonNullable<EventResultRewards> rewards) {
    this.rewards = rewards;
  }

  public EventResult penalties(Object penalties) {
    this.penalties = JsonNullable.of(penalties);
    return this;
  }

  /**
   * Get penalties
   * @return penalties
   */
  
  @Schema(name = "penalties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public JsonNullable<Object> getPenalties() {
    return penalties;
  }

  public void setPenalties(JsonNullable<Object> penalties) {
    this.penalties = penalties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventResult eventResult = (EventResult) o;
    return Objects.equals(this.success, eventResult.success) &&
        Objects.equals(this.outcome, eventResult.outcome) &&
        equalsNullable(this.rewards, eventResult.rewards) &&
        equalsNullable(this.penalties, eventResult.penalties);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, outcome, hashCodeNullable(rewards), hashCodeNullable(penalties));
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
    sb.append("class EventResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    penalties: ").append(toIndentedString(penalties)).append("\n");
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

