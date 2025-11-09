package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.HumanityUpdateResultStageTransition;
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
 * Р РµР·СѓР»СЊС‚Р°С‚ РѕР±РЅРѕРІР»РµРЅРёСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -&gt; РЎРёСЃС‚РµРјР° С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё 
 */

@Schema(name = "HumanityUpdateResult", description = "Р РµР·СѓР»СЊС‚Р°С‚ РѕР±РЅРѕРІР»РµРЅРёСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё. РСЃС‚РѕС‡РЅРёРє: .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md -> РЎРёСЃС‚РµРјР° С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class HumanityUpdateResult {

  private Float newLevel;

  private JsonNullable<HumanityUpdateResultStageTransition> stageTransition = JsonNullable.<HumanityUpdateResultStageTransition>undefined();

  @Valid
  private List<UUID> symptomsTriggered = new ArrayList<>();

  public HumanityUpdateResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HumanityUpdateResult(Float newLevel, HumanityUpdateResultStageTransition stageTransition) {
    this.newLevel = newLevel;
    this.stageTransition = JsonNullable.of(stageTransition);
  }

  public HumanityUpdateResult newLevel(Float newLevel) {
    this.newLevel = newLevel;
    return this;
  }

  /**
   * РќРѕРІС‹Р№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё
   * minimum: 0
   * maximum: 100
   * @return newLevel
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "new_level", description = "РќРѕРІС‹Р№ СѓСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("new_level")
  public Float getNewLevel() {
    return newLevel;
  }

  public void setNewLevel(Float newLevel) {
    this.newLevel = newLevel;
  }

  public HumanityUpdateResult stageTransition(HumanityUpdateResultStageTransition stageTransition) {
    this.stageTransition = JsonNullable.of(stageTransition);
    return this;
  }

  /**
   * Get stageTransition
   * @return stageTransition
   */
  @NotNull @Valid 
  @Schema(name = "stage_transition", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stage_transition")
  public JsonNullable<HumanityUpdateResultStageTransition> getStageTransition() {
    return stageTransition;
  }

  public void setStageTransition(JsonNullable<HumanityUpdateResultStageTransition> stageTransition) {
    this.stageTransition = stageTransition;
  }

  public HumanityUpdateResult symptomsTriggered(List<UUID> symptomsTriggered) {
    this.symptomsTriggered = symptomsTriggered;
    return this;
  }

  public HumanityUpdateResult addSymptomsTriggeredItem(UUID symptomsTriggeredItem) {
    if (this.symptomsTriggered == null) {
      this.symptomsTriggered = new ArrayList<>();
    }
    this.symptomsTriggered.add(symptomsTriggeredItem);
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂС‹ СЃСЂР°Р±РѕС‚Р°РІС€РёС… СЃРёРјРїС‚РѕРјРѕРІ
   * @return symptomsTriggered
   */
  @Valid 
  @Schema(name = "symptoms_triggered", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂС‹ СЃСЂР°Р±РѕС‚Р°РІС€РёС… СЃРёРјРїС‚РѕРјРѕРІ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symptoms_triggered")
  public List<UUID> getSymptomsTriggered() {
    return symptomsTriggered;
  }

  public void setSymptomsTriggered(List<UUID> symptomsTriggered) {
    this.symptomsTriggered = symptomsTriggered;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HumanityUpdateResult humanityUpdateResult = (HumanityUpdateResult) o;
    return Objects.equals(this.newLevel, humanityUpdateResult.newLevel) &&
        Objects.equals(this.stageTransition, humanityUpdateResult.stageTransition) &&
        Objects.equals(this.symptomsTriggered, humanityUpdateResult.symptomsTriggered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newLevel, stageTransition, symptomsTriggered);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HumanityUpdateResult {\n");
    sb.append("    newLevel: ").append(toIndentedString(newLevel)).append("\n");
    sb.append("    stageTransition: ").append(toIndentedString(stageTransition)).append("\n");
    sb.append("    symptomsTriggered: ").append(toIndentedString(symptomsTriggered)).append("\n");
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

