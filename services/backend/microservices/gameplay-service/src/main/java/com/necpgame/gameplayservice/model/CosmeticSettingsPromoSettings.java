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
 * CosmeticSettingsPromoSettings
 */

@JsonTypeName("CosmeticSettings_promoSettings")

public class CosmeticSettingsPromoSettings {

  private @Nullable Integer promoCodeLength;

  private @Nullable Integer promoTTLHours;

  public CosmeticSettingsPromoSettings promoCodeLength(@Nullable Integer promoCodeLength) {
    this.promoCodeLength = promoCodeLength;
    return this;
  }

  /**
   * Get promoCodeLength
   * @return promoCodeLength
   */
  
  @Schema(name = "promoCodeLength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("promoCodeLength")
  public @Nullable Integer getPromoCodeLength() {
    return promoCodeLength;
  }

  public void setPromoCodeLength(@Nullable Integer promoCodeLength) {
    this.promoCodeLength = promoCodeLength;
  }

  public CosmeticSettingsPromoSettings promoTTLHours(@Nullable Integer promoTTLHours) {
    this.promoTTLHours = promoTTLHours;
    return this;
  }

  /**
   * Get promoTTLHours
   * @return promoTTLHours
   */
  
  @Schema(name = "promoTTLHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("promoTTLHours")
  public @Nullable Integer getPromoTTLHours() {
    return promoTTLHours;
  }

  public void setPromoTTLHours(@Nullable Integer promoTTLHours) {
    this.promoTTLHours = promoTTLHours;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticSettingsPromoSettings cosmeticSettingsPromoSettings = (CosmeticSettingsPromoSettings) o;
    return Objects.equals(this.promoCodeLength, cosmeticSettingsPromoSettings.promoCodeLength) &&
        Objects.equals(this.promoTTLHours, cosmeticSettingsPromoSettings.promoTTLHours);
  }

  @Override
  public int hashCode() {
    return Objects.hash(promoCodeLength, promoTTLHours);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticSettingsPromoSettings {\n");
    sb.append("    promoCodeLength: ").append(toIndentedString(promoCodeLength)).append("\n");
    sb.append("    promoTTLHours: ").append(toIndentedString(promoTTLHours)).append("\n");
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

