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
 * InsuranceTier
 */


public class InsuranceTier {

  /**
   * Gets or Sets id
   */
  public enum IdEnum {
    BASIC("basic"),
    
    EXTENDED("extended"),
    
    PREMIUM("premium");

    private final String value;

    IdEnum(String value) {
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
    public static IdEnum fromValue(String value) {
      for (IdEnum b : IdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private IdEnum id;

  private String name;

  private @Nullable String description;

  private Float coverage;

  private Float commissionRate;

  private Float escrowMultiplier;

  private @Nullable Float bonusReputation;

  @Valid
  private List<String> conditions = new ArrayList<>();

  public InsuranceTier() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InsuranceTier(IdEnum id, String name, Float coverage, Float commissionRate, Float escrowMultiplier) {
    this.id = id;
    this.name = name;
    this.coverage = coverage;
    this.commissionRate = commissionRate;
    this.escrowMultiplier = escrowMultiplier;
  }

  public InsuranceTier id(IdEnum id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public IdEnum getId() {
    return id;
  }

  public void setId(IdEnum id) {
    this.id = id;
  }

  public InsuranceTier name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Size(min = 3, max = 64) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public InsuranceTier description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Size(min = 3, max = 256) 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public InsuranceTier coverage(Float coverage) {
    this.coverage = coverage;
    return this;
  }

  /**
   * Get coverage
   * minimum: 0
   * @return coverage
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "coverage", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("coverage")
  public Float getCoverage() {
    return coverage;
  }

  public void setCoverage(Float coverage) {
    this.coverage = coverage;
  }

  public InsuranceTier commissionRate(Float commissionRate) {
    this.commissionRate = commissionRate;
    return this;
  }

  /**
   * Get commissionRate
   * minimum: 0.05
   * maximum: 0.12
   * @return commissionRate
   */
  @NotNull @DecimalMin(value = "0.05") @DecimalMax(value = "0.12") 
  @Schema(name = "commissionRate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("commissionRate")
  public Float getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(Float commissionRate) {
    this.commissionRate = commissionRate;
  }

  public InsuranceTier escrowMultiplier(Float escrowMultiplier) {
    this.escrowMultiplier = escrowMultiplier;
    return this;
  }

  /**
   * Get escrowMultiplier
   * minimum: 0.1
   * maximum: 0.3
   * @return escrowMultiplier
   */
  @NotNull @DecimalMin(value = "0.1") @DecimalMax(value = "0.3") 
  @Schema(name = "escrowMultiplier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("escrowMultiplier")
  public Float getEscrowMultiplier() {
    return escrowMultiplier;
  }

  public void setEscrowMultiplier(Float escrowMultiplier) {
    this.escrowMultiplier = escrowMultiplier;
  }

  public InsuranceTier bonusReputation(@Nullable Float bonusReputation) {
    this.bonusReputation = bonusReputation;
    return this;
  }

  /**
   * Get bonusReputation
   * @return bonusReputation
   */
  
  @Schema(name = "bonusReputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonusReputation")
  public @Nullable Float getBonusReputation() {
    return bonusReputation;
  }

  public void setBonusReputation(@Nullable Float bonusReputation) {
    this.bonusReputation = bonusReputation;
  }

  public InsuranceTier conditions(List<String> conditions) {
    this.conditions = conditions;
    return this;
  }

  public InsuranceTier addConditionsItem(String conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

  /**
   * Get conditions
   * @return conditions
   */
  
  @Schema(name = "conditions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conditions")
  public List<String> getConditions() {
    return conditions;
  }

  public void setConditions(List<String> conditions) {
    this.conditions = conditions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsuranceTier insuranceTier = (InsuranceTier) o;
    return Objects.equals(this.id, insuranceTier.id) &&
        Objects.equals(this.name, insuranceTier.name) &&
        Objects.equals(this.description, insuranceTier.description) &&
        Objects.equals(this.coverage, insuranceTier.coverage) &&
        Objects.equals(this.commissionRate, insuranceTier.commissionRate) &&
        Objects.equals(this.escrowMultiplier, insuranceTier.escrowMultiplier) &&
        Objects.equals(this.bonusReputation, insuranceTier.bonusReputation) &&
        Objects.equals(this.conditions, insuranceTier.conditions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, coverage, commissionRate, escrowMultiplier, bonusReputation, conditions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsuranceTier {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    coverage: ").append(toIndentedString(coverage)).append("\n");
    sb.append("    commissionRate: ").append(toIndentedString(commissionRate)).append("\n");
    sb.append("    escrowMultiplier: ").append(toIndentedString(escrowMultiplier)).append("\n");
    sb.append("    bonusReputation: ").append(toIndentedString(bonusReputation)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
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

