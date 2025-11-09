package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.SettlementUpdateRequestDefenseAdjustments;
import com.necpgame.worldservice.model.SettlementUpdateRequestUpgradePlan;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SettlementUpdateRequest
 */


public class SettlementUpdateRequest {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    CAMP("camp"),
    
    OUTPOST("outpost"),
    
    STRONGHOLD("stronghold"),
    
    CITY("city");

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

  private @Nullable StatusEnum status;

  private @Nullable SettlementUpdateRequestUpgradePlan upgradePlan;

  private @Nullable SettlementUpdateRequestDefenseAdjustments defenseAdjustments;

  /**
   * Gets or Sets logisticsPriority
   */
  public enum LogisticsPriorityEnum {
    LOW("low"),
    
    NORMAL("normal"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    LogisticsPriorityEnum(String value) {
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
    public static LogisticsPriorityEnum fromValue(String value) {
      for (LogisticsPriorityEnum b : LogisticsPriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LogisticsPriorityEnum logisticsPriority;

  private @Nullable String notes;

  public SettlementUpdateRequest status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public SettlementUpdateRequest upgradePlan(@Nullable SettlementUpdateRequestUpgradePlan upgradePlan) {
    this.upgradePlan = upgradePlan;
    return this;
  }

  /**
   * Get upgradePlan
   * @return upgradePlan
   */
  @Valid 
  @Schema(name = "upgradePlan", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgradePlan")
  public @Nullable SettlementUpdateRequestUpgradePlan getUpgradePlan() {
    return upgradePlan;
  }

  public void setUpgradePlan(@Nullable SettlementUpdateRequestUpgradePlan upgradePlan) {
    this.upgradePlan = upgradePlan;
  }

  public SettlementUpdateRequest defenseAdjustments(@Nullable SettlementUpdateRequestDefenseAdjustments defenseAdjustments) {
    this.defenseAdjustments = defenseAdjustments;
    return this;
  }

  /**
   * Get defenseAdjustments
   * @return defenseAdjustments
   */
  @Valid 
  @Schema(name = "defenseAdjustments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defenseAdjustments")
  public @Nullable SettlementUpdateRequestDefenseAdjustments getDefenseAdjustments() {
    return defenseAdjustments;
  }

  public void setDefenseAdjustments(@Nullable SettlementUpdateRequestDefenseAdjustments defenseAdjustments) {
    this.defenseAdjustments = defenseAdjustments;
  }

  public SettlementUpdateRequest logisticsPriority(@Nullable LogisticsPriorityEnum logisticsPriority) {
    this.logisticsPriority = logisticsPriority;
    return this;
  }

  /**
   * Get logisticsPriority
   * @return logisticsPriority
   */
  
  @Schema(name = "logisticsPriority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("logisticsPriority")
  public @Nullable LogisticsPriorityEnum getLogisticsPriority() {
    return logisticsPriority;
  }

  public void setLogisticsPriority(@Nullable LogisticsPriorityEnum logisticsPriority) {
    this.logisticsPriority = logisticsPriority;
  }

  public SettlementUpdateRequest notes(@Nullable String notes) {
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
    SettlementUpdateRequest settlementUpdateRequest = (SettlementUpdateRequest) o;
    return Objects.equals(this.status, settlementUpdateRequest.status) &&
        Objects.equals(this.upgradePlan, settlementUpdateRequest.upgradePlan) &&
        Objects.equals(this.defenseAdjustments, settlementUpdateRequest.defenseAdjustments) &&
        Objects.equals(this.logisticsPriority, settlementUpdateRequest.logisticsPriority) &&
        Objects.equals(this.notes, settlementUpdateRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, upgradePlan, defenseAdjustments, logisticsPriority, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SettlementUpdateRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    upgradePlan: ").append(toIndentedString(upgradePlan)).append("\n");
    sb.append("    defenseAdjustments: ").append(toIndentedString(defenseAdjustments)).append("\n");
    sb.append("    logisticsPriority: ").append(toIndentedString(logisticsPriority)).append("\n");
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

