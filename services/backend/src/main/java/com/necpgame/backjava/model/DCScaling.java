package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.DCScalingExampleChallengesInner;
import com.necpgame.backjava.model.DCScalingModifiers;
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
 * DCScaling
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DCScaling {

  private @Nullable String era;

  private @Nullable Integer baseDc;

  private @Nullable DCScalingModifiers modifiers;

  @Valid
  private List<@Valid DCScalingExampleChallengesInner> exampleChallenges = new ArrayList<>();

  public DCScaling era(@Nullable String era) {
    this.era = era;
    return this;
  }

  /**
   * Get era
   * @return era
   */
  
  @Schema(name = "era", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era")
  public @Nullable String getEra() {
    return era;
  }

  public void setEra(@Nullable String era) {
    this.era = era;
  }

  public DCScaling baseDc(@Nullable Integer baseDc) {
    this.baseDc = baseDc;
    return this;
  }

  /**
   * Базовая сложность проверок
   * @return baseDc
   */
  
  @Schema(name = "base_dc", description = "Базовая сложность проверок", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_dc")
  public @Nullable Integer getBaseDc() {
    return baseDc;
  }

  public void setBaseDc(@Nullable Integer baseDc) {
    this.baseDc = baseDc;
  }

  public DCScaling modifiers(@Nullable DCScalingModifiers modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  @Valid 
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public @Nullable DCScalingModifiers getModifiers() {
    return modifiers;
  }

  public void setModifiers(@Nullable DCScalingModifiers modifiers) {
    this.modifiers = modifiers;
  }

  public DCScaling exampleChallenges(List<@Valid DCScalingExampleChallengesInner> exampleChallenges) {
    this.exampleChallenges = exampleChallenges;
    return this;
  }

  public DCScaling addExampleChallengesItem(DCScalingExampleChallengesInner exampleChallengesItem) {
    if (this.exampleChallenges == null) {
      this.exampleChallenges = new ArrayList<>();
    }
    this.exampleChallenges.add(exampleChallengesItem);
    return this;
  }

  /**
   * Get exampleChallenges
   * @return exampleChallenges
   */
  @Valid 
  @Schema(name = "example_challenges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("example_challenges")
  public List<@Valid DCScalingExampleChallengesInner> getExampleChallenges() {
    return exampleChallenges;
  }

  public void setExampleChallenges(List<@Valid DCScalingExampleChallengesInner> exampleChallenges) {
    this.exampleChallenges = exampleChallenges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DCScaling dcScaling = (DCScaling) o;
    return Objects.equals(this.era, dcScaling.era) &&
        Objects.equals(this.baseDc, dcScaling.baseDc) &&
        Objects.equals(this.modifiers, dcScaling.modifiers) &&
        Objects.equals(this.exampleChallenges, dcScaling.exampleChallenges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(era, baseDc, modifiers, exampleChallenges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCScaling {\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    baseDc: ").append(toIndentedString(baseDc)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
    sb.append("    exampleChallenges: ").append(toIndentedString(exampleChallenges)).append("\n");
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

