package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * AbilityCooldown
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:49:04.787810800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class AbilityCooldown {

  private @Nullable String abilityId;

  private @Nullable BigDecimal remainingTime;

  private @Nullable BigDecimal totalTime;

  private @Nullable Boolean isReady;

  public AbilityCooldown abilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "ability_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ability_id")
  public @Nullable String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
  }

  public AbilityCooldown remainingTime(@Nullable BigDecimal remainingTime) {
    this.remainingTime = remainingTime;
    return this;
  }

  /**
   * Оставшееся время кулдауна (секунды)
   * @return remainingTime
   */
  @Valid 
  @Schema(name = "remaining_time", description = "Оставшееся время кулдауна (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remaining_time")
  public @Nullable BigDecimal getRemainingTime() {
    return remainingTime;
  }

  public void setRemainingTime(@Nullable BigDecimal remainingTime) {
    this.remainingTime = remainingTime;
  }

  public AbilityCooldown totalTime(@Nullable BigDecimal totalTime) {
    this.totalTime = totalTime;
    return this;
  }

  /**
   * Полное время кулдауна
   * @return totalTime
   */
  @Valid 
  @Schema(name = "total_time", description = "Полное время кулдауна", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_time")
  public @Nullable BigDecimal getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(@Nullable BigDecimal totalTime) {
    this.totalTime = totalTime;
  }

  public AbilityCooldown isReady(@Nullable Boolean isReady) {
    this.isReady = isReady;
    return this;
  }

  /**
   * Готова ли способность к использованию
   * @return isReady
   */
  
  @Schema(name = "is_ready", description = "Готова ли способность к использованию", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_ready")
  public @Nullable Boolean getIsReady() {
    return isReady;
  }

  public void setIsReady(@Nullable Boolean isReady) {
    this.isReady = isReady;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityCooldown abilityCooldown = (AbilityCooldown) o;
    return Objects.equals(this.abilityId, abilityCooldown.abilityId) &&
        Objects.equals(this.remainingTime, abilityCooldown.remainingTime) &&
        Objects.equals(this.totalTime, abilityCooldown.totalTime) &&
        Objects.equals(this.isReady, abilityCooldown.isReady);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilityId, remainingTime, totalTime, isReady);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityCooldown {\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
    sb.append("    remainingTime: ").append(toIndentedString(remainingTime)).append("\n");
    sb.append("    totalTime: ").append(toIndentedString(totalTime)).append("\n");
    sb.append("    isReady: ").append(toIndentedString(isReady)).append("\n");
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

