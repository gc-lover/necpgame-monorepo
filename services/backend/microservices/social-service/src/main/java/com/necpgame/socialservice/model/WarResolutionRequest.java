package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.Penalty;
import com.necpgame.socialservice.model.RewardDistribution;
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
 * WarResolutionRequest
 */


public class WarResolutionRequest {

  /**
   * Gets or Sets outcome
   */
  public enum OutcomeEnum {
    ATTACKER_WIN("attacker_win"),
    
    DEFENDER_WIN("defender_win"),
    
    TRUCE("truce"),
    
    STALEMATE("stalemate");

    private final String value;

    OutcomeEnum(String value) {
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
    public static OutcomeEnum fromValue(String value) {
      for (OutcomeEnum b : OutcomeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OutcomeEnum outcome;

  @Valid
  private List<String> capturedTerritories = new ArrayList<>();

  @Valid
  private List<@Valid RewardDistribution> rewardDistribution = new ArrayList<>();

  @Valid
  private List<@Valid Penalty> penalties = new ArrayList<>();

  public WarResolutionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WarResolutionRequest(OutcomeEnum outcome) {
    this.outcome = outcome;
  }

  public WarResolutionRequest outcome(OutcomeEnum outcome) {
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
  public OutcomeEnum getOutcome() {
    return outcome;
  }

  public void setOutcome(OutcomeEnum outcome) {
    this.outcome = outcome;
  }

  public WarResolutionRequest capturedTerritories(List<String> capturedTerritories) {
    this.capturedTerritories = capturedTerritories;
    return this;
  }

  public WarResolutionRequest addCapturedTerritoriesItem(String capturedTerritoriesItem) {
    if (this.capturedTerritories == null) {
      this.capturedTerritories = new ArrayList<>();
    }
    this.capturedTerritories.add(capturedTerritoriesItem);
    return this;
  }

  /**
   * Get capturedTerritories
   * @return capturedTerritories
   */
  
  @Schema(name = "capturedTerritories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capturedTerritories")
  public List<String> getCapturedTerritories() {
    return capturedTerritories;
  }

  public void setCapturedTerritories(List<String> capturedTerritories) {
    this.capturedTerritories = capturedTerritories;
  }

  public WarResolutionRequest rewardDistribution(List<@Valid RewardDistribution> rewardDistribution) {
    this.rewardDistribution = rewardDistribution;
    return this;
  }

  public WarResolutionRequest addRewardDistributionItem(RewardDistribution rewardDistributionItem) {
    if (this.rewardDistribution == null) {
      this.rewardDistribution = new ArrayList<>();
    }
    this.rewardDistribution.add(rewardDistributionItem);
    return this;
  }

  /**
   * Get rewardDistribution
   * @return rewardDistribution
   */
  @Valid 
  @Schema(name = "rewardDistribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardDistribution")
  public List<@Valid RewardDistribution> getRewardDistribution() {
    return rewardDistribution;
  }

  public void setRewardDistribution(List<@Valid RewardDistribution> rewardDistribution) {
    this.rewardDistribution = rewardDistribution;
  }

  public WarResolutionRequest penalties(List<@Valid Penalty> penalties) {
    this.penalties = penalties;
    return this;
  }

  public WarResolutionRequest addPenaltiesItem(Penalty penaltiesItem) {
    if (this.penalties == null) {
      this.penalties = new ArrayList<>();
    }
    this.penalties.add(penaltiesItem);
    return this;
  }

  /**
   * Get penalties
   * @return penalties
   */
  @Valid 
  @Schema(name = "penalties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public List<@Valid Penalty> getPenalties() {
    return penalties;
  }

  public void setPenalties(List<@Valid Penalty> penalties) {
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
    WarResolutionRequest warResolutionRequest = (WarResolutionRequest) o;
    return Objects.equals(this.outcome, warResolutionRequest.outcome) &&
        Objects.equals(this.capturedTerritories, warResolutionRequest.capturedTerritories) &&
        Objects.equals(this.rewardDistribution, warResolutionRequest.rewardDistribution) &&
        Objects.equals(this.penalties, warResolutionRequest.penalties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outcome, capturedTerritories, rewardDistribution, penalties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarResolutionRequest {\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    capturedTerritories: ").append(toIndentedString(capturedTerritories)).append("\n");
    sb.append("    rewardDistribution: ").append(toIndentedString(rewardDistribution)).append("\n");
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

