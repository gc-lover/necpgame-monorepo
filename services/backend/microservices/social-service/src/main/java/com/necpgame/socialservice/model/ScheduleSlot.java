package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * ScheduleSlot
 */


public class ScheduleSlot {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime from;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime to;

  private UUID locationId;

  private @Nullable String locationName;

  private String activity;

  /**
   * Gets or Sets transport
   */
  public enum TransportEnum {
    WALK("walk"),
    
    METRO("metro"),
    
    MAGLEV("maglev"),
    
    VEHICLE("vehicle"),
    
    DRONE("drone"),
    
    TELEPORT("teleport");

    private final String value;

    TransportEnum(String value) {
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
    public static TransportEnum fromValue(String value) {
      for (TransportEnum b : TransportEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TransportEnum transport;

  private @Nullable BigDecimal crowdLevel;

  private @Nullable BigDecimal factionHeat;

  @Valid
  private List<String> questHooks = new ArrayList<>();

  public ScheduleSlot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleSlot(OffsetDateTime from, OffsetDateTime to, UUID locationId, String activity) {
    this.from = from;
    this.to = to;
    this.locationId = locationId;
    this.activity = activity;
  }

  public ScheduleSlot from(OffsetDateTime from) {
    this.from = from;
    return this;
  }

  /**
   * Get from
   * @return from
   */
  @NotNull @Valid 
  @Schema(name = "from", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("from")
  public OffsetDateTime getFrom() {
    return from;
  }

  public void setFrom(OffsetDateTime from) {
    this.from = from;
  }

  public ScheduleSlot to(OffsetDateTime to) {
    this.to = to;
    return this;
  }

  /**
   * Get to
   * @return to
   */
  @NotNull @Valid 
  @Schema(name = "to", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("to")
  public OffsetDateTime getTo() {
    return to;
  }

  public void setTo(OffsetDateTime to) {
    this.to = to;
  }

  public ScheduleSlot locationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  @NotNull @Valid 
  @Schema(name = "locationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locationId")
  public UUID getLocationId() {
    return locationId;
  }

  public void setLocationId(UUID locationId) {
    this.locationId = locationId;
  }

  public ScheduleSlot locationName(@Nullable String locationName) {
    this.locationName = locationName;
    return this;
  }

  /**
   * Get locationName
   * @return locationName
   */
  
  @Schema(name = "locationName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locationName")
  public @Nullable String getLocationName() {
    return locationName;
  }

  public void setLocationName(@Nullable String locationName) {
    this.locationName = locationName;
  }

  public ScheduleSlot activity(String activity) {
    this.activity = activity;
    return this;
  }

  /**
   * Get activity
   * @return activity
   */
  @NotNull 
  @Schema(name = "activity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activity")
  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public ScheduleSlot transport(@Nullable TransportEnum transport) {
    this.transport = transport;
    return this;
  }

  /**
   * Get transport
   * @return transport
   */
  
  @Schema(name = "transport", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transport")
  public @Nullable TransportEnum getTransport() {
    return transport;
  }

  public void setTransport(@Nullable TransportEnum transport) {
    this.transport = transport;
  }

  public ScheduleSlot crowdLevel(@Nullable BigDecimal crowdLevel) {
    this.crowdLevel = crowdLevel;
    return this;
  }

  /**
   * Get crowdLevel
   * @return crowdLevel
   */
  @Valid 
  @Schema(name = "crowdLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crowdLevel")
  public @Nullable BigDecimal getCrowdLevel() {
    return crowdLevel;
  }

  public void setCrowdLevel(@Nullable BigDecimal crowdLevel) {
    this.crowdLevel = crowdLevel;
  }

  public ScheduleSlot factionHeat(@Nullable BigDecimal factionHeat) {
    this.factionHeat = factionHeat;
    return this;
  }

  /**
   * Get factionHeat
   * @return factionHeat
   */
  @Valid 
  @Schema(name = "factionHeat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionHeat")
  public @Nullable BigDecimal getFactionHeat() {
    return factionHeat;
  }

  public void setFactionHeat(@Nullable BigDecimal factionHeat) {
    this.factionHeat = factionHeat;
  }

  public ScheduleSlot questHooks(List<String> questHooks) {
    this.questHooks = questHooks;
    return this;
  }

  public ScheduleSlot addQuestHooksItem(String questHooksItem) {
    if (this.questHooks == null) {
      this.questHooks = new ArrayList<>();
    }
    this.questHooks.add(questHooksItem);
    return this;
  }

  /**
   * Get questHooks
   * @return questHooks
   */
  
  @Schema(name = "questHooks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questHooks")
  public List<String> getQuestHooks() {
    return questHooks;
  }

  public void setQuestHooks(List<String> questHooks) {
    this.questHooks = questHooks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleSlot scheduleSlot = (ScheduleSlot) o;
    return Objects.equals(this.from, scheduleSlot.from) &&
        Objects.equals(this.to, scheduleSlot.to) &&
        Objects.equals(this.locationId, scheduleSlot.locationId) &&
        Objects.equals(this.locationName, scheduleSlot.locationName) &&
        Objects.equals(this.activity, scheduleSlot.activity) &&
        Objects.equals(this.transport, scheduleSlot.transport) &&
        Objects.equals(this.crowdLevel, scheduleSlot.crowdLevel) &&
        Objects.equals(this.factionHeat, scheduleSlot.factionHeat) &&
        Objects.equals(this.questHooks, scheduleSlot.questHooks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, locationId, locationName, activity, transport, crowdLevel, factionHeat, questHooks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleSlot {\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    locationName: ").append(toIndentedString(locationName)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    transport: ").append(toIndentedString(transport)).append("\n");
    sb.append("    crowdLevel: ").append(toIndentedString(crowdLevel)).append("\n");
    sb.append("    factionHeat: ").append(toIndentedString(factionHeat)).append("\n");
    sb.append("    questHooks: ").append(toIndentedString(questHooks)).append("\n");
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

