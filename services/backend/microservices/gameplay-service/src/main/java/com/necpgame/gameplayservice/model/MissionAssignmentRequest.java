package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.MissionRewardPreview;
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
 * MissionAssignmentRequest
 */


public class MissionAssignmentRequest {

  private String missionId;

  private Integer durationMinutes;

  /**
   * Gets or Sets difficulty
   */
  public enum DifficultyEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    ELITE("elite");

    private final String value;

    DifficultyEnum(String value) {
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
    public static DifficultyEnum fromValue(String value) {
      for (DifficultyEnum b : DifficultyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DifficultyEnum difficulty;

  private @Nullable String targetZone;

  private @Nullable MissionRewardPreview rewardPreview;

  /**
   * Gets or Sets notifyChannels
   */
  public enum NotifyChannelsEnum {
    INGAME("ingame"),
    
    PUSH("push"),
    
    EMAIL("email");

    private final String value;

    NotifyChannelsEnum(String value) {
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
    public static NotifyChannelsEnum fromValue(String value) {
      for (NotifyChannelsEnum b : NotifyChannelsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<NotifyChannelsEnum> notifyChannels = new ArrayList<>();

  public MissionAssignmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MissionAssignmentRequest(String missionId, Integer durationMinutes) {
    this.missionId = missionId;
    this.durationMinutes = durationMinutes;
  }

  public MissionAssignmentRequest missionId(String missionId) {
    this.missionId = missionId;
    return this;
  }

  /**
   * Get missionId
   * @return missionId
   */
  @NotNull 
  @Schema(name = "missionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("missionId")
  public String getMissionId() {
    return missionId;
  }

  public void setMissionId(String missionId) {
    this.missionId = missionId;
  }

  public MissionAssignmentRequest durationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * minimum: 5
   * maximum: 480
   * @return durationMinutes
   */
  @NotNull @Min(value = 5) @Max(value = 480) 
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("durationMinutes")
  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public MissionAssignmentRequest difficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable DifficultyEnum getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
  }

  public MissionAssignmentRequest targetZone(@Nullable String targetZone) {
    this.targetZone = targetZone;
    return this;
  }

  /**
   * Get targetZone
   * @return targetZone
   */
  
  @Schema(name = "targetZone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetZone")
  public @Nullable String getTargetZone() {
    return targetZone;
  }

  public void setTargetZone(@Nullable String targetZone) {
    this.targetZone = targetZone;
  }

  public MissionAssignmentRequest rewardPreview(@Nullable MissionRewardPreview rewardPreview) {
    this.rewardPreview = rewardPreview;
    return this;
  }

  /**
   * Get rewardPreview
   * @return rewardPreview
   */
  @Valid 
  @Schema(name = "rewardPreview", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardPreview")
  public @Nullable MissionRewardPreview getRewardPreview() {
    return rewardPreview;
  }

  public void setRewardPreview(@Nullable MissionRewardPreview rewardPreview) {
    this.rewardPreview = rewardPreview;
  }

  public MissionAssignmentRequest notifyChannels(List<NotifyChannelsEnum> notifyChannels) {
    this.notifyChannels = notifyChannels;
    return this;
  }

  public MissionAssignmentRequest addNotifyChannelsItem(NotifyChannelsEnum notifyChannelsItem) {
    if (this.notifyChannels == null) {
      this.notifyChannels = new ArrayList<>();
    }
    this.notifyChannels.add(notifyChannelsItem);
    return this;
  }

  /**
   * Get notifyChannels
   * @return notifyChannels
   */
  
  @Schema(name = "notifyChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyChannels")
  public List<NotifyChannelsEnum> getNotifyChannels() {
    return notifyChannels;
  }

  public void setNotifyChannels(List<NotifyChannelsEnum> notifyChannels) {
    this.notifyChannels = notifyChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MissionAssignmentRequest missionAssignmentRequest = (MissionAssignmentRequest) o;
    return Objects.equals(this.missionId, missionAssignmentRequest.missionId) &&
        Objects.equals(this.durationMinutes, missionAssignmentRequest.durationMinutes) &&
        Objects.equals(this.difficulty, missionAssignmentRequest.difficulty) &&
        Objects.equals(this.targetZone, missionAssignmentRequest.targetZone) &&
        Objects.equals(this.rewardPreview, missionAssignmentRequest.rewardPreview) &&
        Objects.equals(this.notifyChannels, missionAssignmentRequest.notifyChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(missionId, durationMinutes, difficulty, targetZone, rewardPreview, notifyChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MissionAssignmentRequest {\n");
    sb.append("    missionId: ").append(toIndentedString(missionId)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    targetZone: ").append(toIndentedString(targetZone)).append("\n");
    sb.append("    rewardPreview: ").append(toIndentedString(rewardPreview)).append("\n");
    sb.append("    notifyChannels: ").append(toIndentedString(notifyChannels)).append("\n");
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

