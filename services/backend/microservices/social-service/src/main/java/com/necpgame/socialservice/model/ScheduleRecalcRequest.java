package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ScheduleRecalcRequest
 */


public class ScheduleRecalcRequest {

  @Valid
  private List<UUID> npcIds = new ArrayList<>();

  /**
   * Gets or Sets trigger
   */
  public enum TriggerEnum {
    EVENT("event"),
    
    MANUAL("manual"),
    
    PLAYER_IMPACT("player-impact"),
    
    INFRASTRUCTURE_CHANGE("infrastructure-change");

    private final String value;

    TriggerEnum(String value) {
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
    public static TriggerEnum fromValue(String value) {
      for (TriggerEnum b : TriggerEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TriggerEnum trigger;

  private @Nullable UUID cityId;

  @Valid
  private List<UUID> districtIds = new ArrayList<>();

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    NORMAL("normal"),
    
    HIGH("high"),
    
    URGENT("urgent");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority = PriorityEnum.NORMAL;

  private Boolean dryRun = false;

  private @Nullable String notes;

  public ScheduleRecalcRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleRecalcRequest(List<UUID> npcIds, TriggerEnum trigger) {
    this.npcIds = npcIds;
    this.trigger = trigger;
  }

  public ScheduleRecalcRequest npcIds(List<UUID> npcIds) {
    this.npcIds = npcIds;
    return this;
  }

  public ScheduleRecalcRequest addNpcIdsItem(UUID npcIdsItem) {
    if (this.npcIds == null) {
      this.npcIds = new ArrayList<>();
    }
    this.npcIds.add(npcIdsItem);
    return this;
  }

  /**
   * Get npcIds
   * @return npcIds
   */
  @NotNull @Valid 
  @Schema(name = "npcIds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npcIds")
  public List<UUID> getNpcIds() {
    return npcIds;
  }

  public void setNpcIds(List<UUID> npcIds) {
    this.npcIds = npcIds;
  }

  public ScheduleRecalcRequest trigger(TriggerEnum trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * Get trigger
   * @return trigger
   */
  @NotNull 
  @Schema(name = "trigger", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trigger")
  public TriggerEnum getTrigger() {
    return trigger;
  }

  public void setTrigger(TriggerEnum trigger) {
    this.trigger = trigger;
  }

  public ScheduleRecalcRequest cityId(@Nullable UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @Valid 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityId")
  public @Nullable UUID getCityId() {
    return cityId;
  }

  public void setCityId(@Nullable UUID cityId) {
    this.cityId = cityId;
  }

  public ScheduleRecalcRequest districtIds(List<UUID> districtIds) {
    this.districtIds = districtIds;
    return this;
  }

  public ScheduleRecalcRequest addDistrictIdsItem(UUID districtIdsItem) {
    if (this.districtIds == null) {
      this.districtIds = new ArrayList<>();
    }
    this.districtIds.add(districtIdsItem);
    return this;
  }

  /**
   * Get districtIds
   * @return districtIds
   */
  @Valid 
  @Schema(name = "districtIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districtIds")
  public List<UUID> getDistrictIds() {
    return districtIds;
  }

  public void setDistrictIds(List<UUID> districtIds) {
    this.districtIds = districtIds;
  }

  public ScheduleRecalcRequest priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public ScheduleRecalcRequest dryRun(Boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  /**
   * Get dryRun
   * @return dryRun
   */
  
  @Schema(name = "dryRun", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dryRun")
  public Boolean getDryRun() {
    return dryRun;
  }

  public void setDryRun(Boolean dryRun) {
    this.dryRun = dryRun;
  }

  public ScheduleRecalcRequest notes(@Nullable String notes) {
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
    ScheduleRecalcRequest scheduleRecalcRequest = (ScheduleRecalcRequest) o;
    return Objects.equals(this.npcIds, scheduleRecalcRequest.npcIds) &&
        Objects.equals(this.trigger, scheduleRecalcRequest.trigger) &&
        Objects.equals(this.cityId, scheduleRecalcRequest.cityId) &&
        Objects.equals(this.districtIds, scheduleRecalcRequest.districtIds) &&
        Objects.equals(this.priority, scheduleRecalcRequest.priority) &&
        Objects.equals(this.dryRun, scheduleRecalcRequest.dryRun) &&
        Objects.equals(this.notes, scheduleRecalcRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcIds, trigger, cityId, districtIds, priority, dryRun, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleRecalcRequest {\n");
    sb.append("    npcIds: ").append(toIndentedString(npcIds)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    districtIds: ").append(toIndentedString(districtIds)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
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

