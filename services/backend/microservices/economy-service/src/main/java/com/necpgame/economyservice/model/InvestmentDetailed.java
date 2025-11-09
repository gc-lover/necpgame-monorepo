package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.InvestmentDetailedAllOfDividendHistory;
import com.necpgame.economyservice.model.InvestmentOpportunity;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InvestmentDetailed
 */


public class InvestmentDetailed {

  private @Nullable UUID investmentId;

  private @Nullable UUID characterId;

  private @Nullable String opportunityId;

  private @Nullable String type;

  private @Nullable Integer amountInvested;

  private @Nullable Integer currentValue;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    MATURED("MATURED"),
    
    WITHDRAWN("WITHDRAWN"),
    
    FAILED("FAILED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime maturityDate;

  private @Nullable InvestmentOpportunity opportunityDetails;

  private @Nullable Integer profitLoss;

  private @Nullable Float roiCurrent;

  private @Nullable Integer dividendsReceived;

  @Valid
  private List<@Valid InvestmentDetailedAllOfDividendHistory> dividendHistory = new ArrayList<>();

  public InvestmentDetailed investmentId(@Nullable UUID investmentId) {
    this.investmentId = investmentId;
    return this;
  }

  /**
   * Get investmentId
   * @return investmentId
   */
  @Valid 
  @Schema(name = "investment_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("investment_id")
  public @Nullable UUID getInvestmentId() {
    return investmentId;
  }

  public void setInvestmentId(@Nullable UUID investmentId) {
    this.investmentId = investmentId;
  }

  public InvestmentDetailed characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public InvestmentDetailed opportunityId(@Nullable String opportunityId) {
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

  public InvestmentDetailed type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public InvestmentDetailed amountInvested(@Nullable Integer amountInvested) {
    this.amountInvested = amountInvested;
    return this;
  }

  /**
   * Get amountInvested
   * @return amountInvested
   */
  
  @Schema(name = "amount_invested", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount_invested")
  public @Nullable Integer getAmountInvested() {
    return amountInvested;
  }

  public void setAmountInvested(@Nullable Integer amountInvested) {
    this.amountInvested = amountInvested;
  }

  public InvestmentDetailed currentValue(@Nullable Integer currentValue) {
    this.currentValue = currentValue;
    return this;
  }

  /**
   * Get currentValue
   * @return currentValue
   */
  
  @Schema(name = "current_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_value")
  public @Nullable Integer getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(@Nullable Integer currentValue) {
    this.currentValue = currentValue;
  }

  public InvestmentDetailed status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public InvestmentDetailed createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public InvestmentDetailed maturityDate(@Nullable OffsetDateTime maturityDate) {
    this.maturityDate = maturityDate;
    return this;
  }

  /**
   * Get maturityDate
   * @return maturityDate
   */
  @Valid 
  @Schema(name = "maturity_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maturity_date")
  public @Nullable OffsetDateTime getMaturityDate() {
    return maturityDate;
  }

  public void setMaturityDate(@Nullable OffsetDateTime maturityDate) {
    this.maturityDate = maturityDate;
  }

  public InvestmentDetailed opportunityDetails(@Nullable InvestmentOpportunity opportunityDetails) {
    this.opportunityDetails = opportunityDetails;
    return this;
  }

  /**
   * Get opportunityDetails
   * @return opportunityDetails
   */
  @Valid 
  @Schema(name = "opportunity_details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opportunity_details")
  public @Nullable InvestmentOpportunity getOpportunityDetails() {
    return opportunityDetails;
  }

  public void setOpportunityDetails(@Nullable InvestmentOpportunity opportunityDetails) {
    this.opportunityDetails = opportunityDetails;
  }

  public InvestmentDetailed profitLoss(@Nullable Integer profitLoss) {
    this.profitLoss = profitLoss;
    return this;
  }

  /**
   * Текущая прибыль/убыток
   * @return profitLoss
   */
  
  @Schema(name = "profit_loss", description = "Текущая прибыль/убыток", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_loss")
  public @Nullable Integer getProfitLoss() {
    return profitLoss;
  }

  public void setProfitLoss(@Nullable Integer profitLoss) {
    this.profitLoss = profitLoss;
  }

  public InvestmentDetailed roiCurrent(@Nullable Float roiCurrent) {
    this.roiCurrent = roiCurrent;
    return this;
  }

  /**
   * Get roiCurrent
   * @return roiCurrent
   */
  
  @Schema(name = "roi_current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi_current")
  public @Nullable Float getRoiCurrent() {
    return roiCurrent;
  }

  public void setRoiCurrent(@Nullable Float roiCurrent) {
    this.roiCurrent = roiCurrent;
  }

  public InvestmentDetailed dividendsReceived(@Nullable Integer dividendsReceived) {
    this.dividendsReceived = dividendsReceived;
    return this;
  }

  /**
   * Get dividendsReceived
   * @return dividendsReceived
   */
  
  @Schema(name = "dividends_received", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dividends_received")
  public @Nullable Integer getDividendsReceived() {
    return dividendsReceived;
  }

  public void setDividendsReceived(@Nullable Integer dividendsReceived) {
    this.dividendsReceived = dividendsReceived;
  }

  public InvestmentDetailed dividendHistory(List<@Valid InvestmentDetailedAllOfDividendHistory> dividendHistory) {
    this.dividendHistory = dividendHistory;
    return this;
  }

  public InvestmentDetailed addDividendHistoryItem(InvestmentDetailedAllOfDividendHistory dividendHistoryItem) {
    if (this.dividendHistory == null) {
      this.dividendHistory = new ArrayList<>();
    }
    this.dividendHistory.add(dividendHistoryItem);
    return this;
  }

  /**
   * Get dividendHistory
   * @return dividendHistory
   */
  @Valid 
  @Schema(name = "dividend_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dividend_history")
  public List<@Valid InvestmentDetailedAllOfDividendHistory> getDividendHistory() {
    return dividendHistory;
  }

  public void setDividendHistory(List<@Valid InvestmentDetailedAllOfDividendHistory> dividendHistory) {
    this.dividendHistory = dividendHistory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvestmentDetailed investmentDetailed = (InvestmentDetailed) o;
    return Objects.equals(this.investmentId, investmentDetailed.investmentId) &&
        Objects.equals(this.characterId, investmentDetailed.characterId) &&
        Objects.equals(this.opportunityId, investmentDetailed.opportunityId) &&
        Objects.equals(this.type, investmentDetailed.type) &&
        Objects.equals(this.amountInvested, investmentDetailed.amountInvested) &&
        Objects.equals(this.currentValue, investmentDetailed.currentValue) &&
        Objects.equals(this.status, investmentDetailed.status) &&
        Objects.equals(this.createdAt, investmentDetailed.createdAt) &&
        Objects.equals(this.maturityDate, investmentDetailed.maturityDate) &&
        Objects.equals(this.opportunityDetails, investmentDetailed.opportunityDetails) &&
        Objects.equals(this.profitLoss, investmentDetailed.profitLoss) &&
        Objects.equals(this.roiCurrent, investmentDetailed.roiCurrent) &&
        Objects.equals(this.dividendsReceived, investmentDetailed.dividendsReceived) &&
        Objects.equals(this.dividendHistory, investmentDetailed.dividendHistory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(investmentId, characterId, opportunityId, type, amountInvested, currentValue, status, createdAt, maturityDate, opportunityDetails, profitLoss, roiCurrent, dividendsReceived, dividendHistory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvestmentDetailed {\n");
    sb.append("    investmentId: ").append(toIndentedString(investmentId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    opportunityId: ").append(toIndentedString(opportunityId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    amountInvested: ").append(toIndentedString(amountInvested)).append("\n");
    sb.append("    currentValue: ").append(toIndentedString(currentValue)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    maturityDate: ").append(toIndentedString(maturityDate)).append("\n");
    sb.append("    opportunityDetails: ").append(toIndentedString(opportunityDetails)).append("\n");
    sb.append("    profitLoss: ").append(toIndentedString(profitLoss)).append("\n");
    sb.append("    roiCurrent: ").append(toIndentedString(roiCurrent)).append("\n");
    sb.append("    dividendsReceived: ").append(toIndentedString(dividendsReceived)).append("\n");
    sb.append("    dividendHistory: ").append(toIndentedString(dividendHistory)).append("\n");
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

