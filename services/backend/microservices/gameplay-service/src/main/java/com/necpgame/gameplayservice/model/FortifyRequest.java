package com.necpgame.gameplayservice.model;

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
 * FortifyRequest
 */


public class FortifyRequest {

  private Integer investmentAmount;

  private @Nullable Integer upgradeLevel;

  /**
   * Gets or Sets buffType
   */
  public enum BuffTypeEnum {
    TURRET_DAMAGE("turret_damage"),
    
    SHIELD_STRENGTH("shield_strength"),
    
    RESPAWN_SPEED("respawn_speed");

    private final String value;

    BuffTypeEnum(String value) {
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
    public static BuffTypeEnum fromValue(String value) {
      for (BuffTypeEnum b : BuffTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable BuffTypeEnum buffType;

  public FortifyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FortifyRequest(Integer investmentAmount) {
    this.investmentAmount = investmentAmount;
  }

  public FortifyRequest investmentAmount(Integer investmentAmount) {
    this.investmentAmount = investmentAmount;
    return this;
  }

  /**
   * Get investmentAmount
   * minimum: 1000
   * @return investmentAmount
   */
  @NotNull @Min(value = 1000) 
  @Schema(name = "investmentAmount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("investmentAmount")
  public Integer getInvestmentAmount() {
    return investmentAmount;
  }

  public void setInvestmentAmount(Integer investmentAmount) {
    this.investmentAmount = investmentAmount;
  }

  public FortifyRequest upgradeLevel(@Nullable Integer upgradeLevel) {
    this.upgradeLevel = upgradeLevel;
    return this;
  }

  /**
   * Get upgradeLevel
   * minimum: 1
   * maximum: 5
   * @return upgradeLevel
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "upgradeLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgradeLevel")
  public @Nullable Integer getUpgradeLevel() {
    return upgradeLevel;
  }

  public void setUpgradeLevel(@Nullable Integer upgradeLevel) {
    this.upgradeLevel = upgradeLevel;
  }

  public FortifyRequest buffType(@Nullable BuffTypeEnum buffType) {
    this.buffType = buffType;
    return this;
  }

  /**
   * Get buffType
   * @return buffType
   */
  
  @Schema(name = "buffType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buffType")
  public @Nullable BuffTypeEnum getBuffType() {
    return buffType;
  }

  public void setBuffType(@Nullable BuffTypeEnum buffType) {
    this.buffType = buffType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FortifyRequest fortifyRequest = (FortifyRequest) o;
    return Objects.equals(this.investmentAmount, fortifyRequest.investmentAmount) &&
        Objects.equals(this.upgradeLevel, fortifyRequest.upgradeLevel) &&
        Objects.equals(this.buffType, fortifyRequest.buffType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(investmentAmount, upgradeLevel, buffType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FortifyRequest {\n");
    sb.append("    investmentAmount: ").append(toIndentedString(investmentAmount)).append("\n");
    sb.append("    upgradeLevel: ").append(toIndentedString(upgradeLevel)).append("\n");
    sb.append("    buffType: ").append(toIndentedString(buffType)).append("\n");
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

