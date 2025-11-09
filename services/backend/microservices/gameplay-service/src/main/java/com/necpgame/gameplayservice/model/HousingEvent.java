package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * HousingEvent
 */


public class HousingEvent {

  private @Nullable String eventId;

  private @Nullable String eventType;

  private @Nullable String apartmentId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  @Valid
  private List<String> participants = new ArrayList<>();

  @Valid
  private List<String> rewards = new ArrayList<>();

  public HousingEvent eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventId")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public HousingEvent eventType(@Nullable String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "eventType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventType")
  public @Nullable String getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable String eventType) {
    this.eventType = eventType;
  }

  public HousingEvent apartmentId(@Nullable String apartmentId) {
    this.apartmentId = apartmentId;
    return this;
  }

  /**
   * Get apartmentId
   * @return apartmentId
   */
  
  @Schema(name = "apartmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("apartmentId")
  public @Nullable String getApartmentId() {
    return apartmentId;
  }

  public void setApartmentId(@Nullable String apartmentId) {
    this.apartmentId = apartmentId;
  }

  public HousingEvent startAt(@Nullable OffsetDateTime startAt) {
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

  public HousingEvent endAt(@Nullable OffsetDateTime endAt) {
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

  public HousingEvent participants(List<String> participants) {
    this.participants = participants;
    return this;
  }

  public HousingEvent addParticipantsItem(String participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  public HousingEvent rewards(List<String> rewards) {
    this.rewards = rewards;
    return this;
  }

  public HousingEvent addRewardsItem(String rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<String> getRewards() {
    return rewards;
  }

  public void setRewards(List<String> rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HousingEvent housingEvent = (HousingEvent) o;
    return Objects.equals(this.eventId, housingEvent.eventId) &&
        Objects.equals(this.eventType, housingEvent.eventType) &&
        Objects.equals(this.apartmentId, housingEvent.apartmentId) &&
        Objects.equals(this.startAt, housingEvent.startAt) &&
        Objects.equals(this.endAt, housingEvent.endAt) &&
        Objects.equals(this.participants, housingEvent.participants) &&
        Objects.equals(this.rewards, housingEvent.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, eventType, apartmentId, startAt, endAt, participants, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    apartmentId: ").append(toIndentedString(apartmentId)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

