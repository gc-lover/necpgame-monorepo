package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * РЎС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; Р­РєРѕРЅРѕРјРёРєР° СѓРїСЂР°РІР»РµРЅРёСЏ 
 */

@Schema(name = "TreatmentCosts", description = "РЎС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> Р­РєРѕРЅРѕРјРёРєР° СѓРїСЂР°РІР»РµРЅРёСЏ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class TreatmentCosts {

  private Float baseCost;

  @Valid
  private JsonNullable<Map<String, BigDecimal>> modifiers = JsonNullable.<Map<String, BigDecimal>>undefined();

  private Float totalCost;

  private String currency;

  @Valid
  private List<Object> discounts = new ArrayList<>();

  public TreatmentCosts() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TreatmentCosts(Float baseCost, Float totalCost, String currency) {
    this.baseCost = baseCost;
    this.totalCost = totalCost;
    this.currency = currency;
  }

  public TreatmentCosts baseCost(Float baseCost) {
    this.baseCost = baseCost;
    return this;
  }

  /**
   * Р‘Р°Р·РѕРІР°СЏ СЃС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ
   * minimum: 0
   * @return baseCost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "base_cost", description = "Р‘Р°Р·РѕРІР°СЏ СЃС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("base_cost")
  public Float getBaseCost() {
    return baseCost;
  }

  public void setBaseCost(Float baseCost) {
    this.baseCost = baseCost;
  }

  public TreatmentCosts modifiers(Map<String, BigDecimal> modifiers) {
    this.modifiers = JsonNullable.of(modifiers);
    return this;
  }

  public TreatmentCosts putModifiersItem(String key, BigDecimal modifiersItem) {
    if (this.modifiers == null || !this.modifiers.isPresent()) {
      this.modifiers = JsonNullable.of(new HashMap<>());
    }
    this.modifiers.get().put(key, modifiersItem);
    return this;
  }

  /**
   * РњРѕРґРёС„РёРєР°С‚РѕСЂС‹ СЃС‚РѕРёРјРѕСЃС‚Рё
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", description = "РњРѕРґРёС„РёРєР°С‚РѕСЂС‹ СЃС‚РѕРёРјРѕСЃС‚Рё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public JsonNullable<Map<String, BigDecimal>> getModifiers() {
    return modifiers;
  }

  public void setModifiers(JsonNullable<Map<String, BigDecimal>> modifiers) {
    this.modifiers = modifiers;
  }

  public TreatmentCosts totalCost(Float totalCost) {
    this.totalCost = totalCost;
    return this;
  }

  /**
   * РС‚РѕРіРѕРІР°СЏ СЃС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ
   * minimum: 0
   * @return totalCost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "total_cost", description = "РС‚РѕРіРѕРІР°СЏ СЃС‚РѕРёРјРѕСЃС‚СЊ Р»РµС‡РµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total_cost")
  public Float getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(Float totalCost) {
    this.totalCost = totalCost;
  }

  public TreatmentCosts currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Р’Р°Р»СЋС‚Р°
   * @return currency
   */
  @NotNull 
  @Schema(name = "currency", description = "Р’Р°Р»СЋС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currency")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public TreatmentCosts discounts(List<Object> discounts) {
    this.discounts = discounts;
    return this;
  }

  public TreatmentCosts addDiscountsItem(Object discountsItem) {
    if (this.discounts == null) {
      this.discounts = new ArrayList<>();
    }
    this.discounts.add(discountsItem);
    return this;
  }

  /**
   * РџСЂРёРјРµРЅРµРЅРЅС‹Рµ СЃРєРёРґРєРё
   * @return discounts
   */
  
  @Schema(name = "discounts", description = "РџСЂРёРјРµРЅРµРЅРЅС‹Рµ СЃРєРёРґРєРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("discounts")
  public List<Object> getDiscounts() {
    return discounts;
  }

  public void setDiscounts(List<Object> discounts) {
    this.discounts = discounts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TreatmentCosts treatmentCosts = (TreatmentCosts) o;
    return Objects.equals(this.baseCost, treatmentCosts.baseCost) &&
        equalsNullable(this.modifiers, treatmentCosts.modifiers) &&
        Objects.equals(this.totalCost, treatmentCosts.totalCost) &&
        Objects.equals(this.currency, treatmentCosts.currency) &&
        Objects.equals(this.discounts, treatmentCosts.discounts);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseCost, hashCodeNullable(modifiers), totalCost, currency, discounts);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TreatmentCosts {\n");
    sb.append("    baseCost: ").append(toIndentedString(baseCost)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
    sb.append("    totalCost: ").append(toIndentedString(totalCost)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    discounts: ").append(toIndentedString(discounts)).append("\n");
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

