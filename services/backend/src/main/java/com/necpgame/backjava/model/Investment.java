package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Investment
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class Investment {

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

  public Investment investmentId(@Nullable UUID investmentId) {
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

  public Investment characterId(@Nullable UUID characterId) {
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

  public Investment opportunityId(@Nullable String opportunityId) {
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

  public Investment type(@Nullable String type) {
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

  public Investment amountInvested(@Nullable Integer amountInvested) {
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

  public Investment currentValue(@Nullable Integer currentValue) {
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

  public Investment status(@Nullable StatusEnum status) {
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

  public Investment createdAt(@Nullable OffsetDateTime createdAt) {
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

  public Investment maturityDate(@Nullable OffsetDateTime maturityDate) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Investment investment = (Investment) o;
    return Objects.equals(this.investmentId, investment.investmentId) &&
        Objects.equals(this.characterId, investment.characterId) &&
        Objects.equals(this.opportunityId, investment.opportunityId) &&
        Objects.equals(this.type, investment.type) &&
        Objects.equals(this.amountInvested, investment.amountInvested) &&
        Objects.equals(this.currentValue, investment.currentValue) &&
        Objects.equals(this.status, investment.status) &&
        Objects.equals(this.createdAt, investment.createdAt) &&
        Objects.equals(this.maturityDate, investment.maturityDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(investmentId, characterId, opportunityId, type, amountInvested, currentValue, status, createdAt, maturityDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Investment {\n");
    sb.append("    investmentId: ").append(toIndentedString(investmentId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    opportunityId: ").append(toIndentedString(opportunityId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    amountInvested: ").append(toIndentedString(amountInvested)).append("\n");
    sb.append("    currentValue: ").append(toIndentedString(currentValue)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    maturityDate: ").append(toIndentedString(maturityDate)).append("\n");
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

