package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ScheduleAlert;
import com.necpgame.socialservice.model.ScheduleMetric;
import com.necpgame.socialservice.model.ScheduleSlot;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * NpcSchedule
 */


public class NpcSchedule {

  private UUID scheduleId;

  private UUID npcId;

  private @Nullable String npcName;

  private @Nullable String role;

  private @Nullable String rarity;

  private @Nullable UUID districtId;

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    NORMAL_DAY("normal_day"),
    
    NIGHT_SHIFT("night_shift"),
    
    EVENT_MODE("event_mode"),
    
    EMERGENCY("emergency"),
    
    UNDERGROUND("underground");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModeEnum mode;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime validFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime validTo;

  private @Nullable String fsm;

  @Valid
  private List<@Valid ScheduleSlot> slots = new ArrayList<>();

  @Valid
  private List<@Valid ScheduleAlert> alerts = new ArrayList<>();

  @Valid
  private List<@Valid ScheduleMetric> metrics = new ArrayList<>();

  private @Nullable String generatedFromTemplate;

  public NpcSchedule() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NpcSchedule(UUID scheduleId, UUID npcId, ModeEnum mode, OffsetDateTime validFrom, List<@Valid ScheduleSlot> slots) {
    this.scheduleId = scheduleId;
    this.npcId = npcId;
    this.mode = mode;
    this.validFrom = validFrom;
    this.slots = slots;
  }

  public NpcSchedule scheduleId(UUID scheduleId) {
    this.scheduleId = scheduleId;
    return this;
  }

  /**
   * Get scheduleId
   * @return scheduleId
   */
  @NotNull @Valid 
  @Schema(name = "scheduleId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scheduleId")
  public UUID getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(UUID scheduleId) {
    this.scheduleId = scheduleId;
  }

  public NpcSchedule npcId(UUID npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  @NotNull @Valid 
  @Schema(name = "npcId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npcId")
  public UUID getNpcId() {
    return npcId;
  }

  public void setNpcId(UUID npcId) {
    this.npcId = npcId;
  }

  public NpcSchedule npcName(@Nullable String npcName) {
    this.npcName = npcName;
    return this;
  }

  /**
   * Get npcName
   * @return npcName
   */
  
  @Schema(name = "npcName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npcName")
  public @Nullable String getNpcName() {
    return npcName;
  }

  public void setNpcName(@Nullable String npcName) {
    this.npcName = npcName;
  }

  public NpcSchedule role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public NpcSchedule rarity(@Nullable String rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable String getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable String rarity) {
    this.rarity = rarity;
  }

  public NpcSchedule districtId(@Nullable UUID districtId) {
    this.districtId = districtId;
    return this;
  }

  /**
   * Get districtId
   * @return districtId
   */
  @Valid 
  @Schema(name = "districtId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districtId")
  public @Nullable UUID getDistrictId() {
    return districtId;
  }

  public void setDistrictId(@Nullable UUID districtId) {
    this.districtId = districtId;
  }

  public NpcSchedule mode(ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public NpcSchedule validFrom(OffsetDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  /**
   * Get validFrom
   * @return validFrom
   */
  @NotNull @Valid 
  @Schema(name = "validFrom", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("validFrom")
  public OffsetDateTime getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(OffsetDateTime validFrom) {
    this.validFrom = validFrom;
  }

  public NpcSchedule validTo(@Nullable OffsetDateTime validTo) {
    this.validTo = validTo;
    return this;
  }

  /**
   * Get validTo
   * @return validTo
   */
  @Valid 
  @Schema(name = "validTo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validTo")
  public @Nullable OffsetDateTime getValidTo() {
    return validTo;
  }

  public void setValidTo(@Nullable OffsetDateTime validTo) {
    this.validTo = validTo;
  }

  public NpcSchedule fsm(@Nullable String fsm) {
    this.fsm = fsm;
    return this;
  }

  /**
   * Название автомата состояний, управляющего расписанием.
   * @return fsm
   */
  
  @Schema(name = "fsm", description = "Название автомата состояний, управляющего расписанием.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fsm")
  public @Nullable String getFsm() {
    return fsm;
  }

  public void setFsm(@Nullable String fsm) {
    this.fsm = fsm;
  }

  public NpcSchedule slots(List<@Valid ScheduleSlot> slots) {
    this.slots = slots;
    return this;
  }

  public NpcSchedule addSlotsItem(ScheduleSlot slotsItem) {
    if (this.slots == null) {
      this.slots = new ArrayList<>();
    }
    this.slots.add(slotsItem);
    return this;
  }

  /**
   * Get slots
   * @return slots
   */
  @NotNull @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots")
  public List<@Valid ScheduleSlot> getSlots() {
    return slots;
  }

  public void setSlots(List<@Valid ScheduleSlot> slots) {
    this.slots = slots;
  }

  public NpcSchedule alerts(List<@Valid ScheduleAlert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public NpcSchedule addAlertsItem(ScheduleAlert alertsItem) {
    if (this.alerts == null) {
      this.alerts = new ArrayList<>();
    }
    this.alerts.add(alertsItem);
    return this;
  }

  /**
   * Get alerts
   * @return alerts
   */
  @Valid 
  @Schema(name = "alerts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alerts")
  public List<@Valid ScheduleAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<@Valid ScheduleAlert> alerts) {
    this.alerts = alerts;
  }

  public NpcSchedule metrics(List<@Valid ScheduleMetric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public NpcSchedule addMetricsItem(ScheduleMetric metricsItem) {
    if (this.metrics == null) {
      this.metrics = new ArrayList<>();
    }
    this.metrics.add(metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public List<@Valid ScheduleMetric> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<@Valid ScheduleMetric> metrics) {
    this.metrics = metrics;
  }

  public NpcSchedule generatedFromTemplate(@Nullable String generatedFromTemplate) {
    this.generatedFromTemplate = generatedFromTemplate;
    return this;
  }

  /**
   * Get generatedFromTemplate
   * @return generatedFromTemplate
   */
  
  @Schema(name = "generatedFromTemplate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedFromTemplate")
  public @Nullable String getGeneratedFromTemplate() {
    return generatedFromTemplate;
  }

  public void setGeneratedFromTemplate(@Nullable String generatedFromTemplate) {
    this.generatedFromTemplate = generatedFromTemplate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NpcSchedule npcSchedule = (NpcSchedule) o;
    return Objects.equals(this.scheduleId, npcSchedule.scheduleId) &&
        Objects.equals(this.npcId, npcSchedule.npcId) &&
        Objects.equals(this.npcName, npcSchedule.npcName) &&
        Objects.equals(this.role, npcSchedule.role) &&
        Objects.equals(this.rarity, npcSchedule.rarity) &&
        Objects.equals(this.districtId, npcSchedule.districtId) &&
        Objects.equals(this.mode, npcSchedule.mode) &&
        Objects.equals(this.validFrom, npcSchedule.validFrom) &&
        Objects.equals(this.validTo, npcSchedule.validTo) &&
        Objects.equals(this.fsm, npcSchedule.fsm) &&
        Objects.equals(this.slots, npcSchedule.slots) &&
        Objects.equals(this.alerts, npcSchedule.alerts) &&
        Objects.equals(this.metrics, npcSchedule.metrics) &&
        Objects.equals(this.generatedFromTemplate, npcSchedule.generatedFromTemplate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleId, npcId, npcName, role, rarity, districtId, mode, validFrom, validTo, fsm, slots, alerts, metrics, generatedFromTemplate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NpcSchedule {\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    npcName: ").append(toIndentedString(npcName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    validFrom: ").append(toIndentedString(validFrom)).append("\n");
    sb.append("    validTo: ").append(toIndentedString(validTo)).append("\n");
    sb.append("    fsm: ").append(toIndentedString(fsm)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    generatedFromTemplate: ").append(toIndentedString(generatedFromTemplate)).append("\n");
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

