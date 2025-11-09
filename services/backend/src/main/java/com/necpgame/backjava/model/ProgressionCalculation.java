package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Р Р°СЃС‡РµС‚ РїСЂРѕРіСЂРµСЃСЃРёРё РєРёР±РµСЂРїСЃРёС…РѕР·Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РџСЂРѕРіСЂРµСЃСЃРёСЏ 
 */

@Schema(name = "ProgressionCalculation", description = "Р Р°СЃС‡РµС‚ РїСЂРѕРіСЂРµСЃСЃРёРё РєРёР±РµСЂРїСЃРёС…РѕР·Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РџСЂРѕРіСЂРµСЃСЃРёСЏ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ProgressionCalculation {

  private Float baseRate;

  @Valid
  private List<Object> factors = new ArrayList<>();

  private Float totalRate;

  private Float predictedLoss;

  public ProgressionCalculation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProgressionCalculation(Float baseRate, Float totalRate, Float predictedLoss) {
    this.baseRate = baseRate;
    this.totalRate = totalRate;
    this.predictedLoss = predictedLoss;
  }

  public ProgressionCalculation baseRate(Float baseRate) {
    this.baseRate = baseRate;
    return this;
  }

  /**
   * Р‘Р°Р·РѕРІР°СЏ СЃРєРѕСЂРѕСЃС‚СЊ РїСЂРѕРіСЂРµСЃСЃРёРё
   * @return baseRate
   */
  @NotNull 
  @Schema(name = "base_rate", description = "Р‘Р°Р·РѕРІР°СЏ СЃРєРѕСЂРѕСЃС‚СЊ РїСЂРѕРіСЂРµСЃСЃРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("base_rate")
  public Float getBaseRate() {
    return baseRate;
  }

  public void setBaseRate(Float baseRate) {
    this.baseRate = baseRate;
  }

  public ProgressionCalculation factors(List<Object> factors) {
    this.factors = factors;
    return this;
  }

  public ProgressionCalculation addFactorsItem(Object factorsItem) {
    if (this.factors == null) {
      this.factors = new ArrayList<>();
    }
    this.factors.add(factorsItem);
    return this;
  }

  /**
   * Р¤Р°РєС‚РѕСЂС‹ РїСЂРѕРіСЂРµСЃСЃРёРё СЃ РёС… РІР»РёСЏРЅРёРµРј
   * @return factors
   */
  
  @Schema(name = "factors", description = "Р¤Р°РєС‚РѕСЂС‹ РїСЂРѕРіСЂРµСЃСЃРёРё СЃ РёС… РІР»РёСЏРЅРёРµРј", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factors")
  public List<Object> getFactors() {
    return factors;
  }

  public void setFactors(List<Object> factors) {
    this.factors = factors;
  }

  public ProgressionCalculation totalRate(Float totalRate) {
    this.totalRate = totalRate;
    return this;
  }

  /**
   * РС‚РѕРіРѕРІР°СЏ СЃРєРѕСЂРѕСЃС‚СЊ РїСЂРѕРіСЂРµСЃСЃРёРё
   * @return totalRate
   */
  @NotNull 
  @Schema(name = "total_rate", description = "РС‚РѕРіРѕРІР°СЏ СЃРєРѕСЂРѕСЃС‚СЊ РїСЂРѕРіСЂРµСЃСЃРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_rate")
  public Float getTotalRate() {
    return totalRate;
  }

  public void setTotalRate(Float totalRate) {
    this.totalRate = totalRate;
  }

  public ProgressionCalculation predictedLoss(Float predictedLoss) {
    this.predictedLoss = predictedLoss;
    return this;
  }

  /**
   * РџСЂРµРґСЃРєР°Р·Р°РЅРЅР°СЏ РїРѕС‚РµСЂСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё Р·Р° РїРµСЂРёРѕРґ
   * @return predictedLoss
   */
  @NotNull 
  @Schema(name = "predicted_loss", description = "РџСЂРµРґСЃРєР°Р·Р°РЅРЅР°СЏ РїРѕС‚РµСЂСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё Р·Р° РїРµСЂРёРѕРґ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("predicted_loss")
  public Float getPredictedLoss() {
    return predictedLoss;
  }

  public void setPredictedLoss(Float predictedLoss) {
    this.predictedLoss = predictedLoss;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressionCalculation progressionCalculation = (ProgressionCalculation) o;
    return Objects.equals(this.baseRate, progressionCalculation.baseRate) &&
        Objects.equals(this.factors, progressionCalculation.factors) &&
        Objects.equals(this.totalRate, progressionCalculation.totalRate) &&
        Objects.equals(this.predictedLoss, progressionCalculation.predictedLoss);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseRate, factors, totalRate, predictedLoss);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProgressionCalculation {\n");
    sb.append("    baseRate: ").append(toIndentedString(baseRate)).append("\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    totalRate: ").append(toIndentedString(totalRate)).append("\n");
    sb.append("    predictedLoss: ").append(toIndentedString(predictedLoss)).append("\n");
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

