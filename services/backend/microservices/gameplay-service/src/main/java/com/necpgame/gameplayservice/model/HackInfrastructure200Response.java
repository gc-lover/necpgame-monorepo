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
 * HackInfrastructure200Response
 */

@JsonTypeName("hackInfrastructure_200_response")

public class HackInfrastructure200Response {

  private @Nullable Boolean success;

  private @Nullable String infrastructureId;

  private @Nullable Object effect;

  private @Nullable BigDecimal heatGenerated;

  private @Nullable BigDecimal traceRisk;

  public HackInfrastructure200Response success(@Nullable Boolean success) {
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

  public HackInfrastructure200Response infrastructureId(@Nullable String infrastructureId) {
    this.infrastructureId = infrastructureId;
    return this;
  }

  /**
   * Get infrastructureId
   * @return infrastructureId
   */
  
  @Schema(name = "infrastructure_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("infrastructure_id")
  public @Nullable String getInfrastructureId() {
    return infrastructureId;
  }

  public void setInfrastructureId(@Nullable String infrastructureId) {
    this.infrastructureId = infrastructureId;
  }

  public HackInfrastructure200Response effect(@Nullable Object effect) {
    this.effect = effect;
    return this;
  }

  /**
   * Get effect
   * @return effect
   */
  
  @Schema(name = "effect", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effect")
  public @Nullable Object getEffect() {
    return effect;
  }

  public void setEffect(@Nullable Object effect) {
    this.effect = effect;
  }

  public HackInfrastructure200Response heatGenerated(@Nullable BigDecimal heatGenerated) {
    this.heatGenerated = heatGenerated;
    return this;
  }

  /**
   * Get heatGenerated
   * @return heatGenerated
   */
  @Valid 
  @Schema(name = "heat_generated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heat_generated")
  public @Nullable BigDecimal getHeatGenerated() {
    return heatGenerated;
  }

  public void setHeatGenerated(@Nullable BigDecimal heatGenerated) {
    this.heatGenerated = heatGenerated;
  }

  public HackInfrastructure200Response traceRisk(@Nullable BigDecimal traceRisk) {
    this.traceRisk = traceRisk;
    return this;
  }

  /**
   * Риск отследить (%)
   * @return traceRisk
   */
  @Valid 
  @Schema(name = "trace_risk", description = "Риск отследить (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trace_risk")
  public @Nullable BigDecimal getTraceRisk() {
    return traceRisk;
  }

  public void setTraceRisk(@Nullable BigDecimal traceRisk) {
    this.traceRisk = traceRisk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackInfrastructure200Response hackInfrastructure200Response = (HackInfrastructure200Response) o;
    return Objects.equals(this.success, hackInfrastructure200Response.success) &&
        Objects.equals(this.infrastructureId, hackInfrastructure200Response.infrastructureId) &&
        Objects.equals(this.effect, hackInfrastructure200Response.effect) &&
        Objects.equals(this.heatGenerated, hackInfrastructure200Response.heatGenerated) &&
        Objects.equals(this.traceRisk, hackInfrastructure200Response.traceRisk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, infrastructureId, effect, heatGenerated, traceRisk);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackInfrastructure200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    infrastructureId: ").append(toIndentedString(infrastructureId)).append("\n");
    sb.append("    effect: ").append(toIndentedString(effect)).append("\n");
    sb.append("    heatGenerated: ").append(toIndentedString(heatGenerated)).append("\n");
    sb.append("    traceRisk: ").append(toIndentedString(traceRisk)).append("\n");
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

