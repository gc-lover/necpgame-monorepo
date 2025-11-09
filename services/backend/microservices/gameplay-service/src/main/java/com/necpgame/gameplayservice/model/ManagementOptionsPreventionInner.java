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
 * ManagementOptionsPreventionInner
 */

@JsonTypeName("ManagementOptions_prevention_inner")

public class ManagementOptionsPreventionInner {

  private @Nullable String optionType;

  private @Nullable BigDecimal effectiveness;

  private @Nullable BigDecimal cost;

  public ManagementOptionsPreventionInner optionType(@Nullable String optionType) {
    this.optionType = optionType;
    return this;
  }

  /**
   * Get optionType
   * @return optionType
   */
  
  @Schema(name = "option_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("option_type")
  public @Nullable String getOptionType() {
    return optionType;
  }

  public void setOptionType(@Nullable String optionType) {
    this.optionType = optionType;
  }

  public ManagementOptionsPreventionInner effectiveness(@Nullable BigDecimal effectiveness) {
    this.effectiveness = effectiveness;
    return this;
  }

  /**
   * Get effectiveness
   * @return effectiveness
   */
  @Valid 
  @Schema(name = "effectiveness", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effectiveness")
  public @Nullable BigDecimal getEffectiveness() {
    return effectiveness;
  }

  public void setEffectiveness(@Nullable BigDecimal effectiveness) {
    this.effectiveness = effectiveness;
  }

  public ManagementOptionsPreventionInner cost(@Nullable BigDecimal cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  @Valid 
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable BigDecimal getCost() {
    return cost;
  }

  public void setCost(@Nullable BigDecimal cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ManagementOptionsPreventionInner managementOptionsPreventionInner = (ManagementOptionsPreventionInner) o;
    return Objects.equals(this.optionType, managementOptionsPreventionInner.optionType) &&
        Objects.equals(this.effectiveness, managementOptionsPreventionInner.effectiveness) &&
        Objects.equals(this.cost, managementOptionsPreventionInner.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(optionType, effectiveness, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ManagementOptionsPreventionInner {\n");
    sb.append("    optionType: ").append(toIndentedString(optionType)).append("\n");
    sb.append("    effectiveness: ").append(toIndentedString(effectiveness)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

