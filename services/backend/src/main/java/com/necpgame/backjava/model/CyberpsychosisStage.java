package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.CyberpsychosisStageHumanityRange;
import com.necpgame.backjava.model.Symptom;
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
 * РЎС‚Р°РґРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° РёРіСЂРѕРєР°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РЎРёРјРїС‚РѕРјС‹ Рё РїСЂРѕРіСЂРµСЃСЃРёСЏ 
 */

@Schema(name = "CyberpsychosisStage", description = "РЎС‚Р°РґРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° РёРіСЂРѕРєР°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РЎРёРјРїС‚РѕРјС‹ Рё РїСЂРѕРіСЂРµСЃСЃРёСЏ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CyberpsychosisStage {

  /**
   * РќР°Р·РІР°РЅРёРµ СЃС‚Р°РґРёРё
   */
  public enum StageEnum {
    EARLY("early"),
    
    MIDDLE("middle"),
    
    LATE("late"),
    
    CYBERPSYCHOSIS("cyberpsychosis");

    private final String value;

    StageEnum(String value) {
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
    public static StageEnum fromValue(String value) {
      for (StageEnum b : StageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StageEnum stage;

  private CyberpsychosisStageHumanityRange humanityRange;

  @Valid
  private List<@Valid Symptom> symptoms = new ArrayList<>();

  @Valid
  private List<Object> effects = new ArrayList<>();

  public CyberpsychosisStage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CyberpsychosisStage(StageEnum stage, CyberpsychosisStageHumanityRange humanityRange, List<@Valid Symptom> symptoms, List<Object> effects) {
    this.stage = stage;
    this.humanityRange = humanityRange;
    this.symptoms = symptoms;
    this.effects = effects;
  }

  public CyberpsychosisStage stage(StageEnum stage) {
    this.stage = stage;
    return this;
  }

  /**
   * РќР°Р·РІР°РЅРёРµ СЃС‚Р°РґРёРё
   * @return stage
   */
  @NotNull 
  @Schema(name = "stage", description = "РќР°Р·РІР°РЅРёРµ СЃС‚Р°РґРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stage")
  public StageEnum getStage() {
    return stage;
  }

  public void setStage(StageEnum stage) {
    this.stage = stage;
  }

  public CyberpsychosisStage humanityRange(CyberpsychosisStageHumanityRange humanityRange) {
    this.humanityRange = humanityRange;
    return this;
  }

  /**
   * Get humanityRange
   * @return humanityRange
   */
  @NotNull @Valid 
  @Schema(name = "humanity_range", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("humanity_range")
  public CyberpsychosisStageHumanityRange getHumanityRange() {
    return humanityRange;
  }

  public void setHumanityRange(CyberpsychosisStageHumanityRange humanityRange) {
    this.humanityRange = humanityRange;
  }

  public CyberpsychosisStage symptoms(List<@Valid Symptom> symptoms) {
    this.symptoms = symptoms;
    return this;
  }

  public CyberpsychosisStage addSymptomsItem(Symptom symptomsItem) {
    if (this.symptoms == null) {
      this.symptoms = new ArrayList<>();
    }
    this.symptoms.add(symptomsItem);
    return this;
  }

  /**
   * РђРєС‚РёРІРЅС‹Рµ СЃРёРјРїС‚РѕРјС‹ СЃС‚Р°РґРёРё
   * @return symptoms
   */
  @NotNull @Valid 
  @Schema(name = "symptoms", description = "РђРєС‚РёРІРЅС‹Рµ СЃРёРјРїС‚РѕРјС‹ СЃС‚Р°РґРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("symptoms")
  public List<@Valid Symptom> getSymptoms() {
    return symptoms;
  }

  public void setSymptoms(List<@Valid Symptom> symptoms) {
    this.symptoms = symptoms;
  }

  public CyberpsychosisStage effects(List<Object> effects) {
    this.effects = effects;
    return this;
  }

  public CyberpsychosisStage addEffectsItem(Object effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Р­С„С„РµРєС‚С‹ СЃС‚Р°РґРёРё (С€С‚СЂР°С„С‹, РІРёР·СѓР°Р»СЊРЅС‹Рµ РёРЅРґРёРєР°С‚РѕСЂС‹)
   * @return effects
   */
  @NotNull 
  @Schema(name = "effects", description = "Р­С„С„РµРєС‚С‹ СЃС‚Р°РґРёРё (С€С‚СЂР°С„С‹, РІРёР·СѓР°Р»СЊРЅС‹Рµ РёРЅРґРёРєР°С‚РѕСЂС‹)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effects")
  public List<Object> getEffects() {
    return effects;
  }

  public void setEffects(List<Object> effects) {
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
    CyberpsychosisStage cyberpsychosisStage = (CyberpsychosisStage) o;
    return Objects.equals(this.stage, cyberpsychosisStage.stage) &&
        Objects.equals(this.humanityRange, cyberpsychosisStage.humanityRange) &&
        Objects.equals(this.symptoms, cyberpsychosisStage.symptoms) &&
        Objects.equals(this.effects, cyberpsychosisStage.effects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stage, humanityRange, symptoms, effects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberpsychosisStage {\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    humanityRange: ").append(toIndentedString(humanityRange)).append("\n");
    sb.append("    symptoms: ").append(toIndentedString(symptoms)).append("\n");
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

