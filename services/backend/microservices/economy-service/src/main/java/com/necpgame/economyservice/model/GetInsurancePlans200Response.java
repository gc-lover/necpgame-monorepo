package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.InsurancePlan;
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
 * GetInsurancePlans200Response
 */

@JsonTypeName("getInsurancePlans_200_response")

public class GetInsurancePlans200Response {

  @Valid
  private List<@Valid InsurancePlan> plans = new ArrayList<>();

  public GetInsurancePlans200Response plans(List<@Valid InsurancePlan> plans) {
    this.plans = plans;
    return this;
  }

  public GetInsurancePlans200Response addPlansItem(InsurancePlan plansItem) {
    if (this.plans == null) {
      this.plans = new ArrayList<>();
    }
    this.plans.add(plansItem);
    return this;
  }

  /**
   * Get plans
   * @return plans
   */
  @Valid 
  @Schema(name = "plans", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("plans")
  public List<@Valid InsurancePlan> getPlans() {
    return plans;
  }

  public void setPlans(List<@Valid InsurancePlan> plans) {
    this.plans = plans;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetInsurancePlans200Response getInsurancePlans200Response = (GetInsurancePlans200Response) o;
    return Objects.equals(this.plans, getInsurancePlans200Response.plans);
  }

  @Override
  public int hashCode() {
    return Objects.hash(plans);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetInsurancePlans200Response {\n");
    sb.append("    plans: ").append(toIndentedString(plans)).append("\n");
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

