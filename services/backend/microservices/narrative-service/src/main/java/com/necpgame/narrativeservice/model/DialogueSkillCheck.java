package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.OptionConsequence;
import com.necpgame.narrativeservice.model.SkillModifier;
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
 * DialogueSkillCheck
 */


public class DialogueSkillCheck {

  /**
   * Gets or Sets stat
   */
  public enum StatEnum {
    PERCEPTION("Perception"),
    
    PARKOUR("Parkour"),
    
    COMMUNICATION("Communication"),
    
    STEALTH("Stealth"),
    
    TECH("Tech");

    private final String value;

    StatEnum(String value) {
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
    public static StatEnum fromValue(String value) {
      for (StatEnum b : StatEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatEnum stat;

  private Integer dc;

  private Boolean advantage = false;

  @Valid
  private List<@Valid SkillModifier> modifiers = new ArrayList<>();

  @Valid
  private List<@Valid OptionConsequence> failureConsequences = new ArrayList<>();

  public DialogueSkillCheck() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogueSkillCheck(StatEnum stat, Integer dc) {
    this.stat = stat;
    this.dc = dc;
  }

  public DialogueSkillCheck stat(StatEnum stat) {
    this.stat = stat;
    return this;
  }

  /**
   * Get stat
   * @return stat
   */
  @NotNull 
  @Schema(name = "stat", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stat")
  public StatEnum getStat() {
    return stat;
  }

  public void setStat(StatEnum stat) {
    this.stat = stat;
  }

  public DialogueSkillCheck dc(Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  @NotNull 
  @Schema(name = "dc", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dc")
  public Integer getDc() {
    return dc;
  }

  public void setDc(Integer dc) {
    this.dc = dc;
  }

  public DialogueSkillCheck advantage(Boolean advantage) {
    this.advantage = advantage;
    return this;
  }

  /**
   * Get advantage
   * @return advantage
   */
  
  @Schema(name = "advantage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("advantage")
  public Boolean getAdvantage() {
    return advantage;
  }

  public void setAdvantage(Boolean advantage) {
    this.advantage = advantage;
  }

  public DialogueSkillCheck modifiers(List<@Valid SkillModifier> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public DialogueSkillCheck addModifiersItem(SkillModifier modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new ArrayList<>();
    }
    this.modifiers.add(modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public List<@Valid SkillModifier> getModifiers() {
    return modifiers;
  }

  public void setModifiers(List<@Valid SkillModifier> modifiers) {
    this.modifiers = modifiers;
  }

  public DialogueSkillCheck failureConsequences(List<@Valid OptionConsequence> failureConsequences) {
    this.failureConsequences = failureConsequences;
    return this;
  }

  public DialogueSkillCheck addFailureConsequencesItem(OptionConsequence failureConsequencesItem) {
    if (this.failureConsequences == null) {
      this.failureConsequences = new ArrayList<>();
    }
    this.failureConsequences.add(failureConsequencesItem);
    return this;
  }

  /**
   * Get failureConsequences
   * @return failureConsequences
   */
  @Valid 
  @Schema(name = "failureConsequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failureConsequences")
  public List<@Valid OptionConsequence> getFailureConsequences() {
    return failureConsequences;
  }

  public void setFailureConsequences(List<@Valid OptionConsequence> failureConsequences) {
    this.failureConsequences = failureConsequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueSkillCheck dialogueSkillCheck = (DialogueSkillCheck) o;
    return Objects.equals(this.stat, dialogueSkillCheck.stat) &&
        Objects.equals(this.dc, dialogueSkillCheck.dc) &&
        Objects.equals(this.advantage, dialogueSkillCheck.advantage) &&
        Objects.equals(this.modifiers, dialogueSkillCheck.modifiers) &&
        Objects.equals(this.failureConsequences, dialogueSkillCheck.failureConsequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stat, dc, advantage, modifiers, failureConsequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueSkillCheck {\n");
    sb.append("    stat: ").append(toIndentedString(stat)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    advantage: ").append(toIndentedString(advantage)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
    sb.append("    failureConsequences: ").append(toIndentedString(failureConsequences)).append("\n");
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

