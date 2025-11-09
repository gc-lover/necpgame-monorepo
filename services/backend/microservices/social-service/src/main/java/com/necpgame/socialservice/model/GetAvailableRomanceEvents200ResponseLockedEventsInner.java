package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.RomanceEventInfo;
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
 * GetAvailableRomanceEvents200ResponseLockedEventsInner
 */

@JsonTypeName("getAvailableRomanceEvents_200_response_locked_events_inner")

public class GetAvailableRomanceEvents200ResponseLockedEventsInner {

  private @Nullable RomanceEventInfo event;

  @Valid
  private List<String> unlockRequirements = new ArrayList<>();

  public GetAvailableRomanceEvents200ResponseLockedEventsInner event(@Nullable RomanceEventInfo event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable RomanceEventInfo getEvent() {
    return event;
  }

  public void setEvent(@Nullable RomanceEventInfo event) {
    this.event = event;
  }

  public GetAvailableRomanceEvents200ResponseLockedEventsInner unlockRequirements(List<String> unlockRequirements) {
    this.unlockRequirements = unlockRequirements;
    return this;
  }

  public GetAvailableRomanceEvents200ResponseLockedEventsInner addUnlockRequirementsItem(String unlockRequirementsItem) {
    if (this.unlockRequirements == null) {
      this.unlockRequirements = new ArrayList<>();
    }
    this.unlockRequirements.add(unlockRequirementsItem);
    return this;
  }

  /**
   * Get unlockRequirements
   * @return unlockRequirements
   */
  
  @Schema(name = "unlock_requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlock_requirements")
  public List<String> getUnlockRequirements() {
    return unlockRequirements;
  }

  public void setUnlockRequirements(List<String> unlockRequirements) {
    this.unlockRequirements = unlockRequirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableRomanceEvents200ResponseLockedEventsInner getAvailableRomanceEvents200ResponseLockedEventsInner = (GetAvailableRomanceEvents200ResponseLockedEventsInner) o;
    return Objects.equals(this.event, getAvailableRomanceEvents200ResponseLockedEventsInner.event) &&
        Objects.equals(this.unlockRequirements, getAvailableRomanceEvents200ResponseLockedEventsInner.unlockRequirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, unlockRequirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableRomanceEvents200ResponseLockedEventsInner {\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    unlockRequirements: ").append(toIndentedString(unlockRequirements)).append("\n");
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

