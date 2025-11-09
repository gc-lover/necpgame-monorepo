package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * SkillExperienceResultRewards
 */

@JsonTypeName("SkillExperienceResult_rewards")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SkillExperienceResultRewards {

  private @Nullable Integer perkPoints;

  @Valid
  private List<String> unlockedAbilities = new ArrayList<>();

  public SkillExperienceResultRewards perkPoints(@Nullable Integer perkPoints) {
    this.perkPoints = perkPoints;
    return this;
  }

  /**
   * Get perkPoints
   * @return perkPoints
   */
  
  @Schema(name = "perk_points", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perk_points")
  public @Nullable Integer getPerkPoints() {
    return perkPoints;
  }

  public void setPerkPoints(@Nullable Integer perkPoints) {
    this.perkPoints = perkPoints;
  }

  public SkillExperienceResultRewards unlockedAbilities(List<String> unlockedAbilities) {
    this.unlockedAbilities = unlockedAbilities;
    return this;
  }

  public SkillExperienceResultRewards addUnlockedAbilitiesItem(String unlockedAbilitiesItem) {
    if (this.unlockedAbilities == null) {
      this.unlockedAbilities = new ArrayList<>();
    }
    this.unlockedAbilities.add(unlockedAbilitiesItem);
    return this;
  }

  /**
   * Get unlockedAbilities
   * @return unlockedAbilities
   */
  
  @Schema(name = "unlocked_abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_abilities")
  public List<String> getUnlockedAbilities() {
    return unlockedAbilities;
  }

  public void setUnlockedAbilities(List<String> unlockedAbilities) {
    this.unlockedAbilities = unlockedAbilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillExperienceResultRewards skillExperienceResultRewards = (SkillExperienceResultRewards) o;
    return Objects.equals(this.perkPoints, skillExperienceResultRewards.perkPoints) &&
        Objects.equals(this.unlockedAbilities, skillExperienceResultRewards.unlockedAbilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(perkPoints, unlockedAbilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillExperienceResultRewards {\n");
    sb.append("    perkPoints: ").append(toIndentedString(perkPoints)).append("\n");
    sb.append("    unlockedAbilities: ").append(toIndentedString(unlockedAbilities)).append("\n");
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

