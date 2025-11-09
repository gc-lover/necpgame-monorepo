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
 * PerformClimb200Response
 */

@JsonTypeName("performClimb_200_response")

public class PerformClimb200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal staminaCost;

  private @Nullable BigDecimal climbTime;

  public PerformClimb200Response success(@Nullable Boolean success) {
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

  public PerformClimb200Response staminaCost(@Nullable BigDecimal staminaCost) {
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

  public PerformClimb200Response climbTime(@Nullable BigDecimal climbTime) {
    this.climbTime = climbTime;
    return this;
  }

  /**
   * Время лазания (секунды)
   * @return climbTime
   */
  @Valid 
  @Schema(name = "climb_time", description = "Время лазания (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("climb_time")
  public @Nullable BigDecimal getClimbTime() {
    return climbTime;
  }

  public void setClimbTime(@Nullable BigDecimal climbTime) {
    this.climbTime = climbTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformClimb200Response performClimb200Response = (PerformClimb200Response) o;
    return Objects.equals(this.success, performClimb200Response.success) &&
        Objects.equals(this.staminaCost, performClimb200Response.staminaCost) &&
        Objects.equals(this.climbTime, performClimb200Response.climbTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, staminaCost, climbTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformClimb200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    staminaCost: ").append(toIndentedString(staminaCost)).append("\n");
    sb.append("    climbTime: ").append(toIndentedString(climbTime)).append("\n");
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

