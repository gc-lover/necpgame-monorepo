package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.HackEnemyImplant200ResponseEffect;
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
 * HackEnemyImplant200Response
 */

@JsonTypeName("hackEnemyImplant_200_response")

public class HackEnemyImplant200Response {

  private @Nullable Boolean success;

  private @Nullable String targetId;

  private @Nullable String implantId;

  private @Nullable HackEnemyImplant200ResponseEffect effect;

  private @Nullable Boolean iceBypassed;

  private @Nullable BigDecimal traceRisk;

  public HackEnemyImplant200Response success(@Nullable Boolean success) {
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

  public HackEnemyImplant200Response targetId(@Nullable String targetId) {
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

  public HackEnemyImplant200Response implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public HackEnemyImplant200Response effect(@Nullable HackEnemyImplant200ResponseEffect effect) {
    this.effect = effect;
    return this;
  }

  /**
   * Get effect
   * @return effect
   */
  @Valid 
  @Schema(name = "effect", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effect")
  public @Nullable HackEnemyImplant200ResponseEffect getEffect() {
    return effect;
  }

  public void setEffect(@Nullable HackEnemyImplant200ResponseEffect effect) {
    this.effect = effect;
  }

  public HackEnemyImplant200Response iceBypassed(@Nullable Boolean iceBypassed) {
    this.iceBypassed = iceBypassed;
    return this;
  }

  /**
   * Get iceBypassed
   * @return iceBypassed
   */
  
  @Schema(name = "ice_bypassed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ice_bypassed")
  public @Nullable Boolean getIceBypassed() {
    return iceBypassed;
  }

  public void setIceBypassed(@Nullable Boolean iceBypassed) {
    this.iceBypassed = iceBypassed;
  }

  public HackEnemyImplant200Response traceRisk(@Nullable BigDecimal traceRisk) {
    this.traceRisk = traceRisk;
    return this;
  }

  /**
   * Get traceRisk
   * @return traceRisk
   */
  @Valid 
  @Schema(name = "trace_risk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    HackEnemyImplant200Response hackEnemyImplant200Response = (HackEnemyImplant200Response) o;
    return Objects.equals(this.success, hackEnemyImplant200Response.success) &&
        Objects.equals(this.targetId, hackEnemyImplant200Response.targetId) &&
        Objects.equals(this.implantId, hackEnemyImplant200Response.implantId) &&
        Objects.equals(this.effect, hackEnemyImplant200Response.effect) &&
        Objects.equals(this.iceBypassed, hackEnemyImplant200Response.iceBypassed) &&
        Objects.equals(this.traceRisk, hackEnemyImplant200Response.traceRisk);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, targetId, implantId, effect, iceBypassed, traceRisk);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackEnemyImplant200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    effect: ").append(toIndentedString(effect)).append("\n");
    sb.append("    iceBypassed: ").append(toIndentedString(iceBypassed)).append("\n");
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

