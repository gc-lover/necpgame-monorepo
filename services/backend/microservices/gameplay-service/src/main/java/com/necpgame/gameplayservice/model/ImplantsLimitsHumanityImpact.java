package com.necpgame.gameplayservice.model;

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
 * Влияние человечности на лимиты
 */

@Schema(name = "ImplantsLimits_humanity_impact", description = "Влияние человечности на лимиты")
@JsonTypeName("ImplantsLimits_humanity_impact")

public class ImplantsLimitsHumanityImpact {

  private @Nullable Integer slotsPenalty;

  private @Nullable Boolean canExceedLimit;

  public ImplantsLimitsHumanityImpact slotsPenalty(@Nullable Integer slotsPenalty) {
    this.slotsPenalty = slotsPenalty;
    return this;
  }

  /**
   * Штраф к слотам при низкой человечности
   * @return slotsPenalty
   */
  
  @Schema(name = "slots_penalty", description = "Штраф к слотам при низкой человечности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots_penalty")
  public @Nullable Integer getSlotsPenalty() {
    return slotsPenalty;
  }

  public void setSlotsPenalty(@Nullable Integer slotsPenalty) {
    this.slotsPenalty = slotsPenalty;
  }

  public ImplantsLimitsHumanityImpact canExceedLimit(@Nullable Boolean canExceedLimit) {
    this.canExceedLimit = canExceedLimit;
    return this;
  }

  /**
   * Можно ли временно превысить лимит
   * @return canExceedLimit
   */
  
  @Schema(name = "can_exceed_limit", description = "Можно ли временно превысить лимит", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_exceed_limit")
  public @Nullable Boolean getCanExceedLimit() {
    return canExceedLimit;
  }

  public void setCanExceedLimit(@Nullable Boolean canExceedLimit) {
    this.canExceedLimit = canExceedLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantsLimitsHumanityImpact implantsLimitsHumanityImpact = (ImplantsLimitsHumanityImpact) o;
    return Objects.equals(this.slotsPenalty, implantsLimitsHumanityImpact.slotsPenalty) &&
        Objects.equals(this.canExceedLimit, implantsLimitsHumanityImpact.canExceedLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slotsPenalty, canExceedLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantsLimitsHumanityImpact {\n");
    sb.append("    slotsPenalty: ").append(toIndentedString(slotsPenalty)).append("\n");
    sb.append("    canExceedLimit: ").append(toIndentedString(canExceedLimit)).append("\n");
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

