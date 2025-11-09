package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.Cost;
import com.necpgame.narrativeservice.model.Grant;
import com.necpgame.narrativeservice.model.Reward;
import com.necpgame.narrativeservice.model.SkillRoll;
import com.necpgame.narrativeservice.model.TelemetrySummary;
import com.necpgame.narrativeservice.model.TutorialHint;
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
 * ResolveOptionResponse
 */


public class ResolveOptionResponse {

  /**
   * Gets or Sets outcome
   */
  public enum OutcomeEnum {
    SUCCESS("success"),
    
    FAILURE("failure"),
    
    CRITICAL_FAILURE("critical_failure");

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

  private String nodeId;

  private String optionId;

  @Valid
  private List<@Valid SkillRoll> appliedCheck = new ArrayList<>();

  @Valid
  private List<@Valid Reward> rewards = new ArrayList<>();

  @Valid
  private List<@Valid Grant> grants = new ArrayList<>();

  @Valid
  private List<@Valid Cost> costs = new ArrayList<>();

  @Valid
  private List<String> setFlags = new ArrayList<>();

  @Valid
  private List<String> clearFlags = new ArrayList<>();

  private @Nullable String nextNode;

  @Valid
  private List<String> triggeredEvents = new ArrayList<>();

  private @Nullable TelemetrySummary telemetry;

  @Valid
  private List<@Valid TutorialHint> tutorialUpdates = new ArrayList<>();

  public ResolveOptionResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResolveOptionResponse(OutcomeEnum outcome, String nodeId, String optionId) {
    this.outcome = outcome;
    this.nodeId = nodeId;
    this.optionId = optionId;
  }

  public ResolveOptionResponse outcome(OutcomeEnum outcome) {
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

  public ResolveOptionResponse nodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  @NotNull 
  @Schema(name = "nodeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nodeId")
  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public ResolveOptionResponse optionId(String optionId) {
    this.optionId = optionId;
    return this;
  }

  /**
   * Get optionId
   * @return optionId
   */
  @NotNull 
  @Schema(name = "optionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("optionId")
  public String getOptionId() {
    return optionId;
  }

  public void setOptionId(String optionId) {
    this.optionId = optionId;
  }

  public ResolveOptionResponse appliedCheck(List<@Valid SkillRoll> appliedCheck) {
    this.appliedCheck = appliedCheck;
    return this;
  }

  public ResolveOptionResponse addAppliedCheckItem(SkillRoll appliedCheckItem) {
    if (this.appliedCheck == null) {
      this.appliedCheck = new ArrayList<>();
    }
    this.appliedCheck.add(appliedCheckItem);
    return this;
  }

  /**
   * Get appliedCheck
   * @return appliedCheck
   */
  @Valid 
  @Schema(name = "appliedCheck", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliedCheck")
  public List<@Valid SkillRoll> getAppliedCheck() {
    return appliedCheck;
  }

  public void setAppliedCheck(List<@Valid SkillRoll> appliedCheck) {
    this.appliedCheck = appliedCheck;
  }

  public ResolveOptionResponse rewards(List<@Valid Reward> rewards) {
    this.rewards = rewards;
    return this;
  }

  public ResolveOptionResponse addRewardsItem(Reward rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<@Valid Reward> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid Reward> rewards) {
    this.rewards = rewards;
  }

  public ResolveOptionResponse grants(List<@Valid Grant> grants) {
    this.grants = grants;
    return this;
  }

  public ResolveOptionResponse addGrantsItem(Grant grantsItem) {
    if (this.grants == null) {
      this.grants = new ArrayList<>();
    }
    this.grants.add(grantsItem);
    return this;
  }

  /**
   * Get grants
   * @return grants
   */
  @Valid 
  @Schema(name = "grants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grants")
  public List<@Valid Grant> getGrants() {
    return grants;
  }

  public void setGrants(List<@Valid Grant> grants) {
    this.grants = grants;
  }

  public ResolveOptionResponse costs(List<@Valid Cost> costs) {
    this.costs = costs;
    return this;
  }

  public ResolveOptionResponse addCostsItem(Cost costsItem) {
    if (this.costs == null) {
      this.costs = new ArrayList<>();
    }
    this.costs.add(costsItem);
    return this;
  }

  /**
   * Get costs
   * @return costs
   */
  @Valid 
  @Schema(name = "costs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("costs")
  public List<@Valid Cost> getCosts() {
    return costs;
  }

  public void setCosts(List<@Valid Cost> costs) {
    this.costs = costs;
  }

  public ResolveOptionResponse setFlags(List<String> setFlags) {
    this.setFlags = setFlags;
    return this;
  }

  public ResolveOptionResponse addSetFlagsItem(String setFlagsItem) {
    if (this.setFlags == null) {
      this.setFlags = new ArrayList<>();
    }
    this.setFlags.add(setFlagsItem);
    return this;
  }

  /**
   * Get setFlags
   * @return setFlags
   */
  
  @Schema(name = "setFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("setFlags")
  public List<String> getSetFlags() {
    return setFlags;
  }

  public void setSetFlags(List<String> setFlags) {
    this.setFlags = setFlags;
  }

  public ResolveOptionResponse clearFlags(List<String> clearFlags) {
    this.clearFlags = clearFlags;
    return this;
  }

  public ResolveOptionResponse addClearFlagsItem(String clearFlagsItem) {
    if (this.clearFlags == null) {
      this.clearFlags = new ArrayList<>();
    }
    this.clearFlags.add(clearFlagsItem);
    return this;
  }

  /**
   * Get clearFlags
   * @return clearFlags
   */
  
  @Schema(name = "clearFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clearFlags")
  public List<String> getClearFlags() {
    return clearFlags;
  }

  public void setClearFlags(List<String> clearFlags) {
    this.clearFlags = clearFlags;
  }

  public ResolveOptionResponse nextNode(@Nullable String nextNode) {
    this.nextNode = nextNode;
    return this;
  }

  /**
   * Get nextNode
   * @return nextNode
   */
  
  @Schema(name = "nextNode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextNode")
  public @Nullable String getNextNode() {
    return nextNode;
  }

  public void setNextNode(@Nullable String nextNode) {
    this.nextNode = nextNode;
  }

  public ResolveOptionResponse triggeredEvents(List<String> triggeredEvents) {
    this.triggeredEvents = triggeredEvents;
    return this;
  }

  public ResolveOptionResponse addTriggeredEventsItem(String triggeredEventsItem) {
    if (this.triggeredEvents == null) {
      this.triggeredEvents = new ArrayList<>();
    }
    this.triggeredEvents.add(triggeredEventsItem);
    return this;
  }

  /**
   * Get triggeredEvents
   * @return triggeredEvents
   */
  
  @Schema(name = "triggeredEvents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggeredEvents")
  public List<String> getTriggeredEvents() {
    return triggeredEvents;
  }

  public void setTriggeredEvents(List<String> triggeredEvents) {
    this.triggeredEvents = triggeredEvents;
  }

  public ResolveOptionResponse telemetry(@Nullable TelemetrySummary telemetry) {
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
  public @Nullable TelemetrySummary getTelemetry() {
    return telemetry;
  }

  public void setTelemetry(@Nullable TelemetrySummary telemetry) {
    this.telemetry = telemetry;
  }

  public ResolveOptionResponse tutorialUpdates(List<@Valid TutorialHint> tutorialUpdates) {
    this.tutorialUpdates = tutorialUpdates;
    return this;
  }

  public ResolveOptionResponse addTutorialUpdatesItem(TutorialHint tutorialUpdatesItem) {
    if (this.tutorialUpdates == null) {
      this.tutorialUpdates = new ArrayList<>();
    }
    this.tutorialUpdates.add(tutorialUpdatesItem);
    return this;
  }

  /**
   * Get tutorialUpdates
   * @return tutorialUpdates
   */
  @Valid 
  @Schema(name = "tutorialUpdates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tutorialUpdates")
  public List<@Valid TutorialHint> getTutorialUpdates() {
    return tutorialUpdates;
  }

  public void setTutorialUpdates(List<@Valid TutorialHint> tutorialUpdates) {
    this.tutorialUpdates = tutorialUpdates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResolveOptionResponse resolveOptionResponse = (ResolveOptionResponse) o;
    return Objects.equals(this.outcome, resolveOptionResponse.outcome) &&
        Objects.equals(this.nodeId, resolveOptionResponse.nodeId) &&
        Objects.equals(this.optionId, resolveOptionResponse.optionId) &&
        Objects.equals(this.appliedCheck, resolveOptionResponse.appliedCheck) &&
        Objects.equals(this.rewards, resolveOptionResponse.rewards) &&
        Objects.equals(this.grants, resolveOptionResponse.grants) &&
        Objects.equals(this.costs, resolveOptionResponse.costs) &&
        Objects.equals(this.setFlags, resolveOptionResponse.setFlags) &&
        Objects.equals(this.clearFlags, resolveOptionResponse.clearFlags) &&
        Objects.equals(this.nextNode, resolveOptionResponse.nextNode) &&
        Objects.equals(this.triggeredEvents, resolveOptionResponse.triggeredEvents) &&
        Objects.equals(this.telemetry, resolveOptionResponse.telemetry) &&
        Objects.equals(this.tutorialUpdates, resolveOptionResponse.tutorialUpdates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outcome, nodeId, optionId, appliedCheck, rewards, grants, costs, setFlags, clearFlags, nextNode, triggeredEvents, telemetry, tutorialUpdates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResolveOptionResponse {\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    optionId: ").append(toIndentedString(optionId)).append("\n");
    sb.append("    appliedCheck: ").append(toIndentedString(appliedCheck)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    grants: ").append(toIndentedString(grants)).append("\n");
    sb.append("    costs: ").append(toIndentedString(costs)).append("\n");
    sb.append("    setFlags: ").append(toIndentedString(setFlags)).append("\n");
    sb.append("    clearFlags: ").append(toIndentedString(clearFlags)).append("\n");
    sb.append("    nextNode: ").append(toIndentedString(nextNode)).append("\n");
    sb.append("    triggeredEvents: ").append(toIndentedString(triggeredEvents)).append("\n");
    sb.append("    telemetry: ").append(toIndentedString(telemetry)).append("\n");
    sb.append("    tutorialUpdates: ").append(toIndentedString(tutorialUpdates)).append("\n");
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

