package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RiskFactorOverride
 */


public class RiskFactorOverride {

  /**
   * Gets or Sets factor
   */
  public enum FactorEnum {
    REGION_THREAT("regionThreat"),
    
    FACTION_CONFLICT("factionConflict"),
    
    ESCROW_HISTORY("escrowHistory"),
    
    RATING_VARIANCE("ratingVariance"),
    
    DISPUTE_RATE("disputeRate");

    private final String value;

    FactorEnum(String value) {
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
    public static FactorEnum fromValue(String value) {
      for (FactorEnum b : FactorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private FactorEnum factor;

  private Float value;

  public RiskFactorOverride() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskFactorOverride(FactorEnum factor, Float value) {
    this.factor = factor;
    this.value = value;
  }

  public RiskFactorOverride factor(FactorEnum factor) {
    this.factor = factor;
    return this;
  }

  /**
   * Get factor
   * @return factor
   */
  @NotNull 
  @Schema(name = "factor", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factor")
  public FactorEnum getFactor() {
    return factor;
  }

  public void setFactor(FactorEnum factor) {
    this.factor = factor;
  }

  public RiskFactorOverride value(Float value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @NotNull 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskFactorOverride riskFactorOverride = (RiskFactorOverride) o;
    return Objects.equals(this.factor, riskFactorOverride.factor) &&
        Objects.equals(this.value, riskFactorOverride.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factor, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskFactorOverride {\n");
    sb.append("    factor: ").append(toIndentedString(factor)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

