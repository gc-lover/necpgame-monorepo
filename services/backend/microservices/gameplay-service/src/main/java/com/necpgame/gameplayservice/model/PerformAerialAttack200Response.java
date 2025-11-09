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
 * PerformAerialAttack200Response
 */

@JsonTypeName("performAerialAttack_200_response")

public class PerformAerialAttack200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal damage;

  private @Nullable BigDecimal bonusDamage;

  private @Nullable Boolean stunApplied;

  public PerformAerialAttack200Response success(@Nullable Boolean success) {
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

  public PerformAerialAttack200Response damage(@Nullable BigDecimal damage) {
    this.damage = damage;
    return this;
  }

  /**
   * Get damage
   * @return damage
   */
  @Valid 
  @Schema(name = "damage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage")
  public @Nullable BigDecimal getDamage() {
    return damage;
  }

  public void setDamage(@Nullable BigDecimal damage) {
    this.damage = damage;
  }

  public PerformAerialAttack200Response bonusDamage(@Nullable BigDecimal bonusDamage) {
    this.bonusDamage = bonusDamage;
    return this;
  }

  /**
   * Бонусный урон от высоты
   * @return bonusDamage
   */
  @Valid 
  @Schema(name = "bonus_damage", description = "Бонусный урон от высоты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus_damage")
  public @Nullable BigDecimal getBonusDamage() {
    return bonusDamage;
  }

  public void setBonusDamage(@Nullable BigDecimal bonusDamage) {
    this.bonusDamage = bonusDamage;
  }

  public PerformAerialAttack200Response stunApplied(@Nullable Boolean stunApplied) {
    this.stunApplied = stunApplied;
    return this;
  }

  /**
   * Get stunApplied
   * @return stunApplied
   */
  
  @Schema(name = "stun_applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stun_applied")
  public @Nullable Boolean getStunApplied() {
    return stunApplied;
  }

  public void setStunApplied(@Nullable Boolean stunApplied) {
    this.stunApplied = stunApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformAerialAttack200Response performAerialAttack200Response = (PerformAerialAttack200Response) o;
    return Objects.equals(this.success, performAerialAttack200Response.success) &&
        Objects.equals(this.damage, performAerialAttack200Response.damage) &&
        Objects.equals(this.bonusDamage, performAerialAttack200Response.bonusDamage) &&
        Objects.equals(this.stunApplied, performAerialAttack200Response.stunApplied);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, damage, bonusDamage, stunApplied);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformAerialAttack200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
    sb.append("    bonusDamage: ").append(toIndentedString(bonusDamage)).append("\n");
    sb.append("    stunApplied: ").append(toIndentedString(stunApplied)).append("\n");
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

