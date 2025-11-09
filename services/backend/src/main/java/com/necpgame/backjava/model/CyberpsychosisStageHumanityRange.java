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
 * Р”РёР°РїР°Р·РѕРЅ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РґР»СЏ СЃС‚Р°РґРёРё
 */

@Schema(name = "CyberpsychosisStage_humanity_range", description = "Р”РёР°РїР°Р·РѕРЅ РїРѕС‚РµСЂРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РґР»СЏ СЃС‚Р°РґРёРё")
@JsonTypeName("CyberpsychosisStage_humanity_range")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CyberpsychosisStageHumanityRange {

  private @Nullable Float min;

  private @Nullable Float max;

  public CyberpsychosisStageHumanityRange min(@Nullable Float min) {
    this.min = min;
    return this;
  }

  /**
   * Get min
   * @return min
   */
  
  @Schema(name = "min", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min")
  public @Nullable Float getMin() {
    return min;
  }

  public void setMin(@Nullable Float min) {
    this.min = min;
  }

  public CyberpsychosisStageHumanityRange max(@Nullable Float max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  public @Nullable Float getMax() {
    return max;
  }

  public void setMax(@Nullable Float max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberpsychosisStageHumanityRange cyberpsychosisStageHumanityRange = (CyberpsychosisStageHumanityRange) o;
    return Objects.equals(this.min, cyberpsychosisStageHumanityRange.min) &&
        Objects.equals(this.max, cyberpsychosisStageHumanityRange.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberpsychosisStageHumanityRange {\n");
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

