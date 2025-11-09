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
 * Бонусы к лимиту
 */

@Schema(name = "ImplantLimitInfo_bonuses", description = "Бонусы к лимиту")
@JsonTypeName("ImplantLimitInfo_bonuses")

public class ImplantLimitInfoBonuses {

  private @Nullable Integer propertyClass;

  private @Nullable Integer progression;

  private @Nullable Integer other;

  public ImplantLimitInfoBonuses propertyClass(@Nullable Integer propertyClass) {
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

  public ImplantLimitInfoBonuses progression(@Nullable Integer progression) {
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

  public ImplantLimitInfoBonuses other(@Nullable Integer other) {
    this.other = other;
    return this;
  }

  /**
   * Get other
   * minimum: 0
   * @return other
   */
  @Min(value = 0) 
  @Schema(name = "other", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("other")
  public @Nullable Integer getOther() {
    return other;
  }

  public void setOther(@Nullable Integer other) {
    this.other = other;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantLimitInfoBonuses implantLimitInfoBonuses = (ImplantLimitInfoBonuses) o;
    return Objects.equals(this.propertyClass, implantLimitInfoBonuses.propertyClass) &&
        Objects.equals(this.progression, implantLimitInfoBonuses.progression) &&
        Objects.equals(this.other, implantLimitInfoBonuses.other);
  }

  @Override
  public int hashCode() {
    return Objects.hash(propertyClass, progression, other);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantLimitInfoBonuses {\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    progression: ").append(toIndentedString(progression)).append("\n");
    sb.append("    other: ").append(toIndentedString(other)).append("\n");
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

