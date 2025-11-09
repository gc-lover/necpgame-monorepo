package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CyberpsychosisStageHumanityRange;
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
 * Информация о стадии киберпсихоза
 */

@Schema(name = "StageInfo", description = "Информация о стадии киберпсихоза")

public class StageInfo {

  /**
   * Название стадии
   */
  public enum NameEnum {
    EARLY("early"),
    
    MIDDLE("middle"),
    
    LATE("late"),
    
    CYBERPSYCHOSIS("cyberpsychosis");

    private final String value;

    NameEnum(String value) {
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
    public static NameEnum fromValue(String value) {
      for (NameEnum b : NameEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private NameEnum name;

  private CyberpsychosisStageHumanityRange humanityRange;

  @Valid
  private List<Object> symptoms = new ArrayList<>();

  @Valid
  private List<Object> effects = new ArrayList<>();

  @Valid
  private List<Object> consequences = new ArrayList<>();

  public StageInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StageInfo(NameEnum name, CyberpsychosisStageHumanityRange humanityRange, List<Object> symptoms, List<Object> effects, List<Object> consequences) {
    this.name = name;
    this.humanityRange = humanityRange;
    this.symptoms = symptoms;
    this.effects = effects;
    this.consequences = consequences;
  }

  public StageInfo name(NameEnum name) {
    this.name = name;
    return this;
  }

  /**
   * Название стадии
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Название стадии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public NameEnum getName() {
    return name;
  }

  public void setName(NameEnum name) {
    this.name = name;
  }

  public StageInfo humanityRange(CyberpsychosisStageHumanityRange humanityRange) {
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

  public StageInfo symptoms(List<Object> symptoms) {
    this.symptoms = symptoms;
    return this;
  }

  public StageInfo addSymptomsItem(Object symptomsItem) {
    if (this.symptoms == null) {
      this.symptoms = new ArrayList<>();
    }
    this.symptoms.add(symptomsItem);
    return this;
  }

  /**
   * Список возможных симптомов для стадии
   * @return symptoms
   */
  @NotNull 
  @Schema(name = "symptoms", description = "Список возможных симптомов для стадии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("symptoms")
  public List<Object> getSymptoms() {
    return symptoms;
  }

  public void setSymptoms(List<Object> symptoms) {
    this.symptoms = symptoms;
  }

  public StageInfo effects(List<Object> effects) {
    this.effects = effects;
    return this;
  }

  public StageInfo addEffectsItem(Object effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Эффекты стадии
   * @return effects
   */
  @NotNull 
  @Schema(name = "effects", description = "Эффекты стадии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effects")
  public List<Object> getEffects() {
    return effects;
  }

  public void setEffects(List<Object> effects) {
    this.effects = effects;
  }

  public StageInfo consequences(List<Object> consequences) {
    this.consequences = consequences;
    return this;
  }

  public StageInfo addConsequencesItem(Object consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Последствия стадии
   * @return consequences
   */
  @NotNull 
  @Schema(name = "consequences", description = "Последствия стадии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("consequences")
  public List<Object> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<Object> consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StageInfo stageInfo = (StageInfo) o;
    return Objects.equals(this.name, stageInfo.name) &&
        Objects.equals(this.humanityRange, stageInfo.humanityRange) &&
        Objects.equals(this.symptoms, stageInfo.symptoms) &&
        Objects.equals(this.effects, stageInfo.effects) &&
        Objects.equals(this.consequences, stageInfo.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, humanityRange, symptoms, effects, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StageInfo {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    humanityRange: ").append(toIndentedString(humanityRange)).append("\n");
    sb.append("    symptoms: ").append(toIndentedString(symptoms)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

