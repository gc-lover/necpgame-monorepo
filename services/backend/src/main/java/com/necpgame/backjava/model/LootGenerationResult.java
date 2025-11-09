package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.GuaranteedReward;
import com.necpgame.backjava.model.LootCurrency;
import com.necpgame.backjava.model.LootItem;
import com.necpgame.backjava.model.LootRoll;
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
 * LootGenerationResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootGenerationResult {

  private UUID resultId;

  /**
   * Gets or Sets distributionMode
   */
  public enum DistributionModeEnum {
    PERSONAL("PERSONAL"),
    
    SHARED("SHARED"),
    
    RAID("RAID"),
    
    GUARANTEED("GUARANTEED");

    private final String value;

    DistributionModeEnum(String value) {
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
    public static DistributionModeEnum fromValue(String value) {
      for (DistributionModeEnum b : DistributionModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DistributionModeEnum distributionMode;

  @Valid
  private List<@Valid LootItem> items = new ArrayList<>();

  @Valid
  private List<@Valid LootCurrency> currency = new ArrayList<>();

  @Valid
  private List<@Valid LootRoll> rollSessions = new ArrayList<>();

  @Valid
  private List<@Valid GuaranteedReward> guarantees = new ArrayList<>();

  private @Nullable UUID auditTrailId;

  public LootGenerationResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootGenerationResult(UUID resultId, DistributionModeEnum distributionMode, List<@Valid LootItem> items) {
    this.resultId = resultId;
    this.distributionMode = distributionMode;
    this.items = items;
  }

  public LootGenerationResult resultId(UUID resultId) {
    this.resultId = resultId;
    return this;
  }

  /**
   * Get resultId
   * @return resultId
   */
  @NotNull @Valid 
  @Schema(name = "resultId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resultId")
  public UUID getResultId() {
    return resultId;
  }

  public void setResultId(UUID resultId) {
    this.resultId = resultId;
  }

  public LootGenerationResult distributionMode(DistributionModeEnum distributionMode) {
    this.distributionMode = distributionMode;
    return this;
  }

  /**
   * Get distributionMode
   * @return distributionMode
   */
  @NotNull 
  @Schema(name = "distributionMode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("distributionMode")
  public DistributionModeEnum getDistributionMode() {
    return distributionMode;
  }

  public void setDistributionMode(DistributionModeEnum distributionMode) {
    this.distributionMode = distributionMode;
  }

  public LootGenerationResult items(List<@Valid LootItem> items) {
    this.items = items;
    return this;
  }

  public LootGenerationResult addItemsItem(LootItem itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid LootItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid LootItem> items) {
    this.items = items;
  }

  public LootGenerationResult currency(List<@Valid LootCurrency> currency) {
    this.currency = currency;
    return this;
  }

  public LootGenerationResult addCurrencyItem(LootCurrency currencyItem) {
    if (this.currency == null) {
      this.currency = new ArrayList<>();
    }
    this.currency.add(currencyItem);
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Valid 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public List<@Valid LootCurrency> getCurrency() {
    return currency;
  }

  public void setCurrency(List<@Valid LootCurrency> currency) {
    this.currency = currency;
  }

  public LootGenerationResult rollSessions(List<@Valid LootRoll> rollSessions) {
    this.rollSessions = rollSessions;
    return this;
  }

  public LootGenerationResult addRollSessionsItem(LootRoll rollSessionsItem) {
    if (this.rollSessions == null) {
      this.rollSessions = new ArrayList<>();
    }
    this.rollSessions.add(rollSessionsItem);
    return this;
  }

  /**
   * Get rollSessions
   * @return rollSessions
   */
  @Valid 
  @Schema(name = "rollSessions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollSessions")
  public List<@Valid LootRoll> getRollSessions() {
    return rollSessions;
  }

  public void setRollSessions(List<@Valid LootRoll> rollSessions) {
    this.rollSessions = rollSessions;
  }

  public LootGenerationResult guarantees(List<@Valid GuaranteedReward> guarantees) {
    this.guarantees = guarantees;
    return this;
  }

  public LootGenerationResult addGuaranteesItem(GuaranteedReward guaranteesItem) {
    if (this.guarantees == null) {
      this.guarantees = new ArrayList<>();
    }
    this.guarantees.add(guaranteesItem);
    return this;
  }

  /**
   * Get guarantees
   * @return guarantees
   */
  @Valid 
  @Schema(name = "guarantees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guarantees")
  public List<@Valid GuaranteedReward> getGuarantees() {
    return guarantees;
  }

  public void setGuarantees(List<@Valid GuaranteedReward> guarantees) {
    this.guarantees = guarantees;
  }

  public LootGenerationResult auditTrailId(@Nullable UUID auditTrailId) {
    this.auditTrailId = auditTrailId;
    return this;
  }

  /**
   * Get auditTrailId
   * @return auditTrailId
   */
  @Valid 
  @Schema(name = "auditTrailId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditTrailId")
  public @Nullable UUID getAuditTrailId() {
    return auditTrailId;
  }

  public void setAuditTrailId(@Nullable UUID auditTrailId) {
    this.auditTrailId = auditTrailId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootGenerationResult lootGenerationResult = (LootGenerationResult) o;
    return Objects.equals(this.resultId, lootGenerationResult.resultId) &&
        Objects.equals(this.distributionMode, lootGenerationResult.distributionMode) &&
        Objects.equals(this.items, lootGenerationResult.items) &&
        Objects.equals(this.currency, lootGenerationResult.currency) &&
        Objects.equals(this.rollSessions, lootGenerationResult.rollSessions) &&
        Objects.equals(this.guarantees, lootGenerationResult.guarantees) &&
        Objects.equals(this.auditTrailId, lootGenerationResult.auditTrailId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultId, distributionMode, items, currency, rollSessions, guarantees, auditTrailId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootGenerationResult {\n");
    sb.append("    resultId: ").append(toIndentedString(resultId)).append("\n");
    sb.append("    distributionMode: ").append(toIndentedString(distributionMode)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    rollSessions: ").append(toIndentedString(rollSessions)).append("\n");
    sb.append("    guarantees: ").append(toIndentedString(guarantees)).append("\n");
    sb.append("    auditTrailId: ").append(toIndentedString(auditTrailId)).append("\n");
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

