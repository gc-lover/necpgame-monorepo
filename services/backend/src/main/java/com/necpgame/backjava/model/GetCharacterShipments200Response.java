package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.Shipment;
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
 * GetCharacterShipments200Response
 */

@JsonTypeName("getCharacterShipments_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetCharacterShipments200Response {

  @Valid
  private List<@Valid Shipment> shipments = new ArrayList<>();

  public GetCharacterShipments200Response shipments(List<@Valid Shipment> shipments) {
    this.shipments = shipments;
    return this;
  }

  public GetCharacterShipments200Response addShipmentsItem(Shipment shipmentsItem) {
    if (this.shipments == null) {
      this.shipments = new ArrayList<>();
    }
    this.shipments.add(shipmentsItem);
    return this;
  }

  /**
   * Get shipments
   * @return shipments
   */
  @Valid 
  @Schema(name = "shipments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shipments")
  public List<@Valid Shipment> getShipments() {
    return shipments;
  }

  public void setShipments(List<@Valid Shipment> shipments) {
    this.shipments = shipments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCharacterShipments200Response getCharacterShipments200Response = (GetCharacterShipments200Response) o;
    return Objects.equals(this.shipments, getCharacterShipments200Response.shipments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacterShipments200Response {\n");
    sb.append("    shipments: ").append(toIndentedString(shipments)).append("\n");
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

