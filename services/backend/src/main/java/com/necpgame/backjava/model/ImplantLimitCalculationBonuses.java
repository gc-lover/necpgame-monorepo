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
 * Р‘РѕРЅСѓСЃС‹ Рє Р»РёРјРёС‚Сѓ
 */

@Schema(name = "ImplantLimitCalculation_bonuses", description = "Р‘РѕРЅСѓСЃС‹ Рє Р»РёРјРёС‚Сѓ")
@JsonTypeName("ImplantLimitCalculation_bonuses")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ImplantLimitCalculationBonuses {

  private @Nullable Integer propertyClass;

  private @Nullable Integer progression;

  public ImplantLimitCalculationBonuses propertyClass(@Nullable Integer propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Get propertyClass
   * minimum: 0
   * @return propertyClass
   */
  @Min(value = 0) 
  @Schema(name = "class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class")
  public @Nullable Integer getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(@Nullable Integer propertyClass) {
    this.propertyClass = propertyClass;
  }

  public ImplantLimitCalculationBonuses progression(@Nullable Integer progression) {
    this.progression = progression;
    return this;
  }

  /**
   * Get progression
   * minimum: 0
   * @return progression
   */
  @Min(value = 0) 
  @Schema(name = "progression", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progression")
  public @Nullable Integer getProgression() {
    return progression;
  }

  public void setProgression(@Nullable Integer progression) {
    this.progression = progression;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantLimitCalculationBonuses implantLimitCalculationBonuses = (ImplantLimitCalculationBonuses) o;
    return Objects.equals(this.propertyClass, implantLimitCalculationBonuses.propertyClass) &&
        Objects.equals(this.progression, implantLimitCalculationBonuses.progression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(propertyClass, progression);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantLimitCalculationBonuses {\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    progression: ").append(toIndentedString(progression)).append("\n");
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

