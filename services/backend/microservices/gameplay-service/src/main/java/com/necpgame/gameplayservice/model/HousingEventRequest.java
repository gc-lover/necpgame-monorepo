package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * HousingEventRequest
 */


public class HousingEventRequest {

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    PARTY("party"),
    
    TRADE_FAIR("trade_fair"),
    
    RAID_PREPARATION("raid_preparation");

    private final String value;

    EventTypeEnum(String value) {
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
    public static EventTypeEnum fromValue(String value) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private EventTypeEnum eventType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  private @Nullable Integer maxParticipants;

  @Valid
  private List<String> rewardsPreview = new ArrayList<>();

  public HousingEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HousingEventRequest(EventTypeEnum eventType, OffsetDateTime startAt) {
    this.eventType = eventType;
    this.startAt = startAt;
  }

  public HousingEventRequest eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  @NotNull 
  @Schema(name = "eventType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventType")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public HousingEventRequest startAt(OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @NotNull @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startAt")
  public OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public HousingEventRequest endAt(@Nullable OffsetDateTime endAt) {
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

  public HousingEventRequest maxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * @return maxParticipants
   */
  
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParticipants")
  public @Nullable Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public HousingEventRequest rewardsPreview(List<String> rewardsPreview) {
    this.rewardsPreview = rewardsPreview;
    return this;
  }

  public HousingEventRequest addRewardsPreviewItem(String rewardsPreviewItem) {
    if (this.rewardsPreview == null) {
      this.rewardsPreview = new ArrayList<>();
    }
    this.rewardsPreview.add(rewardsPreviewItem);
    return this;
  }

  /**
   * Get rewardsPreview
   * @return rewardsPreview
   */
  
  @Schema(name = "rewardsPreview", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardsPreview")
  public List<String> getRewardsPreview() {
    return rewardsPreview;
  }

  public void setRewardsPreview(List<String> rewardsPreview) {
    this.rewardsPreview = rewardsPreview;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HousingEventRequest housingEventRequest = (HousingEventRequest) o;
    return Objects.equals(this.eventType, housingEventRequest.eventType) &&
        Objects.equals(this.startAt, housingEventRequest.startAt) &&
        Objects.equals(this.endAt, housingEventRequest.endAt) &&
        Objects.equals(this.maxParticipants, housingEventRequest.maxParticipants) &&
        Objects.equals(this.rewardsPreview, housingEventRequest.rewardsPreview);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, startAt, endAt, maxParticipants, rewardsPreview);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingEventRequest {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    rewardsPreview: ").append(toIndentedString(rewardsPreview)).append("\n");
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

