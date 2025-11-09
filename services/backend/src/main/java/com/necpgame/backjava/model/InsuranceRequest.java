package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InsuranceRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class InsuranceRequest {

  private UUID shipmentId;

  /**
   * Gets or Sets plan
   */
  public enum PlanEnum {
    BASIC("BASIC"),
    
    STANDARD("STANDARD"),
    
    PREMIUM("PREMIUM");

    private final String value;

    PlanEnum(String value) {
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
    public static PlanEnum fromValue(String value) {
      for (PlanEnum b : PlanEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PlanEnum plan;

  public InsuranceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InsuranceRequest(UUID shipmentId, PlanEnum plan) {
    this.shipmentId = shipmentId;
    this.plan = plan;
  }

  public InsuranceRequest shipmentId(UUID shipmentId) {
    this.shipmentId = shipmentId;
    return this;
  }

  /**
   * Get shipmentId
   * @return shipmentId
   */
  @NotNull @Valid 
  @Schema(name = "shipment_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("shipment_id")
  public UUID getShipmentId() {
    return shipmentId;
  }

  public void setShipmentId(UUID shipmentId) {
    this.shipmentId = shipmentId;
  }

  public InsuranceRequest plan(PlanEnum plan) {
    this.plan = plan;
    return this;
  }

  /**
   * Get plan
   * @return plan
   */
  @NotNull 
  @Schema(name = "plan", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("plan")
  public PlanEnum getPlan() {
    return plan;
  }

  public void setPlan(PlanEnum plan) {
    this.plan = plan;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsuranceRequest insuranceRequest = (InsuranceRequest) o;
    return Objects.equals(this.shipmentId, insuranceRequest.shipmentId) &&
        Objects.equals(this.plan, insuranceRequest.plan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipmentId, plan);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsuranceRequest {\n");
    sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
    sb.append("    plan: ").append(toIndentedString(plan)).append("\n");
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

