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
 * BudgetComparisonRequest
 */


public class BudgetComparisonRequest {

  private Float proposedBudget;

  /**
   * Gets or Sets templateCode
   */
  public enum TemplateCodeEnum {
    COMBAT("combat"),
    
    HACKER("hacker"),
    
    ECONOMY("economy"),
    
    SOCIAL("social"),
    
    EXPLORATION("exploration");

    private final String value;

    TemplateCodeEnum(String value) {
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
    public static TemplateCodeEnum fromValue(String value) {
      for (TemplateCodeEnum b : TemplateCodeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TemplateCodeEnum templateCode;

  private String districtCode;

  private @Nullable String factionCode;

  private @Nullable String currency;

  public BudgetComparisonRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetComparisonRequest(Float proposedBudget, TemplateCodeEnum templateCode, String districtCode) {
    this.proposedBudget = proposedBudget;
    this.templateCode = templateCode;
    this.districtCode = districtCode;
  }

  public BudgetComparisonRequest proposedBudget(Float proposedBudget) {
    this.proposedBudget = proposedBudget;
    return this;
  }

  /**
   * Get proposedBudget
   * minimum: 0
   * @return proposedBudget
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "proposedBudget", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("proposedBudget")
  public Float getProposedBudget() {
    return proposedBudget;
  }

  public void setProposedBudget(Float proposedBudget) {
    this.proposedBudget = proposedBudget;
  }

  public BudgetComparisonRequest templateCode(TemplateCodeEnum templateCode) {
    this.templateCode = templateCode;
    return this;
  }

  /**
   * Get templateCode
   * @return templateCode
   */
  @NotNull 
  @Schema(name = "templateCode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateCode")
  public TemplateCodeEnum getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(TemplateCodeEnum templateCode) {
    this.templateCode = templateCode;
  }

  public BudgetComparisonRequest districtCode(String districtCode) {
    this.districtCode = districtCode;
    return this;
  }

  /**
   * Get districtCode
   * @return districtCode
   */
  @NotNull @Size(min = 2, max = 32) 
  @Schema(name = "districtCode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("districtCode")
  public String getDistrictCode() {
    return districtCode;
  }

  public void setDistrictCode(String districtCode) {
    this.districtCode = districtCode;
  }

  public BudgetComparisonRequest factionCode(@Nullable String factionCode) {
    this.factionCode = factionCode;
    return this;
  }

  /**
   * Get factionCode
   * @return factionCode
   */
  @Size(min = 2, max = 32) 
  @Schema(name = "factionCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionCode")
  public @Nullable String getFactionCode() {
    return factionCode;
  }

  public void setFactionCode(@Nullable String factionCode) {
    this.factionCode = factionCode;
  }

  public BudgetComparisonRequest currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Pattern(regexp = "^[A-Z]{3}$") 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetComparisonRequest budgetComparisonRequest = (BudgetComparisonRequest) o;
    return Objects.equals(this.proposedBudget, budgetComparisonRequest.proposedBudget) &&
        Objects.equals(this.templateCode, budgetComparisonRequest.templateCode) &&
        Objects.equals(this.districtCode, budgetComparisonRequest.districtCode) &&
        Objects.equals(this.factionCode, budgetComparisonRequest.factionCode) &&
        Objects.equals(this.currency, budgetComparisonRequest.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(proposedBudget, templateCode, districtCode, factionCode, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetComparisonRequest {\n");
    sb.append("    proposedBudget: ").append(toIndentedString(proposedBudget)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    districtCode: ").append(toIndentedString(districtCode)).append("\n");
    sb.append("    factionCode: ").append(toIndentedString(factionCode)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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

