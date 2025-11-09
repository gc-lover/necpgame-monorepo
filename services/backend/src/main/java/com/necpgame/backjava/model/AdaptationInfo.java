package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * РРЅС„РѕСЂРјР°С†РёСЏ РѕР± Р°РґР°РїС‚Р°С†РёРё Рє СЃРёРјРїС‚РѕРјР°Рј. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РђРґР°РїС‚Р°С†РёСЏ 
 */

@Schema(name = "AdaptationInfo", description = "РРЅС„РѕСЂРјР°С†РёСЏ РѕР± Р°РґР°РїС‚Р°С†РёРё Рє СЃРёРјРїС‚РѕРјР°Рј. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РђРґР°РїС‚Р°С†РёСЏ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class AdaptationInfo {

  @Valid
  private List<UUID> adaptedSymptoms = new ArrayList<>();

  private Float adaptationLevel;

  private Float effectsReduction;

  public AdaptationInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AdaptationInfo(List<UUID> adaptedSymptoms, Float adaptationLevel, Float effectsReduction) {
    this.adaptedSymptoms = adaptedSymptoms;
    this.adaptationLevel = adaptationLevel;
    this.effectsReduction = effectsReduction;
  }

  public AdaptationInfo adaptedSymptoms(List<UUID> adaptedSymptoms) {
    this.adaptedSymptoms = adaptedSymptoms;
    return this;
  }

  public AdaptationInfo addAdaptedSymptomsItem(UUID adaptedSymptomsItem) {
    if (this.adaptedSymptoms == null) {
      this.adaptedSymptoms = new ArrayList<>();
    }
    this.adaptedSymptoms.add(adaptedSymptomsItem);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂС‹ Р°РґР°РїС‚РёСЂРѕРІР°РЅРЅС‹С… СЃРёРјРїС‚РѕРјРѕРІ
   * @return adaptedSymptoms
   */
  @NotNull @Valid 
  @Schema(name = "adapted_symptoms", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂС‹ Р°РґР°РїС‚РёСЂРѕРІР°РЅРЅС‹С… СЃРёРјРїС‚РѕРјРѕРІ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("adapted_symptoms")
  public List<UUID> getAdaptedSymptoms() {
    return adaptedSymptoms;
  }

  public void setAdaptedSymptoms(List<UUID> adaptedSymptoms) {
    this.adaptedSymptoms = adaptedSymptoms;
  }

  public AdaptationInfo adaptationLevel(Float adaptationLevel) {
    this.adaptationLevel = adaptationLevel;
    return this;
  }

  /**
   * РЈСЂРѕРІРµРЅСЊ Р°РґР°РїС‚Р°С†РёРё (0-100%)
   * minimum: 0
   * maximum: 100
   * @return adaptationLevel
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "adaptation_level", description = "РЈСЂРѕРІРµРЅСЊ Р°РґР°РїС‚Р°С†РёРё (0-100%)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("adaptation_level")
  public Float getAdaptationLevel() {
    return adaptationLevel;
  }

  public void setAdaptationLevel(Float adaptationLevel) {
    this.adaptationLevel = adaptationLevel;
  }

  public AdaptationInfo effectsReduction(Float effectsReduction) {
    this.effectsReduction = effectsReduction;
    return this;
  }

  /**
   * РЎРЅРёР¶РµРЅРёРµ РІР»РёСЏРЅРёСЏ СЃРёРјРїС‚РѕРјРѕРІ (0-100%)
   * minimum: 0
   * maximum: 100
   * @return effectsReduction
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "effects_reduction", description = "РЎРЅРёР¶РµРЅРёРµ РІР»РёСЏРЅРёСЏ СЃРёРјРїС‚РѕРјРѕРІ (0-100%)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effects_reduction")
  public Float getEffectsReduction() {
    return effectsReduction;
  }

  public void setEffectsReduction(Float effectsReduction) {
    this.effectsReduction = effectsReduction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdaptationInfo adaptationInfo = (AdaptationInfo) o;
    return Objects.equals(this.adaptedSymptoms, adaptationInfo.adaptedSymptoms) &&
        Objects.equals(this.adaptationLevel, adaptationInfo.adaptationLevel) &&
        Objects.equals(this.effectsReduction, adaptationInfo.effectsReduction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adaptedSymptoms, adaptationLevel, effectsReduction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdaptationInfo {\n");
    sb.append("    adaptedSymptoms: ").append(toIndentedString(adaptedSymptoms)).append("\n");
    sb.append("    adaptationLevel: ").append(toIndentedString(adaptationLevel)).append("\n");
    sb.append("    effectsReduction: ").append(toIndentedString(effectsReduction)).append("\n");
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

