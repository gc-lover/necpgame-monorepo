package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.HackEnemy200ResponseEffect;
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
 * HackEnemy200Response
 */

@JsonTypeName("hackEnemy_200_response")

public class HackEnemy200Response {

  private @Nullable Boolean success;

  private @Nullable String targetId;

  private @Nullable HackEnemy200ResponseEffect effect;

  private @Nullable BigDecimal heatGenerated;

  private @Nullable BigDecimal ramCost;

  public HackEnemy200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public HackEnemy200Response targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_id")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public HackEnemy200Response effect(@Nullable HackEnemy200ResponseEffect effect) {
    this.effect = effect;
    return this;
  }

  /**
   * Get effect
   * @return effect
   */
  @Valid 
  @Schema(name = "effect", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effect")
  public @Nullable HackEnemy200ResponseEffect getEffect() {
    return effect;
  }

  public void setEffect(@Nullable HackEnemy200ResponseEffect effect) {
    this.effect = effect;
  }

  public HackEnemy200Response heatGenerated(@Nullable BigDecimal heatGenerated) {
    this.heatGenerated = heatGenerated;
    return this;
  }

  /**
   * Перегрев кибердека (%)
   * @return heatGenerated
   */
  @Valid 
  @Schema(name = "heat_generated", description = "Перегрев кибердека (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heat_generated")
  public @Nullable BigDecimal getHeatGenerated() {
    return heatGenerated;
  }

  public void setHeatGenerated(@Nullable BigDecimal heatGenerated) {
    this.heatGenerated = heatGenerated;
  }

  public HackEnemy200Response ramCost(@Nullable BigDecimal ramCost) {
    this.ramCost = ramCost;
    return this;
  }

  /**
   * Затраты RAM
   * @return ramCost
   */
  @Valid 
  @Schema(name = "ram_cost", description = "Затраты RAM", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ram_cost")
  public @Nullable BigDecimal getRamCost() {
    return ramCost;
  }

  public void setRamCost(@Nullable BigDecimal ramCost) {
    this.ramCost = ramCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackEnemy200Response hackEnemy200Response = (HackEnemy200Response) o;
    return Objects.equals(this.success, hackEnemy200Response.success) &&
        Objects.equals(this.targetId, hackEnemy200Response.targetId) &&
        Objects.equals(this.effect, hackEnemy200Response.effect) &&
        Objects.equals(this.heatGenerated, hackEnemy200Response.heatGenerated) &&
        Objects.equals(this.ramCost, hackEnemy200Response.ramCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, targetId, effect, heatGenerated, ramCost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackEnemy200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    effect: ").append(toIndentedString(effect)).append("\n");
    sb.append("    heatGenerated: ").append(toIndentedString(heatGenerated)).append("\n");
    sb.append("    ramCost: ").append(toIndentedString(ramCost)).append("\n");
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

