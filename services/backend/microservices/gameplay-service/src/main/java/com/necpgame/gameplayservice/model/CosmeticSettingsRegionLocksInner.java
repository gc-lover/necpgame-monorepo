package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CosmeticSettingsRegionLocksInner
 */

@JsonTypeName("CosmeticSettings_regionLocks_inner")

public class CosmeticSettingsRegionLocksInner {

  private @Nullable String region;

  @Valid
  private List<String> allowedCurrencies = new ArrayList<>();

  public CosmeticSettingsRegionLocksInner region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public CosmeticSettingsRegionLocksInner allowedCurrencies(List<String> allowedCurrencies) {
    this.allowedCurrencies = allowedCurrencies;
    return this;
  }

  public CosmeticSettingsRegionLocksInner addAllowedCurrenciesItem(String allowedCurrenciesItem) {
    if (this.allowedCurrencies == null) {
      this.allowedCurrencies = new ArrayList<>();
    }
    this.allowedCurrencies.add(allowedCurrenciesItem);
    return this;
  }

  /**
   * Get allowedCurrencies
   * @return allowedCurrencies
   */
  
  @Schema(name = "allowedCurrencies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowedCurrencies")
  public List<String> getAllowedCurrencies() {
    return allowedCurrencies;
  }

  public void setAllowedCurrencies(List<String> allowedCurrencies) {
    this.allowedCurrencies = allowedCurrencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticSettingsRegionLocksInner cosmeticSettingsRegionLocksInner = (CosmeticSettingsRegionLocksInner) o;
    return Objects.equals(this.region, cosmeticSettingsRegionLocksInner.region) &&
        Objects.equals(this.allowedCurrencies, cosmeticSettingsRegionLocksInner.allowedCurrencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, allowedCurrencies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticSettingsRegionLocksInner {\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    allowedCurrencies: ").append(toIndentedString(allowedCurrencies)).append("\n");
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

