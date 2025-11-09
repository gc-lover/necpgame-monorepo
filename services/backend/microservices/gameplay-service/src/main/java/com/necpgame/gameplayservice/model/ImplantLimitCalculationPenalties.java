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
 * Штрафы к лимиту
 */

@Schema(name = "ImplantLimitCalculation_penalties", description = "Штрафы к лимиту")
@JsonTypeName("ImplantLimitCalculation_penalties")

public class ImplantLimitCalculationPenalties {

  private @Nullable Integer humanity;

  private @Nullable Integer quality;

  public ImplantLimitCalculationPenalties humanity(@Nullable Integer humanity) {
    this.humanity = humanity;
    return this;
  }

  /**
   * Get humanity
   * maximum: 0
   * @return humanity
   */
  @Max(value = 0) 
  @Schema(name = "humanity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity")
  public @Nullable Integer getHumanity() {
    return humanity;
  }

  public void setHumanity(@Nullable Integer humanity) {
    this.humanity = humanity;
  }

  public ImplantLimitCalculationPenalties quality(@Nullable Integer quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * maximum: 0
   * @return quality
   */
  @Max(value = 0) 
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality")
  public @Nullable Integer getQuality() {
    return quality;
  }

  public void setQuality(@Nullable Integer quality) {
    this.quality = quality;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantLimitCalculationPenalties implantLimitCalculationPenalties = (ImplantLimitCalculationPenalties) o;
    return Objects.equals(this.humanity, implantLimitCalculationPenalties.humanity) &&
        Objects.equals(this.quality, implantLimitCalculationPenalties.quality);
  }

  @Override
  public int hashCode() {
    return Objects.hash(humanity, quality);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantLimitCalculationPenalties {\n");
    sb.append("    humanity: ").append(toIndentedString(humanity)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
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

