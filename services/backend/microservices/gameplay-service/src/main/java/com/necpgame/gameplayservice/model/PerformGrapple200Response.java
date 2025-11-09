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
 * PerformGrapple200Response
 */

@JsonTypeName("performGrapple_200_response")

public class PerformGrapple200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal travelTime;

  private @Nullable BigDecimal staminaCost;

  public PerformGrapple200Response success(@Nullable Boolean success) {
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

  public PerformGrapple200Response travelTime(@Nullable BigDecimal travelTime) {
    this.travelTime = travelTime;
    return this;
  }

  /**
   * Get travelTime
   * @return travelTime
   */
  @Valid 
  @Schema(name = "travel_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("travel_time")
  public @Nullable BigDecimal getTravelTime() {
    return travelTime;
  }

  public void setTravelTime(@Nullable BigDecimal travelTime) {
    this.travelTime = travelTime;
  }

  public PerformGrapple200Response staminaCost(@Nullable BigDecimal staminaCost) {
    this.staminaCost = staminaCost;
    return this;
  }

  /**
   * Get staminaCost
   * @return staminaCost
   */
  @Valid 
  @Schema(name = "stamina_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stamina_cost")
  public @Nullable BigDecimal getStaminaCost() {
    return staminaCost;
  }

  public void setStaminaCost(@Nullable BigDecimal staminaCost) {
    this.staminaCost = staminaCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformGrapple200Response performGrapple200Response = (PerformGrapple200Response) o;
    return Objects.equals(this.success, performGrapple200Response.success) &&
        Objects.equals(this.travelTime, performGrapple200Response.travelTime) &&
        Objects.equals(this.staminaCost, performGrapple200Response.staminaCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, travelTime, staminaCost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformGrapple200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    travelTime: ").append(toIndentedString(travelTime)).append("\n");
    sb.append("    staminaCost: ").append(toIndentedString(staminaCost)).append("\n");
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

