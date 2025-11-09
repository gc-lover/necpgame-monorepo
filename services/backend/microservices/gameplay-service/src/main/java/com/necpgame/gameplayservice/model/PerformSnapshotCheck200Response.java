package com.necpgame.gameplayservice.model;

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
 * PerformSnapshotCheck200Response
 */

@JsonTypeName("performSnapshotCheck_200_response")

public class PerformSnapshotCheck200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal damageBonus;

  private @Nullable BigDecimal criticalMultiplier;

  public PerformSnapshotCheck200Response success(@Nullable Boolean success) {
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

  public PerformSnapshotCheck200Response damageBonus(@Nullable BigDecimal damageBonus) {
    this.damageBonus = damageBonus;
    return this;
  }

  /**
   * Get damageBonus
   * @return damageBonus
   */
  @Valid 
  @Schema(name = "damage_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_bonus")
  public @Nullable BigDecimal getDamageBonus() {
    return damageBonus;
  }

  public void setDamageBonus(@Nullable BigDecimal damageBonus) {
    this.damageBonus = damageBonus;
  }

  public PerformSnapshotCheck200Response criticalMultiplier(@Nullable BigDecimal criticalMultiplier) {
    this.criticalMultiplier = criticalMultiplier;
    return this;
  }

  /**
   * Get criticalMultiplier
   * @return criticalMultiplier
   */
  @Valid 
  @Schema(name = "critical_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_multiplier")
  public @Nullable BigDecimal getCriticalMultiplier() {
    return criticalMultiplier;
  }

  public void setCriticalMultiplier(@Nullable BigDecimal criticalMultiplier) {
    this.criticalMultiplier = criticalMultiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformSnapshotCheck200Response performSnapshotCheck200Response = (PerformSnapshotCheck200Response) o;
    return Objects.equals(this.success, performSnapshotCheck200Response.success) &&
        Objects.equals(this.damageBonus, performSnapshotCheck200Response.damageBonus) &&
        Objects.equals(this.criticalMultiplier, performSnapshotCheck200Response.criticalMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, damageBonus, criticalMultiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformSnapshotCheck200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    damageBonus: ").append(toIndentedString(damageBonus)).append("\n");
    sb.append("    criticalMultiplier: ").append(toIndentedString(criticalMultiplier)).append("\n");
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

