package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformMicroCheck200Response
 */

@JsonTypeName("performMicroCheck_200_response")

public class PerformMicroCheck200Response {

  private @Nullable Integer roll;

  private @Nullable Integer total;

  private @Nullable Integer dc;

  private @Nullable Boolean success;

  private @Nullable Boolean critical;

  private @Nullable String effect;

  public PerformMicroCheck200Response roll(@Nullable Integer roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Get roll
   * @return roll
   */
  
  @Schema(name = "roll", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll")
  public @Nullable Integer getRoll() {
    return roll;
  }

  public void setRoll(@Nullable Integer roll) {
    this.roll = roll;
  }

  public PerformMicroCheck200Response total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public PerformMicroCheck200Response dc(@Nullable Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  
  @Schema(name = "dc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc")
  public @Nullable Integer getDc() {
    return dc;
  }

  public void setDc(@Nullable Integer dc) {
    this.dc = dc;
  }

  public PerformMicroCheck200Response success(@Nullable Boolean success) {
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

  public PerformMicroCheck200Response critical(@Nullable Boolean critical) {
    this.critical = critical;
    return this;
  }

  /**
   * Get critical
   * @return critical
   */
  
  @Schema(name = "critical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical")
  public @Nullable Boolean getCritical() {
    return critical;
  }

  public void setCritical(@Nullable Boolean critical) {
    this.critical = critical;
  }

  public PerformMicroCheck200Response effect(@Nullable String effect) {
    this.effect = effect;
    return this;
  }

  /**
   * Get effect
   * @return effect
   */
  
  @Schema(name = "effect", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effect")
  public @Nullable String getEffect() {
    return effect;
  }

  public void setEffect(@Nullable String effect) {
    this.effect = effect;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformMicroCheck200Response performMicroCheck200Response = (PerformMicroCheck200Response) o;
    return Objects.equals(this.roll, performMicroCheck200Response.roll) &&
        Objects.equals(this.total, performMicroCheck200Response.total) &&
        Objects.equals(this.dc, performMicroCheck200Response.dc) &&
        Objects.equals(this.success, performMicroCheck200Response.success) &&
        Objects.equals(this.critical, performMicroCheck200Response.critical) &&
        Objects.equals(this.effect, performMicroCheck200Response.effect);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roll, total, dc, success, critical, effect);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformMicroCheck200Response {\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    critical: ").append(toIndentedString(critical)).append("\n");
    sb.append("    effect: ").append(toIndentedString(effect)).append("\n");
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

