package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ManagementOptionsPreventionInner;
import com.necpgame.gameplayservice.model.ManagementOptionsTreatmentInner;
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
 * ManagementOptions
 */


public class ManagementOptions {

  @Valid
  private List<@Valid ManagementOptionsPreventionInner> prevention = new ArrayList<>();

  @Valid
  private List<@Valid ManagementOptionsTreatmentInner> treatment = new ArrayList<>();

  public ManagementOptions prevention(List<@Valid ManagementOptionsPreventionInner> prevention) {
    this.prevention = prevention;
    return this;
  }

  public ManagementOptions addPreventionItem(ManagementOptionsPreventionInner preventionItem) {
    if (this.prevention == null) {
      this.prevention = new ArrayList<>();
    }
    this.prevention.add(preventionItem);
    return this;
  }

  /**
   * Опции профилактики
   * @return prevention
   */
  @Valid 
  @Schema(name = "prevention", description = "Опции профилактики", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("prevention")
  public List<@Valid ManagementOptionsPreventionInner> getPrevention() {
    return prevention;
  }

  public void setPrevention(List<@Valid ManagementOptionsPreventionInner> prevention) {
    this.prevention = prevention;
  }

  public ManagementOptions treatment(List<@Valid ManagementOptionsTreatmentInner> treatment) {
    this.treatment = treatment;
    return this;
  }

  public ManagementOptions addTreatmentItem(ManagementOptionsTreatmentInner treatmentItem) {
    if (this.treatment == null) {
      this.treatment = new ArrayList<>();
    }
    this.treatment.add(treatmentItem);
    return this;
  }

  /**
   * Опции лечения
   * @return treatment
   */
  @Valid 
  @Schema(name = "treatment", description = "Опции лечения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("treatment")
  public List<@Valid ManagementOptionsTreatmentInner> getTreatment() {
    return treatment;
  }

  public void setTreatment(List<@Valid ManagementOptionsTreatmentInner> treatment) {
    this.treatment = treatment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ManagementOptions managementOptions = (ManagementOptions) o;
    return Objects.equals(this.prevention, managementOptions.prevention) &&
        Objects.equals(this.treatment, managementOptions.treatment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prevention, treatment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ManagementOptions {\n");
    sb.append("    prevention: ").append(toIndentedString(prevention)).append("\n");
    sb.append("    treatment: ").append(toIndentedString(treatment)).append("\n");
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

