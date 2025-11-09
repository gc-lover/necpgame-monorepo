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
 * Статус цели после выстрела
 */

@Schema(name = "ShootResult_target_status", description = "Статус цели после выстрела")
@JsonTypeName("ShootResult_target_status")

public class ShootResultTargetStatus {

  private @Nullable BigDecimal hpRemaining;

  private @Nullable Boolean isDead;

  public ShootResultTargetStatus hpRemaining(@Nullable BigDecimal hpRemaining) {
    this.hpRemaining = hpRemaining;
    return this;
  }

  /**
   * Get hpRemaining
   * minimum: 0
   * @return hpRemaining
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "hp_remaining", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hp_remaining")
  public @Nullable BigDecimal getHpRemaining() {
    return hpRemaining;
  }

  public void setHpRemaining(@Nullable BigDecimal hpRemaining) {
    this.hpRemaining = hpRemaining;
  }

  public ShootResultTargetStatus isDead(@Nullable Boolean isDead) {
    this.isDead = isDead;
    return this;
  }

  /**
   * Get isDead
   * @return isDead
   */
  
  @Schema(name = "is_dead", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_dead")
  public @Nullable Boolean getIsDead() {
    return isDead;
  }

  public void setIsDead(@Nullable Boolean isDead) {
    this.isDead = isDead;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShootResultTargetStatus shootResultTargetStatus = (ShootResultTargetStatus) o;
    return Objects.equals(this.hpRemaining, shootResultTargetStatus.hpRemaining) &&
        Objects.equals(this.isDead, shootResultTargetStatus.isDead);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hpRemaining, isDead);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShootResultTargetStatus {\n");
    sb.append("    hpRemaining: ").append(toIndentedString(hpRemaining)).append("\n");
    sb.append("    isDead: ").append(toIndentedString(isDead)).append("\n");
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

