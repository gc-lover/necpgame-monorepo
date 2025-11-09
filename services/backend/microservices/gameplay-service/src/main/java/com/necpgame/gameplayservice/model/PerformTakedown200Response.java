package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PerformTakedown200Response
 */

@JsonTypeName("performTakedown_200_response")

public class PerformTakedown200Response {

  private @Nullable Boolean success;

  private @Nullable String targetId;

  /**
   * Gets or Sets targetStatus
   */
  public enum TargetStatusEnum {
    UNCONSCIOUS("unconscious"),
    
    DEAD("dead");

    private final String value;

    TargetStatusEnum(String value) {
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
    public static TargetStatusEnum fromValue(String value) {
      for (TargetStatusEnum b : TargetStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TargetStatusEnum targetStatus;

  private @Nullable BigDecimal detectionRisk;

  private @Nullable BigDecimal noiseGenerated;

  public PerformTakedown200Response success(@Nullable Boolean success) {
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

  public PerformTakedown200Response targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_id")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public PerformTakedown200Response targetStatus(@Nullable TargetStatusEnum targetStatus) {
    this.targetStatus = targetStatus;
    return this;
  }

  /**
   * Get targetStatus
   * @return targetStatus
   */
  
  @Schema(name = "target_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_status")
  public @Nullable TargetStatusEnum getTargetStatus() {
    return targetStatus;
  }

  public void setTargetStatus(@Nullable TargetStatusEnum targetStatus) {
    this.targetStatus = targetStatus;
  }

  public PerformTakedown200Response detectionRisk(@Nullable BigDecimal detectionRisk) {
    this.detectionRisk = detectionRisk;
    return this;
  }

  /**
   * Риск обнаружения (%)
   * @return detectionRisk
   */
  @Valid 
  @Schema(name = "detection_risk", description = "Риск обнаружения (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detection_risk")
  public @Nullable BigDecimal getDetectionRisk() {
    return detectionRisk;
  }

  public void setDetectionRisk(@Nullable BigDecimal detectionRisk) {
    this.detectionRisk = detectionRisk;
  }

  public PerformTakedown200Response noiseGenerated(@Nullable BigDecimal noiseGenerated) {
    this.noiseGenerated = noiseGenerated;
    return this;
  }

  /**
   * Get noiseGenerated
   * @return noiseGenerated
   */
  @Valid 
  @Schema(name = "noise_generated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("noise_generated")
  public @Nullable BigDecimal getNoiseGenerated() {
    return noiseGenerated;
  }

  public void setNoiseGenerated(@Nullable BigDecimal noiseGenerated) {
    this.noiseGenerated = noiseGenerated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformTakedown200Response performTakedown200Response = (PerformTakedown200Response) o;
    return Objects.equals(this.success, performTakedown200Response.success) &&
        Objects.equals(this.targetId, performTakedown200Response.targetId) &&
        Objects.equals(this.targetStatus, performTakedown200Response.targetStatus) &&
        Objects.equals(this.detectionRisk, performTakedown200Response.detectionRisk) &&
        Objects.equals(this.noiseGenerated, performTakedown200Response.noiseGenerated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, targetId, targetStatus, detectionRisk, noiseGenerated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformTakedown200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    targetStatus: ").append(toIndentedString(targetStatus)).append("\n");
    sb.append("    detectionRisk: ").append(toIndentedString(detectionRisk)).append("\n");
    sb.append("    noiseGenerated: ").append(toIndentedString(noiseGenerated)).append("\n");
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

