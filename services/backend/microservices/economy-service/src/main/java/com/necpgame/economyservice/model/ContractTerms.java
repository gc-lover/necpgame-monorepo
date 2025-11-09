package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ContractTermsBonuses;
import com.necpgame.economyservice.model.ContractTermsConditionsInner;
import com.necpgame.economyservice.model.ContractTermsDeliverablesInner;
import com.necpgame.economyservice.model.ContractTermsPenalties;
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
 * ContractTerms
 */


public class ContractTerms {

  @Valid
  private List<@Valid ContractTermsDeliverablesInner> deliverables = new ArrayList<>();

  @Valid
  private List<@Valid ContractTermsConditionsInner> conditions = new ArrayList<>();

  private @Nullable ContractTermsPenalties penalties;

  private @Nullable ContractTermsBonuses bonuses;

  public ContractTerms deliverables(List<@Valid ContractTermsDeliverablesInner> deliverables) {
    this.deliverables = deliverables;
    return this;
  }

  public ContractTerms addDeliverablesItem(ContractTermsDeliverablesInner deliverablesItem) {
    if (this.deliverables == null) {
      this.deliverables = new ArrayList<>();
    }
    this.deliverables.add(deliverablesItem);
    return this;
  }

  /**
   * Get deliverables
   * @return deliverables
   */
  @Valid 
  @Schema(name = "deliverables", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliverables")
  public List<@Valid ContractTermsDeliverablesInner> getDeliverables() {
    return deliverables;
  }

  public void setDeliverables(List<@Valid ContractTermsDeliverablesInner> deliverables) {
    this.deliverables = deliverables;
  }

  public ContractTerms conditions(List<@Valid ContractTermsConditionsInner> conditions) {
    this.conditions = conditions;
    return this;
  }

  public ContractTerms addConditionsItem(ContractTermsConditionsInner conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

  /**
   * Get conditions
   * @return conditions
   */
  @Valid 
  @Schema(name = "conditions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conditions")
  public List<@Valid ContractTermsConditionsInner> getConditions() {
    return conditions;
  }

  public void setConditions(List<@Valid ContractTermsConditionsInner> conditions) {
    this.conditions = conditions;
  }

  public ContractTerms penalties(@Nullable ContractTermsPenalties penalties) {
    this.penalties = penalties;
    return this;
  }

  /**
   * Get penalties
   * @return penalties
   */
  @Valid 
  @Schema(name = "penalties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public @Nullable ContractTermsPenalties getPenalties() {
    return penalties;
  }

  public void setPenalties(@Nullable ContractTermsPenalties penalties) {
    this.penalties = penalties;
  }

  public ContractTerms bonuses(@Nullable ContractTermsBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable ContractTermsBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable ContractTermsBonuses bonuses) {
    this.bonuses = bonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractTerms contractTerms = (ContractTerms) o;
    return Objects.equals(this.deliverables, contractTerms.deliverables) &&
        Objects.equals(this.conditions, contractTerms.conditions) &&
        Objects.equals(this.penalties, contractTerms.penalties) &&
        Objects.equals(this.bonuses, contractTerms.bonuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliverables, conditions, penalties, bonuses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractTerms {\n");
    sb.append("    deliverables: ").append(toIndentedString(deliverables)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
    sb.append("    penalties: ").append(toIndentedString(penalties)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
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

