package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ChronicleEventRef;
import com.necpgame.worldservice.model.SettlementProductionInner;
import com.necpgame.worldservice.model.SettlementUpgradeRequirementsInner;
import java.math.BigDecimal;
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
 * Settlement
 */


public class Settlement {

  private UUID settlementId;

  private String name;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    CAMP("camp"),
    
    OUTPOST("outpost"),
    
    STRONGHOLD("stronghold"),
    
    CITY("city");

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

  private StatusEnum status;

  private UUID ownerFactionId;

  private Integer population;

  @Valid
  private List<@Valid SettlementProductionInner> production = new ArrayList<>();

  private Integer defenseRating;

  private BigDecimal logisticsPressure;

  @Valid
  private List<@Valid SettlementUpgradeRequirementsInner> upgradeRequirements = new ArrayList<>();

  @Valid
  private List<@Valid ChronicleEventRef> activeEvents = new ArrayList<>();

  private @Nullable BigDecimal unrestScore;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  public Settlement() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Settlement(UUID settlementId, String name, StatusEnum status, UUID ownerFactionId, Integer population, List<@Valid SettlementProductionInner> production, Integer defenseRating, BigDecimal logisticsPressure) {
    this.settlementId = settlementId;
    this.name = name;
    this.status = status;
    this.ownerFactionId = ownerFactionId;
    this.population = population;
    this.production = production;
    this.defenseRating = defenseRating;
    this.logisticsPressure = logisticsPressure;
  }

  public Settlement settlementId(UUID settlementId) {
    this.settlementId = settlementId;
    return this;
  }

  /**
   * Get settlementId
   * @return settlementId
   */
  @NotNull @Valid 
  @Schema(name = "settlementId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("settlementId")
  public UUID getSettlementId() {
    return settlementId;
  }

  public void setSettlementId(UUID settlementId) {
    this.settlementId = settlementId;
  }

  public Settlement name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Settlement status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Settlement ownerFactionId(UUID ownerFactionId) {
    this.ownerFactionId = ownerFactionId;
    return this;
  }

  /**
   * Get ownerFactionId
   * @return ownerFactionId
   */
  @NotNull @Valid 
  @Schema(name = "ownerFactionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerFactionId")
  public UUID getOwnerFactionId() {
    return ownerFactionId;
  }

  public void setOwnerFactionId(UUID ownerFactionId) {
    this.ownerFactionId = ownerFactionId;
  }

  public Settlement population(Integer population) {
    this.population = population;
    return this;
  }

  /**
   * Get population
   * minimum: 0
   * @return population
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "population", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("population")
  public Integer getPopulation() {
    return population;
  }

  public void setPopulation(Integer population) {
    this.population = population;
  }

  public Settlement production(List<@Valid SettlementProductionInner> production) {
    this.production = production;
    return this;
  }

  public Settlement addProductionItem(SettlementProductionInner productionItem) {
    if (this.production == null) {
      this.production = new ArrayList<>();
    }
    this.production.add(productionItem);
    return this;
  }

  /**
   * Get production
   * @return production
   */
  @NotNull @Valid 
  @Schema(name = "production", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("production")
  public List<@Valid SettlementProductionInner> getProduction() {
    return production;
  }

  public void setProduction(List<@Valid SettlementProductionInner> production) {
    this.production = production;
  }

  public Settlement defenseRating(Integer defenseRating) {
    this.defenseRating = defenseRating;
    return this;
  }

  /**
   * Get defenseRating
   * minimum: 0
   * maximum: 1000
   * @return defenseRating
   */
  @NotNull @Min(value = 0) @Max(value = 1000) 
  @Schema(name = "defenseRating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("defenseRating")
  public Integer getDefenseRating() {
    return defenseRating;
  }

  public void setDefenseRating(Integer defenseRating) {
    this.defenseRating = defenseRating;
  }

  public Settlement logisticsPressure(BigDecimal logisticsPressure) {
    this.logisticsPressure = logisticsPressure;
    return this;
  }

  /**
   * Get logisticsPressure
   * minimum: 0
   * @return logisticsPressure
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "logisticsPressure", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("logisticsPressure")
  public BigDecimal getLogisticsPressure() {
    return logisticsPressure;
  }

  public void setLogisticsPressure(BigDecimal logisticsPressure) {
    this.logisticsPressure = logisticsPressure;
  }

  public Settlement upgradeRequirements(List<@Valid SettlementUpgradeRequirementsInner> upgradeRequirements) {
    this.upgradeRequirements = upgradeRequirements;
    return this;
  }

  public Settlement addUpgradeRequirementsItem(SettlementUpgradeRequirementsInner upgradeRequirementsItem) {
    if (this.upgradeRequirements == null) {
      this.upgradeRequirements = new ArrayList<>();
    }
    this.upgradeRequirements.add(upgradeRequirementsItem);
    return this;
  }

  /**
   * Get upgradeRequirements
   * @return upgradeRequirements
   */
  @Valid 
  @Schema(name = "upgradeRequirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgradeRequirements")
  public List<@Valid SettlementUpgradeRequirementsInner> getUpgradeRequirements() {
    return upgradeRequirements;
  }

  public void setUpgradeRequirements(List<@Valid SettlementUpgradeRequirementsInner> upgradeRequirements) {
    this.upgradeRequirements = upgradeRequirements;
  }

  public Settlement activeEvents(List<@Valid ChronicleEventRef> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public Settlement addActiveEventsItem(ChronicleEventRef activeEventsItem) {
    if (this.activeEvents == null) {
      this.activeEvents = new ArrayList<>();
    }
    this.activeEvents.add(activeEventsItem);
    return this;
  }

  /**
   * Get activeEvents
   * @return activeEvents
   */
  @Valid 
  @Schema(name = "activeEvents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeEvents")
  public List<@Valid ChronicleEventRef> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<@Valid ChronicleEventRef> activeEvents) {
    this.activeEvents = activeEvents;
  }

  public Settlement unrestScore(@Nullable BigDecimal unrestScore) {
    this.unrestScore = unrestScore;
    return this;
  }

  /**
   * Get unrestScore
   * minimum: 0
   * maximum: 100
   * @return unrestScore
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "unrestScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unrestScore")
  public @Nullable BigDecimal getUnrestScore() {
    return unrestScore;
  }

  public void setUnrestScore(@Nullable BigDecimal unrestScore) {
    this.unrestScore = unrestScore;
  }

  public Settlement lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "lastUpdated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdated")
  public @Nullable OffsetDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Settlement settlement = (Settlement) o;
    return Objects.equals(this.settlementId, settlement.settlementId) &&
        Objects.equals(this.name, settlement.name) &&
        Objects.equals(this.status, settlement.status) &&
        Objects.equals(this.ownerFactionId, settlement.ownerFactionId) &&
        Objects.equals(this.population, settlement.population) &&
        Objects.equals(this.production, settlement.production) &&
        Objects.equals(this.defenseRating, settlement.defenseRating) &&
        Objects.equals(this.logisticsPressure, settlement.logisticsPressure) &&
        Objects.equals(this.upgradeRequirements, settlement.upgradeRequirements) &&
        Objects.equals(this.activeEvents, settlement.activeEvents) &&
        Objects.equals(this.unrestScore, settlement.unrestScore) &&
        Objects.equals(this.lastUpdated, settlement.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(settlementId, name, status, ownerFactionId, population, production, defenseRating, logisticsPressure, upgradeRequirements, activeEvents, unrestScore, lastUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Settlement {\n");
    sb.append("    settlementId: ").append(toIndentedString(settlementId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    ownerFactionId: ").append(toIndentedString(ownerFactionId)).append("\n");
    sb.append("    population: ").append(toIndentedString(population)).append("\n");
    sb.append("    production: ").append(toIndentedString(production)).append("\n");
    sb.append("    defenseRating: ").append(toIndentedString(defenseRating)).append("\n");
    sb.append("    logisticsPressure: ").append(toIndentedString(logisticsPressure)).append("\n");
    sb.append("    upgradeRequirements: ").append(toIndentedString(upgradeRequirements)).append("\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
    sb.append("    unrestScore: ").append(toIndentedString(unrestScore)).append("\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
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

