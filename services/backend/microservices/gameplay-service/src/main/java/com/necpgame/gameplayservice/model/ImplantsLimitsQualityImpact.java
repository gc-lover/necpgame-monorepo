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
 * Влияние качества имплантов
 */

@Schema(name = "ImplantsLimits_quality_impact", description = "Влияние качества имплантов")
@JsonTypeName("ImplantsLimits_quality_impact")

public class ImplantsLimitsQualityImpact {

  private @Nullable Integer highQualityBonus;

  public ImplantsLimitsQualityImpact highQualityBonus(@Nullable Integer highQualityBonus) {
    this.highQualityBonus = highQualityBonus;
    return this;
  }

  /**
   * Бонус слотов от качественных имплантов
   * @return highQualityBonus
   */
  
  @Schema(name = "high_quality_bonus", description = "Бонус слотов от качественных имплантов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high_quality_bonus")
  public @Nullable Integer getHighQualityBonus() {
    return highQualityBonus;
  }

  public void setHighQualityBonus(@Nullable Integer highQualityBonus) {
    this.highQualityBonus = highQualityBonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantsLimitsQualityImpact implantsLimitsQualityImpact = (ImplantsLimitsQualityImpact) o;
    return Objects.equals(this.highQualityBonus, implantsLimitsQualityImpact.highQualityBonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(highQualityBonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantsLimitsQualityImpact {\n");
    sb.append("    highQualityBonus: ").append(toIndentedString(highQualityBonus)).append("\n");
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

