package com.necpgame.narrativeservice.model;

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
 * HandleRealityAnomaly200Response
 */

@JsonTypeName("handleRealityAnomaly_200_response")

public class HandleRealityAnomaly200Response {

  private @Nullable Boolean success;

  private @Nullable String anomalyId;

  private @Nullable BigDecimal sanityImpact;

  public HandleRealityAnomaly200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public HandleRealityAnomaly200Response anomalyId(@Nullable String anomalyId) {
    this.anomalyId = anomalyId;
    return this;
  }

  /**
   * Get anomalyId
   * @return anomalyId
   */
  
  @Schema(name = "anomaly_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("anomaly_id")
  public @Nullable String getAnomalyId() {
    return anomalyId;
  }

  public void setAnomalyId(@Nullable String anomalyId) {
    this.anomalyId = anomalyId;
  }

  public HandleRealityAnomaly200Response sanityImpact(@Nullable BigDecimal sanityImpact) {
    this.sanityImpact = sanityImpact;
    return this;
  }

  /**
   * Влияние на sanity группы
   * @return sanityImpact
   */
  @Valid 
  @Schema(name = "sanity_impact", description = "Влияние на sanity группы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sanity_impact")
  public @Nullable BigDecimal getSanityImpact() {
    return sanityImpact;
  }

  public void setSanityImpact(@Nullable BigDecimal sanityImpact) {
    this.sanityImpact = sanityImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HandleRealityAnomaly200Response handleRealityAnomaly200Response = (HandleRealityAnomaly200Response) o;
    return Objects.equals(this.success, handleRealityAnomaly200Response.success) &&
        Objects.equals(this.anomalyId, handleRealityAnomaly200Response.anomalyId) &&
        Objects.equals(this.sanityImpact, handleRealityAnomaly200Response.sanityImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, anomalyId, sanityImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HandleRealityAnomaly200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    anomalyId: ").append(toIndentedString(anomalyId)).append("\n");
    sb.append("    sanityImpact: ").append(toIndentedString(sanityImpact)).append("\n");
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

