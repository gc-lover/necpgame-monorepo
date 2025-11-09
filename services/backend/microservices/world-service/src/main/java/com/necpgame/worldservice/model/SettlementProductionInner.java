package com.necpgame.worldservice.model;

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
 * SettlementProductionInner
 */

@JsonTypeName("Settlement_production_inner")

public class SettlementProductionInner {

  private @Nullable String resource;

  private @Nullable BigDecimal outputPerHour;

  public SettlementProductionInner resource(@Nullable String resource) {
    this.resource = resource;
    return this;
  }

  /**
   * Get resource
   * @return resource
   */
  
  @Schema(name = "resource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resource")
  public @Nullable String getResource() {
    return resource;
  }

  public void setResource(@Nullable String resource) {
    this.resource = resource;
  }

  public SettlementProductionInner outputPerHour(@Nullable BigDecimal outputPerHour) {
    this.outputPerHour = outputPerHour;
    return this;
  }

  /**
   * Get outputPerHour
   * @return outputPerHour
   */
  @Valid 
  @Schema(name = "outputPerHour", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outputPerHour")
  public @Nullable BigDecimal getOutputPerHour() {
    return outputPerHour;
  }

  public void setOutputPerHour(@Nullable BigDecimal outputPerHour) {
    this.outputPerHour = outputPerHour;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SettlementProductionInner settlementProductionInner = (SettlementProductionInner) o;
    return Objects.equals(this.resource, settlementProductionInner.resource) &&
        Objects.equals(this.outputPerHour, settlementProductionInner.outputPerHour);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resource, outputPerHour);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SettlementProductionInner {\n");
    sb.append("    resource: ").append(toIndentedString(resource)).append("\n");
    sb.append("    outputPerHour: ").append(toIndentedString(outputPerHour)).append("\n");
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

