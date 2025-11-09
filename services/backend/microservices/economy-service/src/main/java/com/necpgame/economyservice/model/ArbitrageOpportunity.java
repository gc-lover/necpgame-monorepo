package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * ArbitrageOpportunity
 */


public class ArbitrageOpportunity {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    REGIONAL("REGIONAL"),
    
    TRIANGULAR("TRIANGULAR");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  @Valid
  private List<String> pairs = new ArrayList<>();

  private @Nullable Float profitPotential;

  private @Nullable String description;

  private @Nullable Integer expiresInSeconds;

  public ArbitrageOpportunity type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public ArbitrageOpportunity pairs(List<String> pairs) {
    this.pairs = pairs;
    return this;
  }

  public ArbitrageOpportunity addPairsItem(String pairsItem) {
    if (this.pairs == null) {
      this.pairs = new ArrayList<>();
    }
    this.pairs.add(pairsItem);
    return this;
  }

  /**
   * Get pairs
   * @return pairs
   */
  
  @Schema(name = "pairs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pairs")
  public List<String> getPairs() {
    return pairs;
  }

  public void setPairs(List<String> pairs) {
    this.pairs = pairs;
  }

  public ArbitrageOpportunity profitPotential(@Nullable Float profitPotential) {
    this.profitPotential = profitPotential;
    return this;
  }

  /**
   * Потенциальная прибыль (%)
   * @return profitPotential
   */
  
  @Schema(name = "profit_potential", description = "Потенциальная прибыль (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_potential")
  public @Nullable Float getProfitPotential() {
    return profitPotential;
  }

  public void setProfitPotential(@Nullable Float profitPotential) {
    this.profitPotential = profitPotential;
  }

  public ArbitrageOpportunity description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ArbitrageOpportunity expiresInSeconds(@Nullable Integer expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
    return this;
  }

  /**
   * Get expiresInSeconds
   * @return expiresInSeconds
   */
  
  @Schema(name = "expires_in_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_in_seconds")
  public @Nullable Integer getExpiresInSeconds() {
    return expiresInSeconds;
  }

  public void setExpiresInSeconds(@Nullable Integer expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArbitrageOpportunity arbitrageOpportunity = (ArbitrageOpportunity) o;
    return Objects.equals(this.type, arbitrageOpportunity.type) &&
        Objects.equals(this.pairs, arbitrageOpportunity.pairs) &&
        Objects.equals(this.profitPotential, arbitrageOpportunity.profitPotential) &&
        Objects.equals(this.description, arbitrageOpportunity.description) &&
        Objects.equals(this.expiresInSeconds, arbitrageOpportunity.expiresInSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, pairs, profitPotential, description, expiresInSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArbitrageOpportunity {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    pairs: ").append(toIndentedString(pairs)).append("\n");
    sb.append("    profitPotential: ").append(toIndentedString(profitPotential)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    expiresInSeconds: ").append(toIndentedString(expiresInSeconds)).append("\n");
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

