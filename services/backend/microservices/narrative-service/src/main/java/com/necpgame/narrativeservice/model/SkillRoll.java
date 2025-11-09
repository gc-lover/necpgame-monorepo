package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * SkillRoll
 */


public class SkillRoll {

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

  private Integer roll;

  @Valid
  private List<@Valid SkillModifier> modifiersApplied = new ArrayList<>();

  /**
   * Gets or Sets outcome
   */
  public enum OutcomeEnum {
    SUCCESS("success"),
    
    FAILURE("failure"),
    
    CRITICAL_SUCCESS("critical_success"),
    
    CRITICAL_FAILURE("critical_failure");

    private final String value;

    OutcomeEnum(String value) {
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
    public static OutcomeEnum fromValue(String value) {
      for (OutcomeEnum b : OutcomeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OutcomeEnum outcome;

  public SkillRoll() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SkillRoll(StatEnum stat, Integer roll) {
    this.stat = stat;
    this.roll = roll;
  }

  public SkillRoll stat(StatEnum stat) {
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

  public SkillRoll roll(Integer roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Get roll
   * @return roll
   */
  @NotNull 
  @Schema(name = "roll", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roll")
  public Integer getRoll() {
    return roll;
  }

  public void setRoll(Integer roll) {
    this.roll = roll;
  }

  public SkillRoll modifiersApplied(List<@Valid SkillModifier> modifiersApplied) {
    this.modifiersApplied = modifiersApplied;
    return this;
  }

  public SkillRoll addModifiersAppliedItem(SkillModifier modifiersAppliedItem) {
    if (this.modifiersApplied == null) {
      this.modifiersApplied = new ArrayList<>();
    }
    this.modifiersApplied.add(modifiersAppliedItem);
    return this;
  }

  /**
   * Get modifiersApplied
   * @return modifiersApplied
   */
  @Valid 
  @Schema(name = "modifiersApplied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiersApplied")
  public List<@Valid SkillModifier> getModifiersApplied() {
    return modifiersApplied;
  }

  public void setModifiersApplied(List<@Valid SkillModifier> modifiersApplied) {
    this.modifiersApplied = modifiersApplied;
  }

  public SkillRoll outcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
    return this;
  }

  /**
   * Get outcome
   * @return outcome
   */
  
  @Schema(name = "outcome", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outcome")
  public @Nullable OutcomeEnum getOutcome() {
    return outcome;
  }

  public void setOutcome(@Nullable OutcomeEnum outcome) {
    this.outcome = outcome;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillRoll skillRoll = (SkillRoll) o;
    return Objects.equals(this.stat, skillRoll.stat) &&
        Objects.equals(this.roll, skillRoll.roll) &&
        Objects.equals(this.modifiersApplied, skillRoll.modifiersApplied) &&
        Objects.equals(this.outcome, skillRoll.outcome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stat, roll, modifiersApplied, outcome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillRoll {\n");
    sb.append("    stat: ").append(toIndentedString(stat)).append("\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    modifiersApplied: ").append(toIndentedString(modifiersApplied)).append("\n");
    sb.append("    outcome: ").append(toIndentedString(outcome)).append("\n");
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

