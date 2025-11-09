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
 * CosmeticSettingsGiftingLimits
 */

@JsonTypeName("CosmeticSettings_giftingLimits")

public class CosmeticSettingsGiftingLimits {

  private @Nullable Integer perDay;

  private @Nullable Integer perWeek;

  public CosmeticSettingsGiftingLimits perDay(@Nullable Integer perDay) {
    this.perDay = perDay;
    return this;
  }

  /**
   * Get perDay
   * @return perDay
   */
  
  @Schema(name = "perDay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perDay")
  public @Nullable Integer getPerDay() {
    return perDay;
  }

  public void setPerDay(@Nullable Integer perDay) {
    this.perDay = perDay;
  }

  public CosmeticSettingsGiftingLimits perWeek(@Nullable Integer perWeek) {
    this.perWeek = perWeek;
    return this;
  }

  /**
   * Get perWeek
   * @return perWeek
   */
  
  @Schema(name = "perWeek", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perWeek")
  public @Nullable Integer getPerWeek() {
    return perWeek;
  }

  public void setPerWeek(@Nullable Integer perWeek) {
    this.perWeek = perWeek;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticSettingsGiftingLimits cosmeticSettingsGiftingLimits = (CosmeticSettingsGiftingLimits) o;
    return Objects.equals(this.perDay, cosmeticSettingsGiftingLimits.perDay) &&
        Objects.equals(this.perWeek, cosmeticSettingsGiftingLimits.perWeek);
  }

  @Override
  public int hashCode() {
    return Objects.hash(perDay, perWeek);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticSettingsGiftingLimits {\n");
    sb.append("    perDay: ").append(toIndentedString(perDay)).append("\n");
    sb.append("    perWeek: ").append(toIndentedString(perWeek)).append("\n");
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

