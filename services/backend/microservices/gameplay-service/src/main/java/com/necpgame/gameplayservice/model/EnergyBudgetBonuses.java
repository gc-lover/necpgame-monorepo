package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Бонусы к энергетическому пулу
 */

@Schema(name = "EnergyBudget_bonuses", description = "Бонусы к энергетическому пулу")
@JsonTypeName("EnergyBudget_bonuses")

public class EnergyBudgetBonuses {

  private @Nullable BigDecimal fromClass;

  private @Nullable BigDecimal fromSkills;

  private @Nullable BigDecimal fromImplants;

  public EnergyBudgetBonuses fromClass(@Nullable BigDecimal fromClass) {
    this.fromClass = fromClass;
    return this;
  }

  /**
   * Get fromClass
   * @return fromClass
   */
  @Valid 
  @Schema(name = "from_class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_class")
  public @Nullable BigDecimal getFromClass() {
    return fromClass;
  }

  public void setFromClass(@Nullable BigDecimal fromClass) {
    this.fromClass = fromClass;
  }

  public EnergyBudgetBonuses fromSkills(@Nullable BigDecimal fromSkills) {
    this.fromSkills = fromSkills;
    return this;
  }

  /**
   * Get fromSkills
   * @return fromSkills
   */
  @Valid 
  @Schema(name = "from_skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_skills")
  public @Nullable BigDecimal getFromSkills() {
    return fromSkills;
  }

  public void setFromSkills(@Nullable BigDecimal fromSkills) {
    this.fromSkills = fromSkills;
  }

  public EnergyBudgetBonuses fromImplants(@Nullable BigDecimal fromImplants) {
    this.fromImplants = fromImplants;
    return this;
  }

  /**
   * Get fromImplants
   * @return fromImplants
   */
  @Valid 
  @Schema(name = "from_implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_implants")
  public @Nullable BigDecimal getFromImplants() {
    return fromImplants;
  }

  public void setFromImplants(@Nullable BigDecimal fromImplants) {
    this.fromImplants = fromImplants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnergyBudgetBonuses energyBudgetBonuses = (EnergyBudgetBonuses) o;
    return Objects.equals(this.fromClass, energyBudgetBonuses.fromClass) &&
        Objects.equals(this.fromSkills, energyBudgetBonuses.fromSkills) &&
        Objects.equals(this.fromImplants, energyBudgetBonuses.fromImplants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromClass, fromSkills, fromImplants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnergyBudgetBonuses {\n");
    sb.append("    fromClass: ").append(toIndentedString(fromClass)).append("\n");
    sb.append("    fromSkills: ").append(toIndentedString(fromSkills)).append("\n");
    sb.append("    fromImplants: ").append(toIndentedString(fromImplants)).append("\n");
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

