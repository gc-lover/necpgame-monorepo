package com.necpgame.gameplayservice.model;

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
 * CoverPenetrationResult
 */


public class CoverPenetrationResult {

  private @Nullable Boolean penetrates;

  private @Nullable BigDecimal damageReduction;

  private @Nullable Boolean coverDestroyed;

  private @Nullable BigDecimal coverHealthRemaining;

  public CoverPenetrationResult penetrates(@Nullable Boolean penetrates) {
    this.penetrates = penetrates;
    return this;
  }

  /**
   * Проникает ли пуля
   * @return penetrates
   */
  
  @Schema(name = "penetrates", description = "Проникает ли пуля", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penetrates")
  public @Nullable Boolean getPenetrates() {
    return penetrates;
  }

  public void setPenetrates(@Nullable Boolean penetrates) {
    this.penetrates = penetrates;
  }

  public CoverPenetrationResult damageReduction(@Nullable BigDecimal damageReduction) {
    this.damageReduction = damageReduction;
    return this;
  }

  /**
   * Снижение урона при проникновении (%)
   * @return damageReduction
   */
  @Valid 
  @Schema(name = "damage_reduction", description = "Снижение урона при проникновении (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_reduction")
  public @Nullable BigDecimal getDamageReduction() {
    return damageReduction;
  }

  public void setDamageReduction(@Nullable BigDecimal damageReduction) {
    this.damageReduction = damageReduction;
  }

  public CoverPenetrationResult coverDestroyed(@Nullable Boolean coverDestroyed) {
    this.coverDestroyed = coverDestroyed;
    return this;
  }

  /**
   * Разрушено ли укрытие
   * @return coverDestroyed
   */
  
  @Schema(name = "cover_destroyed", description = "Разрушено ли укрытие", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cover_destroyed")
  public @Nullable Boolean getCoverDestroyed() {
    return coverDestroyed;
  }

  public void setCoverDestroyed(@Nullable Boolean coverDestroyed) {
    this.coverDestroyed = coverDestroyed;
  }

  public CoverPenetrationResult coverHealthRemaining(@Nullable BigDecimal coverHealthRemaining) {
    this.coverHealthRemaining = coverHealthRemaining;
    return this;
  }

  /**
   * Оставшаяся прочность укрытия
   * @return coverHealthRemaining
   */
  @Valid 
  @Schema(name = "cover_health_remaining", description = "Оставшаяся прочность укрытия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cover_health_remaining")
  public @Nullable BigDecimal getCoverHealthRemaining() {
    return coverHealthRemaining;
  }

  public void setCoverHealthRemaining(@Nullable BigDecimal coverHealthRemaining) {
    this.coverHealthRemaining = coverHealthRemaining;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoverPenetrationResult coverPenetrationResult = (CoverPenetrationResult) o;
    return Objects.equals(this.penetrates, coverPenetrationResult.penetrates) &&
        Objects.equals(this.damageReduction, coverPenetrationResult.damageReduction) &&
        Objects.equals(this.coverDestroyed, coverPenetrationResult.coverDestroyed) &&
        Objects.equals(this.coverHealthRemaining, coverPenetrationResult.coverHealthRemaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(penetrates, damageReduction, coverDestroyed, coverHealthRemaining);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoverPenetrationResult {\n");
    sb.append("    penetrates: ").append(toIndentedString(penetrates)).append("\n");
    sb.append("    damageReduction: ").append(toIndentedString(damageReduction)).append("\n");
    sb.append("    coverDestroyed: ").append(toIndentedString(coverDestroyed)).append("\n");
    sb.append("    coverHealthRemaining: ").append(toIndentedString(coverHealthRemaining)).append("\n");
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

