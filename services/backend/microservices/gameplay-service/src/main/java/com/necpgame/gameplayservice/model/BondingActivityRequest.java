package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BondingActivityRequest
 */


public class BondingActivityRequest {

  /**
   * Gets or Sets activityType
   */
  public enum ActivityTypeEnum {
    DIALOGUE("dialogue"),
    
    TRAINING("training"),
    
    PATROL("patrol"),
    
    GIFT("gift"),
    
    MISSION_SUPPORT("mission_support");

    private final String value;

    ActivityTypeEnum(String value) {
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
    public static ActivityTypeEnum fromValue(String value) {
      for (ActivityTypeEnum b : ActivityTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActivityTypeEnum activityType;

  private @Nullable String itemId;

  private @Nullable Integer bondingDelta;

  private @Nullable String emotionTag;

  private @Nullable String notes;

  public BondingActivityRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BondingActivityRequest(ActivityTypeEnum activityType) {
    this.activityType = activityType;
  }

  public BondingActivityRequest activityType(ActivityTypeEnum activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  @NotNull 
  @Schema(name = "activityType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityType")
  public ActivityTypeEnum getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityTypeEnum activityType) {
    this.activityType = activityType;
  }

  public BondingActivityRequest itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public BondingActivityRequest bondingDelta(@Nullable Integer bondingDelta) {
    this.bondingDelta = bondingDelta;
    return this;
  }

  /**
   * Get bondingDelta
   * minimum: 0
   * maximum: 25
   * @return bondingDelta
   */
  @Min(value = 0) @Max(value = 25) 
  @Schema(name = "bondingDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bondingDelta")
  public @Nullable Integer getBondingDelta() {
    return bondingDelta;
  }

  public void setBondingDelta(@Nullable Integer bondingDelta) {
    this.bondingDelta = bondingDelta;
  }

  public BondingActivityRequest emotionTag(@Nullable String emotionTag) {
    this.emotionTag = emotionTag;
    return this;
  }

  /**
   * Get emotionTag
   * @return emotionTag
   */
  
  @Schema(name = "emotionTag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("emotionTag")
  public @Nullable String getEmotionTag() {
    return emotionTag;
  }

  public void setEmotionTag(@Nullable String emotionTag) {
    this.emotionTag = emotionTag;
  }

  public BondingActivityRequest notes(@Nullable String notes) {
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
    BondingActivityRequest bondingActivityRequest = (BondingActivityRequest) o;
    return Objects.equals(this.activityType, bondingActivityRequest.activityType) &&
        Objects.equals(this.itemId, bondingActivityRequest.itemId) &&
        Objects.equals(this.bondingDelta, bondingActivityRequest.bondingDelta) &&
        Objects.equals(this.emotionTag, bondingActivityRequest.emotionTag) &&
        Objects.equals(this.notes, bondingActivityRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activityType, itemId, bondingDelta, emotionTag, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BondingActivityRequest {\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    bondingDelta: ").append(toIndentedString(bondingDelta)).append("\n");
    sb.append("    emotionTag: ").append(toIndentedString(emotionTag)).append("\n");
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

