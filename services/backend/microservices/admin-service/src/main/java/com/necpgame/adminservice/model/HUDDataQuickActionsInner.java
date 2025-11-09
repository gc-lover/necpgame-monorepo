package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HUDDataQuickActionsInner
 */

@JsonTypeName("HUDData_quick_actions_inner")

public class HUDDataQuickActionsInner {

  private @Nullable Integer slot;

  private @Nullable String actionId;

  private @Nullable BigDecimal cooldownRemaining;

  public HUDDataQuickActionsInner slot(@Nullable Integer slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Get slot
   * @return slot
   */
  
  @Schema(name = "slot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slot")
  public @Nullable Integer getSlot() {
    return slot;
  }

  public void setSlot(@Nullable Integer slot) {
    this.slot = slot;
  }

  public HUDDataQuickActionsInner actionId(@Nullable String actionId) {
    this.actionId = actionId;
    return this;
  }

  /**
   * Get actionId
   * @return actionId
   */
  
  @Schema(name = "action_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action_id")
  public @Nullable String getActionId() {
    return actionId;
  }

  public void setActionId(@Nullable String actionId) {
    this.actionId = actionId;
  }

  public HUDDataQuickActionsInner cooldownRemaining(@Nullable BigDecimal cooldownRemaining) {
    this.cooldownRemaining = cooldownRemaining;
    return this;
  }

  /**
   * Get cooldownRemaining
   * @return cooldownRemaining
   */
  @Valid 
  @Schema(name = "cooldown_remaining", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown_remaining")
  public @Nullable BigDecimal getCooldownRemaining() {
    return cooldownRemaining;
  }

  public void setCooldownRemaining(@Nullable BigDecimal cooldownRemaining) {
    this.cooldownRemaining = cooldownRemaining;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HUDDataQuickActionsInner huDDataQuickActionsInner = (HUDDataQuickActionsInner) o;
    return Objects.equals(this.slot, huDDataQuickActionsInner.slot) &&
        Objects.equals(this.actionId, huDDataQuickActionsInner.actionId) &&
        Objects.equals(this.cooldownRemaining, huDDataQuickActionsInner.cooldownRemaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slot, actionId, cooldownRemaining);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HUDDataQuickActionsInner {\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    cooldownRemaining: ").append(toIndentedString(cooldownRemaining)).append("\n");
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

