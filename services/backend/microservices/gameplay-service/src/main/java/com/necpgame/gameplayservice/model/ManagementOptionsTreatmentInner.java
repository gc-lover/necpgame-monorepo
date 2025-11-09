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
 * ManagementOptionsTreatmentInner
 */

@JsonTypeName("ManagementOptions_treatment_inner")

public class ManagementOptionsTreatmentInner {

  private @Nullable String treatmentType;

  private @Nullable BigDecimal humanityRestoration;

  private @Nullable BigDecimal cost;

  private @Nullable Boolean availability;

  public ManagementOptionsTreatmentInner treatmentType(@Nullable String treatmentType) {
    this.treatmentType = treatmentType;
    return this;
  }

  /**
   * Get treatmentType
   * @return treatmentType
   */
  
  @Schema(name = "treatment_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("treatment_type")
  public @Nullable String getTreatmentType() {
    return treatmentType;
  }

  public void setTreatmentType(@Nullable String treatmentType) {
    this.treatmentType = treatmentType;
  }

  public ManagementOptionsTreatmentInner humanityRestoration(@Nullable BigDecimal humanityRestoration) {
    this.humanityRestoration = humanityRestoration;
    return this;
  }

  /**
   * Get humanityRestoration
   * @return humanityRestoration
   */
  @Valid 
  @Schema(name = "humanity_restoration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_restoration")
  public @Nullable BigDecimal getHumanityRestoration() {
    return humanityRestoration;
  }

  public void setHumanityRestoration(@Nullable BigDecimal humanityRestoration) {
    this.humanityRestoration = humanityRestoration;
  }

  public ManagementOptionsTreatmentInner cost(@Nullable BigDecimal cost) {
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

  public ManagementOptionsTreatmentInner availability(@Nullable Boolean availability) {
    this.availability = availability;
    return this;
  }

  /**
   * Get availability
   * @return availability
   */
  
  @Schema(name = "availability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availability")
  public @Nullable Boolean getAvailability() {
    return availability;
  }

  public void setAvailability(@Nullable Boolean availability) {
    this.availability = availability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ManagementOptionsTreatmentInner managementOptionsTreatmentInner = (ManagementOptionsTreatmentInner) o;
    return Objects.equals(this.treatmentType, managementOptionsTreatmentInner.treatmentType) &&
        Objects.equals(this.humanityRestoration, managementOptionsTreatmentInner.humanityRestoration) &&
        Objects.equals(this.cost, managementOptionsTreatmentInner.cost) &&
        Objects.equals(this.availability, managementOptionsTreatmentInner.availability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(treatmentType, humanityRestoration, cost, availability);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ManagementOptionsTreatmentInner {\n");
    sb.append("    treatmentType: ").append(toIndentedString(treatmentType)).append("\n");
    sb.append("    humanityRestoration: ").append(toIndentedString(humanityRestoration)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
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

