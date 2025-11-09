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
 * PerformStealthKillCheck200Response
 */

@JsonTypeName("performStealthKillCheck_200_response")

public class PerformStealthKillCheck200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal detectionRisk;

  private @Nullable Integer noiseGenerated;

  private @Nullable Boolean instantKill;

  public PerformStealthKillCheck200Response success(@Nullable Boolean success) {
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

  public PerformStealthKillCheck200Response detectionRisk(@Nullable BigDecimal detectionRisk) {
    this.detectionRisk = detectionRisk;
    return this;
  }

  /**
   * Get detectionRisk
   * @return detectionRisk
   */
  @Valid 
  @Schema(name = "detection_risk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detection_risk")
  public @Nullable BigDecimal getDetectionRisk() {
    return detectionRisk;
  }

  public void setDetectionRisk(@Nullable BigDecimal detectionRisk) {
    this.detectionRisk = detectionRisk;
  }

  public PerformStealthKillCheck200Response noiseGenerated(@Nullable Integer noiseGenerated) {
    this.noiseGenerated = noiseGenerated;
    return this;
  }

  /**
   * Get noiseGenerated
   * @return noiseGenerated
   */
  
  @Schema(name = "noise_generated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("noise_generated")
  public @Nullable Integer getNoiseGenerated() {
    return noiseGenerated;
  }

  public void setNoiseGenerated(@Nullable Integer noiseGenerated) {
    this.noiseGenerated = noiseGenerated;
  }

  public PerformStealthKillCheck200Response instantKill(@Nullable Boolean instantKill) {
    this.instantKill = instantKill;
    return this;
  }

  /**
   * Get instantKill
   * @return instantKill
   */
  
  @Schema(name = "instant_kill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instant_kill")
  public @Nullable Boolean getInstantKill() {
    return instantKill;
  }

  public void setInstantKill(@Nullable Boolean instantKill) {
    this.instantKill = instantKill;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformStealthKillCheck200Response performStealthKillCheck200Response = (PerformStealthKillCheck200Response) o;
    return Objects.equals(this.success, performStealthKillCheck200Response.success) &&
        Objects.equals(this.detectionRisk, performStealthKillCheck200Response.detectionRisk) &&
        Objects.equals(this.noiseGenerated, performStealthKillCheck200Response.noiseGenerated) &&
        Objects.equals(this.instantKill, performStealthKillCheck200Response.instantKill);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, detectionRisk, noiseGenerated, instantKill);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformStealthKillCheck200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    detectionRisk: ").append(toIndentedString(detectionRisk)).append("\n");
    sb.append("    noiseGenerated: ").append(toIndentedString(noiseGenerated)).append("\n");
    sb.append("    instantKill: ").append(toIndentedString(instantKill)).append("\n");
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

