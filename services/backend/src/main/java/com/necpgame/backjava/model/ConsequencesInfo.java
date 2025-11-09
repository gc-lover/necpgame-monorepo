package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.SocialEffects;
import com.necpgame.backjava.model.StatPenalties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * РџРѕСЃР»РµРґСЃС‚РІРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РџРѕСЃР»РµРґСЃС‚РІРёСЏ 
 */

@Schema(name = "ConsequencesInfo", description = "РџРѕСЃР»РµРґСЃС‚РІРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р°. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РџРѕСЃР»РµРґСЃС‚РІРёСЏ ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ConsequencesInfo {

  private StatPenalties statPenalties;

  private SocialEffects socialEffects;

  @Valid
  private Map<String, Object> controlEffects = new HashMap<>();

  @Valid
  private List<String> visualIndicators = new ArrayList<>();

  public ConsequencesInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ConsequencesInfo(StatPenalties statPenalties, SocialEffects socialEffects, Map<String, Object> controlEffects) {
    this.statPenalties = statPenalties;
    this.socialEffects = socialEffects;
    this.controlEffects = controlEffects;
  }

  public ConsequencesInfo statPenalties(StatPenalties statPenalties) {
    this.statPenalties = statPenalties;
    return this;
  }

  /**
   * Get statPenalties
   * @return statPenalties
   */
  @NotNull @Valid 
  @Schema(name = "stat_penalties", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stat_penalties")
  public StatPenalties getStatPenalties() {
    return statPenalties;
  }

  public void setStatPenalties(StatPenalties statPenalties) {
    this.statPenalties = statPenalties;
  }

  public ConsequencesInfo socialEffects(SocialEffects socialEffects) {
    this.socialEffects = socialEffects;
    return this;
  }

  /**
   * Get socialEffects
   * @return socialEffects
   */
  @NotNull @Valid 
  @Schema(name = "social_effects", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("social_effects")
  public SocialEffects getSocialEffects() {
    return socialEffects;
  }

  public void setSocialEffects(SocialEffects socialEffects) {
    this.socialEffects = socialEffects;
  }

  public ConsequencesInfo controlEffects(Map<String, Object> controlEffects) {
    this.controlEffects = controlEffects;
    return this;
  }

  public ConsequencesInfo putControlEffectsItem(String key, Object controlEffectsItem) {
    if (this.controlEffects == null) {
      this.controlEffects = new HashMap<>();
    }
    this.controlEffects.put(key, controlEffectsItem);
    return this;
  }

  /**
   * Р­С„С„РµРєС‚С‹ РЅР° РєРѕРЅС‚СЂРѕР»СЊ РїРµСЂСЃРѕРЅР°Р¶Р°
   * @return controlEffects
   */
  @NotNull 
  @Schema(name = "control_effects", description = "Р­С„С„РµРєС‚С‹ РЅР° РєРѕРЅС‚СЂРѕР»СЊ РїРµСЂСЃРѕРЅР°Р¶Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("control_effects")
  public Map<String, Object> getControlEffects() {
    return controlEffects;
  }

  public void setControlEffects(Map<String, Object> controlEffects) {
    this.controlEffects = controlEffects;
  }

  public ConsequencesInfo visualIndicators(List<String> visualIndicators) {
    this.visualIndicators = visualIndicators;
    return this;
  }

  public ConsequencesInfo addVisualIndicatorsItem(String visualIndicatorsItem) {
    if (this.visualIndicators == null) {
      this.visualIndicators = new ArrayList<>();
    }
    this.visualIndicators.add(visualIndicatorsItem);
    return this;
  }

  /**
   * Р’РёР·СѓР°Р»СЊРЅС‹Рµ РёРЅРґРёРєР°С‚РѕСЂС‹ СЃС‚Р°РґРёРё
   * @return visualIndicators
   */
  
  @Schema(name = "visual_indicators", description = "Р’РёР·СѓР°Р»СЊРЅС‹Рµ РёРЅРґРёРєР°С‚РѕСЂС‹ СЃС‚Р°РґРёРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visual_indicators")
  public List<String> getVisualIndicators() {
    return visualIndicators;
  }

  public void setVisualIndicators(List<String> visualIndicators) {
    this.visualIndicators = visualIndicators;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConsequencesInfo consequencesInfo = (ConsequencesInfo) o;
    return Objects.equals(this.statPenalties, consequencesInfo.statPenalties) &&
        Objects.equals(this.socialEffects, consequencesInfo.socialEffects) &&
        Objects.equals(this.controlEffects, consequencesInfo.controlEffects) &&
        Objects.equals(this.visualIndicators, consequencesInfo.visualIndicators);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statPenalties, socialEffects, controlEffects, visualIndicators);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConsequencesInfo {\n");
    sb.append("    statPenalties: ").append(toIndentedString(statPenalties)).append("\n");
    sb.append("    socialEffects: ").append(toIndentedString(socialEffects)).append("\n");
    sb.append("    controlEffects: ").append(toIndentedString(controlEffects)).append("\n");
    sb.append("    visualIndicators: ").append(toIndentedString(visualIndicators)).append("\n");
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

