package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ChallengeObjective;
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
 * Challenge
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Challenge {

  private String challengeId;

  /**
   * Gets or Sets challengeType
   */
  public enum ChallengeTypeEnum {
    DAILY("DAILY"),
    
    WEEKLY("WEEKLY"),
    
    SEASONAL("SEASONAL");

    private final String value;

    ChallengeTypeEnum(String value) {
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
    public static ChallengeTypeEnum fromValue(String value) {
      for (ChallengeTypeEnum b : ChallengeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ChallengeTypeEnum challengeType;

  private String description;

  @Valid
  private List<@Valid ChallengeObjective> objectives = new ArrayList<>();

  private @Nullable Integer xpReward;

  private @Nullable String rewardId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  private @Nullable Integer rerollCost;

  public Challenge() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Challenge(String challengeId, ChallengeTypeEnum challengeType, String description, List<@Valid ChallengeObjective> objectives) {
    this.challengeId = challengeId;
    this.challengeType = challengeType;
    this.description = description;
    this.objectives = objectives;
  }

  public Challenge challengeId(String challengeId) {
    this.challengeId = challengeId;
    return this;
  }

  /**
   * Get challengeId
   * @return challengeId
   */
  @NotNull 
  @Schema(name = "challengeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("challengeId")
  public String getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(String challengeId) {
    this.challengeId = challengeId;
  }

  public Challenge challengeType(ChallengeTypeEnum challengeType) {
    this.challengeType = challengeType;
    return this;
  }

  /**
   * Get challengeType
   * @return challengeType
   */
  @NotNull 
  @Schema(name = "challengeType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("challengeType")
  public ChallengeTypeEnum getChallengeType() {
    return challengeType;
  }

  public void setChallengeType(ChallengeTypeEnum challengeType) {
    this.challengeType = challengeType;
  }

  public Challenge description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Challenge objectives(List<@Valid ChallengeObjective> objectives) {
    this.objectives = objectives;
    return this;
  }

  public Challenge addObjectivesItem(ChallengeObjective objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  @NotNull @Valid 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectives")
  public List<@Valid ChallengeObjective> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid ChallengeObjective> objectives) {
    this.objectives = objectives;
  }

  public Challenge xpReward(@Nullable Integer xpReward) {
    this.xpReward = xpReward;
    return this;
  }

  /**
   * Get xpReward
   * @return xpReward
   */
  
  @Schema(name = "xpReward", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpReward")
  public @Nullable Integer getXpReward() {
    return xpReward;
  }

  public void setXpReward(@Nullable Integer xpReward) {
    this.xpReward = xpReward;
  }

  public Challenge rewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  
  @Schema(name = "rewardId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardId")
  public @Nullable String getRewardId() {
    return rewardId;
  }

  public void setRewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
  }

  public Challenge startAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startAt")
  public @Nullable OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public Challenge endAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * Get endAt
   * @return endAt
   */
  @Valid 
  @Schema(name = "endAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endAt")
  public @Nullable OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  public Challenge rerollCost(@Nullable Integer rerollCost) {
    this.rerollCost = rerollCost;
    return this;
  }

  /**
   * Get rerollCost
   * @return rerollCost
   */
  
  @Schema(name = "rerollCost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rerollCost")
  public @Nullable Integer getRerollCost() {
    return rerollCost;
  }

  public void setRerollCost(@Nullable Integer rerollCost) {
    this.rerollCost = rerollCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Challenge challenge = (Challenge) o;
    return Objects.equals(this.challengeId, challenge.challengeId) &&
        Objects.equals(this.challengeType, challenge.challengeType) &&
        Objects.equals(this.description, challenge.description) &&
        Objects.equals(this.objectives, challenge.objectives) &&
        Objects.equals(this.xpReward, challenge.xpReward) &&
        Objects.equals(this.rewardId, challenge.rewardId) &&
        Objects.equals(this.startAt, challenge.startAt) &&
        Objects.equals(this.endAt, challenge.endAt) &&
        Objects.equals(this.rerollCost, challenge.rerollCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challengeId, challengeType, description, objectives, xpReward, rewardId, startAt, endAt, rerollCost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Challenge {\n");
    sb.append("    challengeId: ").append(toIndentedString(challengeId)).append("\n");
    sb.append("    challengeType: ").append(toIndentedString(challengeType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    xpReward: ").append(toIndentedString(xpReward)).append("\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    rerollCost: ").append(toIndentedString(rerollCost)).append("\n");
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

