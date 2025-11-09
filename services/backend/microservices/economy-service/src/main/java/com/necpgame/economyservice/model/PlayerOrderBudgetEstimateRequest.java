package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.BudgetModifier;
import java.math.BigDecimal;
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
 * PlayerOrderBudgetEstimateRequest
 */


public class PlayerOrderBudgetEstimateRequest {

  private Float complexityScore;

  private BigDecimal riskModifier;

  private BigDecimal marketIndex;

  private BigDecimal timeModifier;

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

  /**
   * Gets or Sets riskLevel
   */
  public enum RiskLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    SEVERE("severe"),
    
    EXTREME("extreme");

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

  private String preferredCurrency;

  /**
   * Gets or Sets guaranteeTier
   */
  public enum GuaranteeTierEnum {
    BASIC("basic"),
    
    EXTENDED("extended"),
    
    PREMIUM("premium");

    private final String value;

    GuaranteeTierEnum(String value) {
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
    public static GuaranteeTierEnum fromValue(String value) {
      for (GuaranteeTierEnum b : GuaranteeTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable GuaranteeTierEnum guaranteeTier;

  private Boolean isCorporate = false;

  private Boolean manualAdjustment = false;

  private @Nullable String adjustmentReason;

  @Valid
  private List<@Valid BudgetModifier> bonuses = new ArrayList<>();

  @Valid
  private List<@Valid BudgetModifier> penalties = new ArrayList<>();

  public PlayerOrderBudgetEstimateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBudgetEstimateRequest(Float complexityScore, BigDecimal riskModifier, BigDecimal marketIndex, BigDecimal timeModifier, TemplateCodeEnum templateCode, String districtCode, String preferredCurrency) {
    this.complexityScore = complexityScore;
    this.riskModifier = riskModifier;
    this.marketIndex = marketIndex;
    this.timeModifier = timeModifier;
    this.templateCode = templateCode;
    this.districtCode = districtCode;
    this.preferredCurrency = preferredCurrency;
  }

  public PlayerOrderBudgetEstimateRequest complexityScore(Float complexityScore) {
    this.complexityScore = complexityScore;
    return this;
  }

  /**
   * Get complexityScore
   * minimum: 0
   * @return complexityScore
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "complexityScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complexityScore")
  public Float getComplexityScore() {
    return complexityScore;
  }

  public void setComplexityScore(Float complexityScore) {
    this.complexityScore = complexityScore;
  }

  public PlayerOrderBudgetEstimateRequest riskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
    return this;
  }

  /**
   * Get riskModifier
   * minimum: 0.8
   * maximum: 1.5
   * @return riskModifier
   */
  @NotNull @Valid @DecimalMin(value = "0.8") @DecimalMax(value = "1.5") 
  @Schema(name = "riskModifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskModifier")
  public BigDecimal getRiskModifier() {
    return riskModifier;
  }

  public void setRiskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
  }

  public PlayerOrderBudgetEstimateRequest marketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
    return this;
  }

  /**
   * Get marketIndex
   * minimum: 0
   * @return marketIndex
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "marketIndex", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("marketIndex")
  public BigDecimal getMarketIndex() {
    return marketIndex;
  }

  public void setMarketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
  }

  public PlayerOrderBudgetEstimateRequest timeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
    return this;
  }

  /**
   * Get timeModifier
   * minimum: 1.0
   * maximum: 1.3
   * @return timeModifier
   */
  @NotNull @Valid @DecimalMin(value = "1.0") @DecimalMax(value = "1.3") 
  @Schema(name = "timeModifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeModifier")
  public BigDecimal getTimeModifier() {
    return timeModifier;
  }

  public void setTimeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
  }

  public PlayerOrderBudgetEstimateRequest templateCode(TemplateCodeEnum templateCode) {
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

  public PlayerOrderBudgetEstimateRequest districtCode(String districtCode) {
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

  public PlayerOrderBudgetEstimateRequest factionCode(@Nullable String factionCode) {
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

  public PlayerOrderBudgetEstimateRequest riskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Get riskLevel
   * @return riskLevel
   */
  
  @Schema(name = "riskLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("riskLevel")
  public @Nullable RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public PlayerOrderBudgetEstimateRequest preferredCurrency(String preferredCurrency) {
    this.preferredCurrency = preferredCurrency;
    return this;
  }

  /**
   * Get preferredCurrency
   * @return preferredCurrency
   */
  @NotNull @Pattern(regexp = "^[A-Z]{3}$") 
  @Schema(name = "preferredCurrency", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("preferredCurrency")
  public String getPreferredCurrency() {
    return preferredCurrency;
  }

  public void setPreferredCurrency(String preferredCurrency) {
    this.preferredCurrency = preferredCurrency;
  }

  public PlayerOrderBudgetEstimateRequest guaranteeTier(@Nullable GuaranteeTierEnum guaranteeTier) {
    this.guaranteeTier = guaranteeTier;
    return this;
  }

  /**
   * Get guaranteeTier
   * @return guaranteeTier
   */
  
  @Schema(name = "guaranteeTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteeTier")
  public @Nullable GuaranteeTierEnum getGuaranteeTier() {
    return guaranteeTier;
  }

  public void setGuaranteeTier(@Nullable GuaranteeTierEnum guaranteeTier) {
    this.guaranteeTier = guaranteeTier;
  }

  public PlayerOrderBudgetEstimateRequest isCorporate(Boolean isCorporate) {
    this.isCorporate = isCorporate;
    return this;
  }

  /**
   * Get isCorporate
   * @return isCorporate
   */
  
  @Schema(name = "isCorporate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isCorporate")
  public Boolean getIsCorporate() {
    return isCorporate;
  }

  public void setIsCorporate(Boolean isCorporate) {
    this.isCorporate = isCorporate;
  }

  public PlayerOrderBudgetEstimateRequest manualAdjustment(Boolean manualAdjustment) {
    this.manualAdjustment = manualAdjustment;
    return this;
  }

  /**
   * Get manualAdjustment
   * @return manualAdjustment
   */
  
  @Schema(name = "manualAdjustment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("manualAdjustment")
  public Boolean getManualAdjustment() {
    return manualAdjustment;
  }

  public void setManualAdjustment(Boolean manualAdjustment) {
    this.manualAdjustment = manualAdjustment;
  }

  public PlayerOrderBudgetEstimateRequest adjustmentReason(@Nullable String adjustmentReason) {
    this.adjustmentReason = adjustmentReason;
    return this;
  }

  /**
   * Get adjustmentReason
   * @return adjustmentReason
   */
  @Size(min = 3, max = 512) 
  @Schema(name = "adjustmentReason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("adjustmentReason")
  public @Nullable String getAdjustmentReason() {
    return adjustmentReason;
  }

  public void setAdjustmentReason(@Nullable String adjustmentReason) {
    this.adjustmentReason = adjustmentReason;
  }

  public PlayerOrderBudgetEstimateRequest bonuses(List<@Valid BudgetModifier> bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  public PlayerOrderBudgetEstimateRequest addBonusesItem(BudgetModifier bonusesItem) {
    if (this.bonuses == null) {
      this.bonuses = new ArrayList<>();
    }
    this.bonuses.add(bonusesItem);
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public List<@Valid BudgetModifier> getBonuses() {
    return bonuses;
  }

  public void setBonuses(List<@Valid BudgetModifier> bonuses) {
    this.bonuses = bonuses;
  }

  public PlayerOrderBudgetEstimateRequest penalties(List<@Valid BudgetModifier> penalties) {
    this.penalties = penalties;
    return this;
  }

  public PlayerOrderBudgetEstimateRequest addPenaltiesItem(BudgetModifier penaltiesItem) {
    if (this.penalties == null) {
      this.penalties = new ArrayList<>();
    }
    this.penalties.add(penaltiesItem);
    return this;
  }

  /**
   * Get penalties
   * @return penalties
   */
  @Valid 
  @Schema(name = "penalties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public List<@Valid BudgetModifier> getPenalties() {
    return penalties;
  }

  public void setPenalties(List<@Valid BudgetModifier> penalties) {
    this.penalties = penalties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderBudgetEstimateRequest playerOrderBudgetEstimateRequest = (PlayerOrderBudgetEstimateRequest) o;
    return Objects.equals(this.complexityScore, playerOrderBudgetEstimateRequest.complexityScore) &&
        Objects.equals(this.riskModifier, playerOrderBudgetEstimateRequest.riskModifier) &&
        Objects.equals(this.marketIndex, playerOrderBudgetEstimateRequest.marketIndex) &&
        Objects.equals(this.timeModifier, playerOrderBudgetEstimateRequest.timeModifier) &&
        Objects.equals(this.templateCode, playerOrderBudgetEstimateRequest.templateCode) &&
        Objects.equals(this.districtCode, playerOrderBudgetEstimateRequest.districtCode) &&
        Objects.equals(this.factionCode, playerOrderBudgetEstimateRequest.factionCode) &&
        Objects.equals(this.riskLevel, playerOrderBudgetEstimateRequest.riskLevel) &&
        Objects.equals(this.preferredCurrency, playerOrderBudgetEstimateRequest.preferredCurrency) &&
        Objects.equals(this.guaranteeTier, playerOrderBudgetEstimateRequest.guaranteeTier) &&
        Objects.equals(this.isCorporate, playerOrderBudgetEstimateRequest.isCorporate) &&
        Objects.equals(this.manualAdjustment, playerOrderBudgetEstimateRequest.manualAdjustment) &&
        Objects.equals(this.adjustmentReason, playerOrderBudgetEstimateRequest.adjustmentReason) &&
        Objects.equals(this.bonuses, playerOrderBudgetEstimateRequest.bonuses) &&
        Objects.equals(this.penalties, playerOrderBudgetEstimateRequest.penalties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complexityScore, riskModifier, marketIndex, timeModifier, templateCode, districtCode, factionCode, riskLevel, preferredCurrency, guaranteeTier, isCorporate, manualAdjustment, adjustmentReason, bonuses, penalties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBudgetEstimateRequest {\n");
    sb.append("    complexityScore: ").append(toIndentedString(complexityScore)).append("\n");
    sb.append("    riskModifier: ").append(toIndentedString(riskModifier)).append("\n");
    sb.append("    marketIndex: ").append(toIndentedString(marketIndex)).append("\n");
    sb.append("    timeModifier: ").append(toIndentedString(timeModifier)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    districtCode: ").append(toIndentedString(districtCode)).append("\n");
    sb.append("    factionCode: ").append(toIndentedString(factionCode)).append("\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    preferredCurrency: ").append(toIndentedString(preferredCurrency)).append("\n");
    sb.append("    guaranteeTier: ").append(toIndentedString(guaranteeTier)).append("\n");
    sb.append("    isCorporate: ").append(toIndentedString(isCorporate)).append("\n");
    sb.append("    manualAdjustment: ").append(toIndentedString(manualAdjustment)).append("\n");
    sb.append("    adjustmentReason: ").append(toIndentedString(adjustmentReason)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    penalties: ").append(toIndentedString(penalties)).append("\n");
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

