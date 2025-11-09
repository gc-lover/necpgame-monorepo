package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ScheduleSlot;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ScheduleSlotChange
 */


public class ScheduleSlotChange {

  private @Nullable String slotId;

  /**
   * Gets or Sets changeType
   */
  public enum ChangeTypeEnum {
    ADDED("added"),
    
    UPDATED("updated"),
    
    REMOVED("removed");

    private final String value;

    ChangeTypeEnum(String value) {
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
    public static ChangeTypeEnum fromValue(String value) {
      for (ChangeTypeEnum b : ChangeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ChangeTypeEnum changeType;

  private @Nullable ScheduleSlot previous;

  private @Nullable ScheduleSlot current;

  public ScheduleSlotChange slotId(@Nullable String slotId) {
    this.slotId = slotId;
    return this;
  }

  /**
   * Get slotId
   * @return slotId
   */
  
  @Schema(name = "slotId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slotId")
  public @Nullable String getSlotId() {
    return slotId;
  }

  public void setSlotId(@Nullable String slotId) {
    this.slotId = slotId;
  }

  public ScheduleSlotChange changeType(@Nullable ChangeTypeEnum changeType) {
    this.changeType = changeType;
    return this;
  }

  /**
   * Get changeType
   * @return changeType
   */
  
  @Schema(name = "changeType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("changeType")
  public @Nullable ChangeTypeEnum getChangeType() {
    return changeType;
  }

  public void setChangeType(@Nullable ChangeTypeEnum changeType) {
    this.changeType = changeType;
  }

  public ScheduleSlotChange previous(@Nullable ScheduleSlot previous) {
    this.previous = previous;
    return this;
  }

  /**
   * Get previous
   * @return previous
   */
  @Valid 
  @Schema(name = "previous", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous")
  public @Nullable ScheduleSlot getPrevious() {
    return previous;
  }

  public void setPrevious(@Nullable ScheduleSlot previous) {
    this.previous = previous;
  }

  public ScheduleSlotChange current(@Nullable ScheduleSlot current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  @Valid 
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable ScheduleSlot getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable ScheduleSlot current) {
    this.current = current;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleSlotChange scheduleSlotChange = (ScheduleSlotChange) o;
    return Objects.equals(this.slotId, scheduleSlotChange.slotId) &&
        Objects.equals(this.changeType, scheduleSlotChange.changeType) &&
        Objects.equals(this.previous, scheduleSlotChange.previous) &&
        Objects.equals(this.current, scheduleSlotChange.current);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotId, changeType, previous, current);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleSlotChange {\n");
    sb.append("    slotId: ").append(toIndentedString(slotId)).append("\n");
    sb.append("    changeType: ").append(toIndentedString(changeType)).append("\n");
    sb.append("    previous: ").append(toIndentedString(previous)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
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

