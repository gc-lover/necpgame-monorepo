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
 * Р РµР·СѓР»СЊС‚Р°С‚ СЃРѕС†РёР°Р»СЊРЅРѕР№ РїРѕРґРґРµСЂР¶РєРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РЎРѕС†РёР°Р»СЊРЅС‹Рµ РјРµС…Р°РЅРёРєРё 
 */

@Schema(name = "SocialSupportResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ СЃРѕС†РёР°Р»СЊРЅРѕР№ РїРѕРґРґРµСЂР¶РєРё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РЎРѕС†РёР°Р»СЊРЅС‹Рµ РјРµС…Р°РЅРёРєРё ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class SocialSupportResult {

  private Float stressReduction;

  private Float progressionModifier;

  private Float duration;

  public SocialSupportResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialSupportResult(Float stressReduction, Float progressionModifier, Float duration) {
    this.stressReduction = stressReduction;
    this.progressionModifier = progressionModifier;
    this.duration = duration;
  }

  public SocialSupportResult stressReduction(Float stressReduction) {
    this.stressReduction = stressReduction;
    return this;
  }

  /**
   * РЎРЅРёР¶РµРЅРёРµ СЃС‚СЂРµСЃСЃР°
   * minimum: 0
   * @return stressReduction
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "stress_reduction", description = "РЎРЅРёР¶РµРЅРёРµ СЃС‚СЂРµСЃСЃР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stress_reduction")
  public Float getStressReduction() {
    return stressReduction;
  }

  public void setStressReduction(Float stressReduction) {
    this.stressReduction = stressReduction;
  }

  public SocialSupportResult progressionModifier(Float progressionModifier) {
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

  public SocialSupportResult duration(Float duration) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialSupportResult socialSupportResult = (SocialSupportResult) o;
    return Objects.equals(this.stressReduction, socialSupportResult.stressReduction) &&
        Objects.equals(this.progressionModifier, socialSupportResult.progressionModifier) &&
        Objects.equals(this.duration, socialSupportResult.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stressReduction, progressionModifier, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialSupportResult {\n");
    sb.append("    stressReduction: ").append(toIndentedString(stressReduction)).append("\n");
    sb.append("    progressionModifier: ").append(toIndentedString(progressionModifier)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

