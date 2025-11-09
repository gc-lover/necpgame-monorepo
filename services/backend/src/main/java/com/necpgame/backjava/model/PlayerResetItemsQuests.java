package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerResetItemsQuests
 */

@JsonTypeName("PlayerResetItems_quests")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerResetItemsQuests {

  private @Nullable Integer availableSlots;

  private @Nullable Integer completedToday;

  public PlayerResetItemsQuests availableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
    return this;
  }

  /**
   * Get availableSlots
   * @return availableSlots
   */
  
  @Schema(name = "available_slots", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_slots")
  public @Nullable Integer getAvailableSlots() {
    return availableSlots;
  }

  public void setAvailableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
  }

  public PlayerResetItemsQuests completedToday(@Nullable Integer completedToday) {
    this.completedToday = completedToday;
    return this;
  }

  /**
   * Get completedToday
   * @return completedToday
   */
  
  @Schema(name = "completed_today", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_today")
  public @Nullable Integer getCompletedToday() {
    return completedToday;
  }

  public void setCompletedToday(@Nullable Integer completedToday) {
    this.completedToday = completedToday;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerResetItemsQuests playerResetItemsQuests = (PlayerResetItemsQuests) o;
    return Objects.equals(this.availableSlots, playerResetItemsQuests.availableSlots) &&
        Objects.equals(this.completedToday, playerResetItemsQuests.completedToday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(availableSlots, completedToday);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerResetItemsQuests {\n");
    sb.append("    availableSlots: ").append(toIndentedString(availableSlots)).append("\n");
    sb.append("    completedToday: ").append(toIndentedString(completedToday)).append("\n");
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

