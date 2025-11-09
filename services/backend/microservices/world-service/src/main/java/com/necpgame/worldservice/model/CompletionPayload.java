package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.OutcomeEffect;
import com.necpgame.worldservice.model.RewardDescriptor;
import com.necpgame.worldservice.model.TelemetrySnapshot;
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
 * CompletionPayload
 */


public class CompletionPayload {

  /**
   * Gets or Sets result
   */
  public enum ResultEnum {
    SUCCESS("SUCCESS"),
    
    FAILURE("FAILURE"),
    
    ABORTED("ABORTED");

    private final String value;

    ResultEnum(String value) {
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
    public static ResultEnum fromValue(String value) {
      for (ResultEnum b : ResultEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ResultEnum result;

  private @Nullable OutcomeEffect outcomes;

  @Valid
  private List<@Valid RewardDescriptor> issuedRewards = new ArrayList<>();

  private @Nullable TelemetrySnapshot telemetry;

  public CompletionPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CompletionPayload(ResultEnum result) {
    this.result = result;
  }

  public CompletionPayload result(ResultEnum result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  @NotNull 
  @Schema(name = "result", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("result")
  public ResultEnum getResult() {
    return result;
  }

  public void setResult(ResultEnum result) {
    this.result = result;
  }

  public CompletionPayload outcomes(@Nullable OutcomeEffect outcomes) {
    this.outcomes = outcomes;
    return this;
  }

  /**
   * Get outcomes
   * @return outcomes
   */
  @Valid 
  @Schema(name = "outcomes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcomes")
  public @Nullable OutcomeEffect getOutcomes() {
    return outcomes;
  }

  public void setOutcomes(@Nullable OutcomeEffect outcomes) {
    this.outcomes = outcomes;
  }

  public CompletionPayload issuedRewards(List<@Valid RewardDescriptor> issuedRewards) {
    this.issuedRewards = issuedRewards;
    return this;
  }

  public CompletionPayload addIssuedRewardsItem(RewardDescriptor issuedRewardsItem) {
    if (this.issuedRewards == null) {
      this.issuedRewards = new ArrayList<>();
    }
    this.issuedRewards.add(issuedRewardsItem);
    return this;
  }

  /**
   * Get issuedRewards
   * @return issuedRewards
   */
  @Valid 
  @Schema(name = "issuedRewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issuedRewards")
  public List<@Valid RewardDescriptor> getIssuedRewards() {
    return issuedRewards;
  }

  public void setIssuedRewards(List<@Valid RewardDescriptor> issuedRewards) {
    this.issuedRewards = issuedRewards;
  }

  public CompletionPayload telemetry(@Nullable TelemetrySnapshot telemetry) {
    this.telemetry = telemetry;
    return this;
  }

  /**
   * Get telemetry
   * @return telemetry
   */
  @Valid 
  @Schema(name = "telemetry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetry")
  public @Nullable TelemetrySnapshot getTelemetry() {
    return telemetry;
  }

  public void setTelemetry(@Nullable TelemetrySnapshot telemetry) {
    this.telemetry = telemetry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompletionPayload completionPayload = (CompletionPayload) o;
    return Objects.equals(this.result, completionPayload.result) &&
        Objects.equals(this.outcomes, completionPayload.outcomes) &&
        Objects.equals(this.issuedRewards, completionPayload.issuedRewards) &&
        Objects.equals(this.telemetry, completionPayload.telemetry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result, outcomes, issuedRewards, telemetry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompletionPayload {\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    outcomes: ").append(toIndentedString(outcomes)).append("\n");
    sb.append("    issuedRewards: ").append(toIndentedString(issuedRewards)).append("\n");
    sb.append("    telemetry: ").append(toIndentedString(telemetry)).append("\n");
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

