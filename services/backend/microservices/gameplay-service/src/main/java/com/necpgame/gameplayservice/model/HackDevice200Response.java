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
 * HackDevice200Response
 */

@JsonTypeName("hackDevice_200_response")

public class HackDevice200Response {

  private @Nullable Boolean success;

  private @Nullable String deviceId;

  private @Nullable String action;

  private @Nullable BigDecimal duration;

  private @Nullable BigDecimal heatGenerated;

  public HackDevice200Response success(@Nullable Boolean success) {
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

  public HackDevice200Response deviceId(@Nullable String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  /**
   * Get deviceId
   * @return deviceId
   */
  
  @Schema(name = "device_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("device_id")
  public @Nullable String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(@Nullable String deviceId) {
    this.deviceId = deviceId;
  }

  public HackDevice200Response action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  public HackDevice200Response duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Длительность эффекта (секунды)
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", description = "Длительность эффекта (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  public HackDevice200Response heatGenerated(@Nullable BigDecimal heatGenerated) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackDevice200Response hackDevice200Response = (HackDevice200Response) o;
    return Objects.equals(this.success, hackDevice200Response.success) &&
        Objects.equals(this.deviceId, hackDevice200Response.deviceId) &&
        Objects.equals(this.action, hackDevice200Response.action) &&
        Objects.equals(this.duration, hackDevice200Response.duration) &&
        Objects.equals(this.heatGenerated, hackDevice200Response.heatGenerated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, deviceId, action, duration, heatGenerated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackDevice200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    heatGenerated: ").append(toIndentedString(heatGenerated)).append("\n");
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

