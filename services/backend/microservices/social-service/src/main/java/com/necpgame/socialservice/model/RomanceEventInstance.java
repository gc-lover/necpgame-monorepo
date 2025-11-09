package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RomanceEvent;
import com.necpgame.socialservice.model.RomanceEventInstanceChoicesInner;
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
 * RomanceEventInstance
 */


public class RomanceEventInstance {

  private @Nullable UUID instanceId;

  private @Nullable RomanceEvent event;

  @Valid
  private List<@Valid RomanceEventInstanceChoicesInner> choices = new ArrayList<>();

  public RomanceEventInstance instanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @Valid 
  @Schema(name = "instance_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance_id")
  public @Nullable UUID getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
  }

  public RomanceEventInstance event(@Nullable RomanceEvent event) {
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
  public @Nullable RomanceEvent getEvent() {
    return event;
  }

  public void setEvent(@Nullable RomanceEvent event) {
    this.event = event;
  }

  public RomanceEventInstance choices(List<@Valid RomanceEventInstanceChoicesInner> choices) {
    this.choices = choices;
    return this;
  }

  public RomanceEventInstance addChoicesItem(RomanceEventInstanceChoicesInner choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

  /**
   * Get choices
   * @return choices
   */
  @Valid 
  @Schema(name = "choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<@Valid RomanceEventInstanceChoicesInner> getChoices() {
    return choices;
  }

  public void setChoices(List<@Valid RomanceEventInstanceChoicesInner> choices) {
    this.choices = choices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventInstance romanceEventInstance = (RomanceEventInstance) o;
    return Objects.equals(this.instanceId, romanceEventInstance.instanceId) &&
        Objects.equals(this.event, romanceEventInstance.event) &&
        Objects.equals(this.choices, romanceEventInstance.choices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, event, choices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventInstance {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
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

