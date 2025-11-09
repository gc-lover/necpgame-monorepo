package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.MissionRewardPreview;
import java.time.OffsetDateTime;
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
 * MissionTicket
 */


public class MissionTicket {

  private String missionInstanceId;

  private String companionId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eta;

  private @Nullable Integer successChance;

  private @Nullable MissionRewardPreview rewardsEarned;

  public MissionTicket() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MissionTicket(String missionInstanceId, String companionId, StatusEnum status) {
    this.missionInstanceId = missionInstanceId;
    this.companionId = companionId;
    this.status = status;
  }

  public MissionTicket missionInstanceId(String missionInstanceId) {
    this.missionInstanceId = missionInstanceId;
    return this;
  }

  /**
   * Get missionInstanceId
   * @return missionInstanceId
   */
  @NotNull 
  @Schema(name = "missionInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("missionInstanceId")
  public String getMissionInstanceId() {
    return missionInstanceId;
  }

  public void setMissionInstanceId(String missionInstanceId) {
    this.missionInstanceId = missionInstanceId;
  }

  public MissionTicket companionId(String companionId) {
    this.companionId = companionId;
    return this;
  }

  /**
   * Get companionId
   * @return companionId
   */
  @NotNull 
  @Schema(name = "companionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("companionId")
  public String getCompanionId() {
    return companionId;
  }

  public void setCompanionId(String companionId) {
    this.companionId = companionId;
  }

  public MissionTicket status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public MissionTicket startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "startedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startedAt")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public MissionTicket eta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
    return this;
  }

  /**
   * Get eta
   * @return eta
   */
  @Valid 
  @Schema(name = "eta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eta")
  public @Nullable OffsetDateTime getEta() {
    return eta;
  }

  public void setEta(@Nullable OffsetDateTime eta) {
    this.eta = eta;
  }

  public MissionTicket successChance(@Nullable Integer successChance) {
    this.successChance = successChance;
    return this;
  }

  /**
   * Get successChance
   * minimum: 0
   * maximum: 100
   * @return successChance
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "successChance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("successChance")
  public @Nullable Integer getSuccessChance() {
    return successChance;
  }

  public void setSuccessChance(@Nullable Integer successChance) {
    this.successChance = successChance;
  }

  public MissionTicket rewardsEarned(@Nullable MissionRewardPreview rewardsEarned) {
    this.rewardsEarned = rewardsEarned;
    return this;
  }

  /**
   * Get rewardsEarned
   * @return rewardsEarned
   */
  @Valid 
  @Schema(name = "rewardsEarned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardsEarned")
  public @Nullable MissionRewardPreview getRewardsEarned() {
    return rewardsEarned;
  }

  public void setRewardsEarned(@Nullable MissionRewardPreview rewardsEarned) {
    this.rewardsEarned = rewardsEarned;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MissionTicket missionTicket = (MissionTicket) o;
    return Objects.equals(this.missionInstanceId, missionTicket.missionInstanceId) &&
        Objects.equals(this.companionId, missionTicket.companionId) &&
        Objects.equals(this.status, missionTicket.status) &&
        Objects.equals(this.startedAt, missionTicket.startedAt) &&
        Objects.equals(this.eta, missionTicket.eta) &&
        Objects.equals(this.successChance, missionTicket.successChance) &&
        Objects.equals(this.rewardsEarned, missionTicket.rewardsEarned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(missionInstanceId, companionId, status, startedAt, eta, successChance, rewardsEarned);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MissionTicket {\n");
    sb.append("    missionInstanceId: ").append(toIndentedString(missionInstanceId)).append("\n");
    sb.append("    companionId: ").append(toIndentedString(companionId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    eta: ").append(toIndentedString(eta)).append("\n");
    sb.append("    successChance: ").append(toIndentedString(successChance)).append("\n");
    sb.append("    rewardsEarned: ").append(toIndentedString(rewardsEarned)).append("\n");
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

