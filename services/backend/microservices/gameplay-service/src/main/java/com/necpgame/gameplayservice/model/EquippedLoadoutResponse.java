package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.EquippedSlot;
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
 * EquippedLoadoutResponse
 */


public class EquippedLoadoutResponse {

  private @Nullable String playerId;

  @Valid
  private List<@Valid EquippedSlot> slots = new ArrayList<>();

  public EquippedLoadoutResponse playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public EquippedLoadoutResponse slots(List<@Valid EquippedSlot> slots) {
    this.slots = slots;
    return this;
  }

  public EquippedLoadoutResponse addSlotsItem(EquippedSlot slotsItem) {
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
  @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots")
  public List<@Valid EquippedSlot> getSlots() {
    return slots;
  }

  public void setSlots(List<@Valid EquippedSlot> slots) {
    this.slots = slots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EquippedLoadoutResponse equippedLoadoutResponse = (EquippedLoadoutResponse) o;
    return Objects.equals(this.playerId, equippedLoadoutResponse.playerId) &&
        Objects.equals(this.slots, equippedLoadoutResponse.slots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, slots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquippedLoadoutResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
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

