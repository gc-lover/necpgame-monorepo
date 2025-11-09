package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InvestmentOpportunity
 */


public class InvestmentOpportunity {

  private @Nullable String opportunityId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CORPORATE("CORPORATE"),
    
    FACTION("FACTION"),
    
    REGIONAL("REGIONAL"),
    
    REAL_ESTATE("REAL_ESTATE"),
    
    PRODUCTION_CHAINS("PRODUCTION_CHAINS");

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

  private @Nullable String description;

  private @Nullable Integer minInvestment;

  private JsonNullable<Integer> maxInvestment = JsonNullable.<Integer>undefined();

  private @Nullable Float expectedRoi;

  /**
   * Gets or Sets riskLevel
   */
  public enum RiskLevelEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH"),
    
    VERY_HIGH("VERY_HIGH");

    private final String value;

    RiskLevelEnum(String value) {
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
    public static RiskLevelEnum fromValue(String value) {
      for (RiskLevelEnum b : RiskLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RiskLevelEnum riskLevel;

  private @Nullable Integer durationDays;

  private @Nullable Integer availableSlots;

  public InvestmentOpportunity opportunityId(@Nullable String opportunityId) {
    this.opportunityId = opportunityId;
    return this;
  }

  /**
   * Get opportunityId
   * @return opportunityId
   */
  
  @Schema(name = "opportunity_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opportunity_id")
  public @Nullable String getOpportunityId() {
    return opportunityId;
  }

  public void setOpportunityId(@Nullable String opportunityId) {
    this.opportunityId = opportunityId;
  }

  public InvestmentOpportunity name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public InvestmentOpportunity type(@Nullable TypeEnum type) {
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

  public InvestmentOpportunity description(@Nullable String description) {
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

  public InvestmentOpportunity minInvestment(@Nullable Integer minInvestment) {
    this.minInvestment = minInvestment;
    return this;
  }

  /**
   * Get minInvestment
   * @return minInvestment
   */
  
  @Schema(name = "min_investment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_investment")
  public @Nullable Integer getMinInvestment() {
    return minInvestment;
  }

  public void setMinInvestment(@Nullable Integer minInvestment) {
    this.minInvestment = minInvestment;
  }

  public InvestmentOpportunity maxInvestment(Integer maxInvestment) {
    this.maxInvestment = JsonNullable.of(maxInvestment);
    return this;
  }

  /**
   * Get maxInvestment
   * @return maxInvestment
   */
  
  @Schema(name = "max_investment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_investment")
  public JsonNullable<Integer> getMaxInvestment() {
    return maxInvestment;
  }

  public void setMaxInvestment(JsonNullable<Integer> maxInvestment) {
    this.maxInvestment = maxInvestment;
  }

  public InvestmentOpportunity expectedRoi(@Nullable Float expectedRoi) {
    this.expectedRoi = expectedRoi;
    return this;
  }

  /**
   * Ожидаемый ROI (%)
   * @return expectedRoi
   */
  
  @Schema(name = "expected_roi", description = "Ожидаемый ROI (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expected_roi")
  public @Nullable Float getExpectedRoi() {
    return expectedRoi;
  }

  public void setExpectedRoi(@Nullable Float expectedRoi) {
    this.expectedRoi = expectedRoi;
  }

  public InvestmentOpportunity riskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Get riskLevel
   * @return riskLevel
   */
  
  @Schema(name = "risk_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_level")
  public @Nullable RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public InvestmentOpportunity durationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
    return this;
  }

  /**
   * Срок инвестиции
   * @return durationDays
   */
  
  @Schema(name = "duration_days", description = "Срок инвестиции", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public @Nullable Integer getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
  }

  public InvestmentOpportunity availableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
    return this;
  }

  /**
   * Доступно мест для инвесторов
   * @return availableSlots
   */
  
  @Schema(name = "available_slots", description = "Доступно мест для инвесторов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_slots")
  public @Nullable Integer getAvailableSlots() {
    return availableSlots;
  }

  public void setAvailableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvestmentOpportunity investmentOpportunity = (InvestmentOpportunity) o;
    return Objects.equals(this.opportunityId, investmentOpportunity.opportunityId) &&
        Objects.equals(this.name, investmentOpportunity.name) &&
        Objects.equals(this.type, investmentOpportunity.type) &&
        Objects.equals(this.description, investmentOpportunity.description) &&
        Objects.equals(this.minInvestment, investmentOpportunity.minInvestment) &&
        equalsNullable(this.maxInvestment, investmentOpportunity.maxInvestment) &&
        Objects.equals(this.expectedRoi, investmentOpportunity.expectedRoi) &&
        Objects.equals(this.riskLevel, investmentOpportunity.riskLevel) &&
        Objects.equals(this.durationDays, investmentOpportunity.durationDays) &&
        Objects.equals(this.availableSlots, investmentOpportunity.availableSlots);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(opportunityId, name, type, description, minInvestment, hashCodeNullable(maxInvestment), expectedRoi, riskLevel, durationDays, availableSlots);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvestmentOpportunity {\n");
    sb.append("    opportunityId: ").append(toIndentedString(opportunityId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    minInvestment: ").append(toIndentedString(minInvestment)).append("\n");
    sb.append("    maxInvestment: ").append(toIndentedString(maxInvestment)).append("\n");
    sb.append("    expectedRoi: ").append(toIndentedString(expectedRoi)).append("\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
    sb.append("    availableSlots: ").append(toIndentedString(availableSlots)).append("\n");
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

