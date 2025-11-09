package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.BudgetEstimateRequestManualAdjustment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BudgetEstimateRequest
 */


public class BudgetEstimateRequest {

  private @Nullable UUID orderId;

  private String templateCode;

  private BigDecimal complexityScore;

  private BigDecimal riskModifier;

  private BigDecimal marketIndex;

  private BigDecimal timeModifier;

  private @Nullable String districtCode;

  private @Nullable String factionCode;

  private @Nullable String playerRank;

  private String preferredCurrency;

  private Boolean isCorporate = false;

  /**
   * Выбранный страховой пакет.
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

  @Valid
  private List<String> bonuses = new ArrayList<>();

  @Valid
  private List<String> penalties = new ArrayList<>();

  private @Nullable BudgetEstimateRequestManualAdjustment manualAdjustment;

  private @Nullable UUID auditTraceId;

  public BudgetEstimateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetEstimateRequest(String templateCode, BigDecimal complexityScore, BigDecimal riskModifier, BigDecimal marketIndex, BigDecimal timeModifier, String preferredCurrency) {
    this.templateCode = templateCode;
    this.complexityScore = complexityScore;
    this.riskModifier = riskModifier;
    this.marketIndex = marketIndex;
    this.timeModifier = timeModifier;
    this.preferredCurrency = preferredCurrency;
  }

  public BudgetEstimateRequest orderId(@Nullable UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Идентификатор заказа для повторного расчёта.
   * @return orderId
   */
  @Valid 
  @Schema(name = "orderId", description = "Идентификатор заказа для повторного расчёта.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderId")
  public @Nullable UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable UUID orderId) {
    this.orderId = orderId;
  }

  public BudgetEstimateRequest templateCode(String templateCode) {
    this.templateCode = templateCode;
    return this;
  }

  /**
   * Код шаблона заказа (combat, hacker, economic и др.).
   * @return templateCode
   */
  @NotNull 
  @Schema(name = "templateCode", example = "combat", description = "Код шаблона заказа (combat, hacker, economic и др.).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateCode")
  public String getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(String templateCode) {
    this.templateCode = templateCode;
  }

  public BudgetEstimateRequest complexityScore(BigDecimal complexityScore) {
    this.complexityScore = complexityScore;
    return this;
  }

  /**
   * Итоговый показатель сложности заказа.
   * minimum: 0
   * @return complexityScore
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "complexityScore", example = "275.5", description = "Итоговый показатель сложности заказа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complexityScore")
  public BigDecimal getComplexityScore() {
    return complexityScore;
  }

  public void setComplexityScore(BigDecimal complexityScore) {
    this.complexityScore = complexityScore;
  }

  public BudgetEstimateRequest riskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
    return this;
  }

  /**
   * Модификатор риска (0.8–1.5).
   * minimum: 0.8
   * maximum: 1.5
   * @return riskModifier
   */
  @NotNull @Valid @DecimalMin(value = "0.8") @DecimalMax(value = "1.5") 
  @Schema(name = "riskModifier", example = "1.2", description = "Модификатор риска (0.8–1.5).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskModifier")
  public BigDecimal getRiskModifier() {
    return riskModifier;
  }

  public void setRiskModifier(BigDecimal riskModifier) {
    this.riskModifier = riskModifier;
  }

  public BudgetEstimateRequest marketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
    return this;
  }

  /**
   * Текущее значение рыночного индекса.
   * minimum: 0
   * @return marketIndex
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "marketIndex", example = "1.08", description = "Текущее значение рыночного индекса.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("marketIndex")
  public BigDecimal getMarketIndex() {
    return marketIndex;
  }

  public void setMarketIndex(BigDecimal marketIndex) {
    this.marketIndex = marketIndex;
  }

  public BudgetEstimateRequest timeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
    return this;
  }

  /**
   * Модификатор времени (1.0–1.3).
   * minimum: 1.0
   * maximum: 1.3
   * @return timeModifier
   */
  @NotNull @Valid @DecimalMin(value = "1.0") @DecimalMax(value = "1.3") 
  @Schema(name = "timeModifier", example = "1.1", description = "Модификатор времени (1.0–1.3).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeModifier")
  public BigDecimal getTimeModifier() {
    return timeModifier;
  }

  public void setTimeModifier(BigDecimal timeModifier) {
    this.timeModifier = timeModifier;
  }

  public BudgetEstimateRequest districtCode(@Nullable String districtCode) {
    this.districtCode = districtCode;
    return this;
  }

  /**
   * Код района выполнения заказа.
   * @return districtCode
   */
  
  @Schema(name = "districtCode", example = "NC-WAT-01", description = "Код района выполнения заказа.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districtCode")
  public @Nullable String getDistrictCode() {
    return districtCode;
  }

  public void setDistrictCode(@Nullable String districtCode) {
    this.districtCode = districtCode;
  }

  public BudgetEstimateRequest factionCode(@Nullable String factionCode) {
    this.factionCode = factionCode;
    return this;
  }

  /**
   * Корпорация или фракция заказчика.
   * @return factionCode
   */
  
  @Schema(name = "factionCode", example = "ARASAKA", description = "Корпорация или фракция заказчика.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionCode")
  public @Nullable String getFactionCode() {
    return factionCode;
  }

  public void setFactionCode(@Nullable String factionCode) {
    this.factionCode = factionCode;
  }

  public BudgetEstimateRequest playerRank(@Nullable String playerRank) {
    this.playerRank = playerRank;
    return this;
  }

  /**
   * Текущий рейтинг заказчика.
   * @return playerRank
   */
  
  @Schema(name = "playerRank", example = "Silver", description = "Текущий рейтинг заказчика.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerRank")
  public @Nullable String getPlayerRank() {
    return playerRank;
  }

  public void setPlayerRank(@Nullable String playerRank) {
    this.playerRank = playerRank;
  }

  public BudgetEstimateRequest preferredCurrency(String preferredCurrency) {
    this.preferredCurrency = preferredCurrency;
    return this;
  }

  /**
   * Код валюты расчёта.
   * @return preferredCurrency
   */
  @NotNull 
  @Schema(name = "preferredCurrency", example = "NCU", description = "Код валюты расчёта.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("preferredCurrency")
  public String getPreferredCurrency() {
    return preferredCurrency;
  }

  public void setPreferredCurrency(String preferredCurrency) {
    this.preferredCurrency = preferredCurrency;
  }

  public BudgetEstimateRequest isCorporate(Boolean isCorporate) {
    this.isCorporate = isCorporate;
    return this;
  }

  /**
   * Флаг корпоративного заказа.
   * @return isCorporate
   */
  
  @Schema(name = "isCorporate", description = "Флаг корпоративного заказа.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isCorporate")
  public Boolean getIsCorporate() {
    return isCorporate;
  }

  public void setIsCorporate(Boolean isCorporate) {
    this.isCorporate = isCorporate;
  }

  public BudgetEstimateRequest guaranteeTier(@Nullable GuaranteeTierEnum guaranteeTier) {
    this.guaranteeTier = guaranteeTier;
    return this;
  }

  /**
   * Выбранный страховой пакет.
   * @return guaranteeTier
   */
  
  @Schema(name = "guaranteeTier", description = "Выбранный страховой пакет.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteeTier")
  public @Nullable GuaranteeTierEnum getGuaranteeTier() {
    return guaranteeTier;
  }

  public void setGuaranteeTier(@Nullable GuaranteeTierEnum guaranteeTier) {
    this.guaranteeTier = guaranteeTier;
  }

  public BudgetEstimateRequest bonuses(List<String> bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  public BudgetEstimateRequest addBonusesItem(String bonusesItem) {
    if (this.bonuses == null) {
      this.bonuses = new ArrayList<>();
    }
    this.bonuses.add(bonusesItem);
    return this;
  }

  /**
   * Дополнительные бонусные коэффициенты.
   * @return bonuses
   */
  
  @Schema(name = "bonuses", description = "Дополнительные бонусные коэффициенты.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public List<String> getBonuses() {
    return bonuses;
  }

  public void setBonuses(List<String> bonuses) {
    this.bonuses = bonuses;
  }

  public BudgetEstimateRequest penalties(List<String> penalties) {
    this.penalties = penalties;
    return this;
  }

  public BudgetEstimateRequest addPenaltiesItem(String penaltiesItem) {
    if (this.penalties == null) {
      this.penalties = new ArrayList<>();
    }
    this.penalties.add(penaltiesItem);
    return this;
  }

  /**
   * Штрафные коэффициенты.
   * @return penalties
   */
  
  @Schema(name = "penalties", description = "Штрафные коэффициенты.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public List<String> getPenalties() {
    return penalties;
  }

  public void setPenalties(List<String> penalties) {
    this.penalties = penalties;
  }

  public BudgetEstimateRequest manualAdjustment(@Nullable BudgetEstimateRequestManualAdjustment manualAdjustment) {
    this.manualAdjustment = manualAdjustment;
    return this;
  }

  /**
   * Get manualAdjustment
   * @return manualAdjustment
   */
  @Valid 
  @Schema(name = "manualAdjustment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("manualAdjustment")
  public @Nullable BudgetEstimateRequestManualAdjustment getManualAdjustment() {
    return manualAdjustment;
  }

  public void setManualAdjustment(@Nullable BudgetEstimateRequestManualAdjustment manualAdjustment) {
    this.manualAdjustment = manualAdjustment;
  }

  public BudgetEstimateRequest auditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
    return this;
  }

  /**
   * Идентификатор трассы для связи с world-service.
   * @return auditTraceId
   */
  @Valid 
  @Schema(name = "auditTraceId", description = "Идентификатор трассы для связи с world-service.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditTraceId")
  public @Nullable UUID getAuditTraceId() {
    return auditTraceId;
  }

  public void setAuditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetEstimateRequest budgetEstimateRequest = (BudgetEstimateRequest) o;
    return Objects.equals(this.orderId, budgetEstimateRequest.orderId) &&
        Objects.equals(this.templateCode, budgetEstimateRequest.templateCode) &&
        Objects.equals(this.complexityScore, budgetEstimateRequest.complexityScore) &&
        Objects.equals(this.riskModifier, budgetEstimateRequest.riskModifier) &&
        Objects.equals(this.marketIndex, budgetEstimateRequest.marketIndex) &&
        Objects.equals(this.timeModifier, budgetEstimateRequest.timeModifier) &&
        Objects.equals(this.districtCode, budgetEstimateRequest.districtCode) &&
        Objects.equals(this.factionCode, budgetEstimateRequest.factionCode) &&
        Objects.equals(this.playerRank, budgetEstimateRequest.playerRank) &&
        Objects.equals(this.preferredCurrency, budgetEstimateRequest.preferredCurrency) &&
        Objects.equals(this.isCorporate, budgetEstimateRequest.isCorporate) &&
        Objects.equals(this.guaranteeTier, budgetEstimateRequest.guaranteeTier) &&
        Objects.equals(this.bonuses, budgetEstimateRequest.bonuses) &&
        Objects.equals(this.penalties, budgetEstimateRequest.penalties) &&
        Objects.equals(this.manualAdjustment, budgetEstimateRequest.manualAdjustment) &&
        Objects.equals(this.auditTraceId, budgetEstimateRequest.auditTraceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, templateCode, complexityScore, riskModifier, marketIndex, timeModifier, districtCode, factionCode, playerRank, preferredCurrency, isCorporate, guaranteeTier, bonuses, penalties, manualAdjustment, auditTraceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetEstimateRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
    sb.append("    complexityScore: ").append(toIndentedString(complexityScore)).append("\n");
    sb.append("    riskModifier: ").append(toIndentedString(riskModifier)).append("\n");
    sb.append("    marketIndex: ").append(toIndentedString(marketIndex)).append("\n");
    sb.append("    timeModifier: ").append(toIndentedString(timeModifier)).append("\n");
    sb.append("    districtCode: ").append(toIndentedString(districtCode)).append("\n");
    sb.append("    factionCode: ").append(toIndentedString(factionCode)).append("\n");
    sb.append("    playerRank: ").append(toIndentedString(playerRank)).append("\n");
    sb.append("    preferredCurrency: ").append(toIndentedString(preferredCurrency)).append("\n");
    sb.append("    isCorporate: ").append(toIndentedString(isCorporate)).append("\n");
    sb.append("    guaranteeTier: ").append(toIndentedString(guaranteeTier)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    penalties: ").append(toIndentedString(penalties)).append("\n");
    sb.append("    manualAdjustment: ").append(toIndentedString(manualAdjustment)).append("\n");
    sb.append("    auditTraceId: ").append(toIndentedString(auditTraceId)).append("\n");
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

