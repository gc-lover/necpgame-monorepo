package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Р РµР·СѓР»СЊС‚Р°С‚ РїСЂРѕС„РёР»Р°РєС‚РёРєРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РџСЂРѕС„РёР»Р°РєС‚РёРєР° 
 */

@Schema(name = "PreventionResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ РїСЂРѕС„РёР»Р°РєС‚РёРєРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РџСЂРѕС„РёР»Р°РєС‚РёРєР° ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class PreventionResult {

  private Float effectiveness;

  private Float duration;

  private Float cost;

  private Float progressionModifier;

  public PreventionResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PreventionResult(Float effectiveness, Float duration, Float cost, Float progressionModifier) {
    this.effectiveness = effectiveness;
    this.duration = duration;
    this.cost = cost;
    this.progressionModifier = progressionModifier;
  }

  public PreventionResult effectiveness(Float effectiveness) {
    this.effectiveness = effectiveness;
    return this;
  }

  /**
   * Р­С„С„РµРєС‚РёРІРЅРѕСЃС‚СЊ РїСЂРѕС„РёР»Р°РєС‚РёРєРё (0-100%)
   * minimum: 0
   * maximum: 100
   * @return effectiveness
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "effectiveness", description = "Р­С„С„РµРєС‚РёРІРЅРѕСЃС‚СЊ РїСЂРѕС„РёР»Р°РєС‚РёРєРё (0-100%)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effectiveness")
  public Float getEffectiveness() {
    return effectiveness;
  }

  public void setEffectiveness(Float effectiveness) {
    this.effectiveness = effectiveness;
  }

  public PreventionResult duration(Float duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ СЌС„С„РµРєС‚Р° РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return duration
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "duration", description = "Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ СЌС„С„РµРєС‚Р° РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration")
  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public PreventionResult cost(Float cost) {
    this.cost = cost;
    return this;
  }

  /**
   * РЎС‚РѕРёРјРѕСЃС‚СЊ РїСЂРѕС„РёР»Р°РєС‚РёРєРё
   * minimum: 0
   * @return cost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cost", description = "РЎС‚РѕРёРјРѕСЃС‚СЊ РїСЂРѕС„РёР»Р°РєС‚РёРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }

  public PreventionResult progressionModifier(Float progressionModifier) {
    this.progressionModifier = progressionModifier;
    return this;
  }

  /**
   * РњРѕРґРёС„РёРєР°С‚РѕСЂ РїСЂРѕРіСЂРµСЃСЃРёРё (РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕРµ Р·РЅР°С‡РµРЅРёРµ = Р·Р°РјРµРґР»РµРЅРёРµ)
   * maximum: 0
   * @return progressionModifier
   */
  @NotNull @DecimalMax(value = "0") 
  @Schema(name = "progression_modifier", description = "РњРѕРґРёС„РёРєР°С‚РѕСЂ РїСЂРѕРіСЂРµСЃСЃРёРё (РѕС‚СЂРёС†Р°С‚РµР»СЊРЅРѕРµ Р·РЅР°С‡РµРЅРёРµ = Р·Р°РјРµРґР»РµРЅРёРµ)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("progression_modifier")
  public Float getProgressionModifier() {
    return progressionModifier;
  }

  public void setProgressionModifier(Float progressionModifier) {
    this.progressionModifier = progressionModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreventionResult preventionResult = (PreventionResult) o;
    return Objects.equals(this.effectiveness, preventionResult.effectiveness) &&
        Objects.equals(this.duration, preventionResult.duration) &&
        Objects.equals(this.cost, preventionResult.cost) &&
        Objects.equals(this.progressionModifier, preventionResult.progressionModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectiveness, duration, cost, progressionModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreventionResult {\n");
    sb.append("    effectiveness: ").append(toIndentedString(effectiveness)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    progressionModifier: ").append(toIndentedString(progressionModifier)).append("\n");
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

