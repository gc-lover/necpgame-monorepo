package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.HashMap;
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
 * Р РµР·СѓР»СЊС‚Р°С‚ СѓРґР°Р»РµРЅРёСЏ РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РЈРґР°Р»РµРЅРёРµ РёРјРїР»Р°РЅС‚РѕРІ 
 */

@Schema(name = "ImplantRemovalResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ СѓРґР°Р»РµРЅРёСЏ РёРјРїР»Р°РЅС‚Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РЈРґР°Р»РµРЅРёРµ РёРјРїР»Р°РЅС‚РѕРІ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ImplantRemovalResult {

  private Float humanityRestored;

  private Float cost;

  @Valid
  private JsonNullable<Map<String, Object>> effects = JsonNullable.<Map<String, Object>>undefined();

  public ImplantRemovalResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImplantRemovalResult(Float humanityRestored, Float cost) {
    this.humanityRestored = humanityRestored;
    this.cost = cost;
  }

  public ImplantRemovalResult humanityRestored(Float humanityRestored) {
    this.humanityRestored = humanityRestored;
    return this;
  }

  /**
   * Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРЅР°СЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ
   * minimum: 0
   * @return humanityRestored
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "humanity_restored", description = "Р’РѕСЃСЃС‚Р°РЅРѕРІР»РµРЅРЅР°СЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("humanity_restored")
  public Float getHumanityRestored() {
    return humanityRestored;
  }

  public void setHumanityRestored(Float humanityRestored) {
    this.humanityRestored = humanityRestored;
  }

  public ImplantRemovalResult cost(Float cost) {
    this.cost = cost;
    return this;
  }

  /**
   * РЎС‚РѕРёРјРѕСЃС‚СЊ СѓРґР°Р»РµРЅРёСЏ РёРјРїР»Р°РЅС‚Р°
   * minimum: 0
   * @return cost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cost", description = "РЎС‚РѕРёРјРѕСЃС‚СЊ СѓРґР°Р»РµРЅРёСЏ РёРјРїР»Р°РЅС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }

  public ImplantRemovalResult effects(Map<String, Object> effects) {
    this.effects = JsonNullable.of(effects);
    return this;
  }

  public ImplantRemovalResult putEffectsItem(String key, Object effectsItem) {
    if (this.effects == null || !this.effects.isPresent()) {
      this.effects = JsonNullable.of(new HashMap<>());
    }
    this.effects.get().put(key, effectsItem);
    return this;
  }

  /**
   * Р­С„С„РµРєС‚С‹ СѓРґР°Р»РµРЅРёСЏ (РїРѕС‚РµСЂСЏ С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРє РёРјРїР»Р°РЅС‚Р°)
   * @return effects
   */
  
  @Schema(name = "effects", description = "Р­С„С„РµРєС‚С‹ СѓРґР°Р»РµРЅРёСЏ (РїРѕС‚РµСЂСЏ С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРє РёРјРїР»Р°РЅС‚Р°)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public JsonNullable<Map<String, Object>> getEffects() {
    return effects;
  }

  public void setEffects(JsonNullable<Map<String, Object>> effects) {
    this.effects = effects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantRemovalResult implantRemovalResult = (ImplantRemovalResult) o;
    return Objects.equals(this.humanityRestored, implantRemovalResult.humanityRestored) &&
        Objects.equals(this.cost, implantRemovalResult.cost) &&
        equalsNullable(this.effects, implantRemovalResult.effects);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(humanityRestored, cost, hashCodeNullable(effects));
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
    sb.append("class ImplantRemovalResult {\n");
    sb.append("    humanityRestored: ").append(toIndentedString(humanityRestored)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
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

