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
 * ActivateOpticalCamo200Response
 */

@JsonTypeName("activateOpticalCamo_200_response")

public class ActivateOpticalCamo200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal activeDuration;

  private @Nullable BigDecimal energyCost;

  private @Nullable BigDecimal visibilityReduction;

  public ActivateOpticalCamo200Response success(@Nullable Boolean success) {
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

  public ActivateOpticalCamo200Response activeDuration(@Nullable BigDecimal activeDuration) {
    this.activeDuration = activeDuration;
    return this;
  }

  /**
   * Get activeDuration
   * @return activeDuration
   */
  @Valid 
  @Schema(name = "active_duration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_duration")
  public @Nullable BigDecimal getActiveDuration() {
    return activeDuration;
  }

  public void setActiveDuration(@Nullable BigDecimal activeDuration) {
    this.activeDuration = activeDuration;
  }

  public ActivateOpticalCamo200Response energyCost(@Nullable BigDecimal energyCost) {
    this.energyCost = energyCost;
    return this;
  }

  /**
   * Get energyCost
   * @return energyCost
   */
  @Valid 
  @Schema(name = "energy_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_cost")
  public @Nullable BigDecimal getEnergyCost() {
    return energyCost;
  }

  public void setEnergyCost(@Nullable BigDecimal energyCost) {
    this.energyCost = energyCost;
  }

  public ActivateOpticalCamo200Response visibilityReduction(@Nullable BigDecimal visibilityReduction) {
    this.visibilityReduction = visibilityReduction;
    return this;
  }

  /**
   * Снижение видимости (%)
   * @return visibilityReduction
   */
  @Valid 
  @Schema(name = "visibility_reduction", description = "Снижение видимости (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility_reduction")
  public @Nullable BigDecimal getVisibilityReduction() {
    return visibilityReduction;
  }

  public void setVisibilityReduction(@Nullable BigDecimal visibilityReduction) {
    this.visibilityReduction = visibilityReduction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivateOpticalCamo200Response activateOpticalCamo200Response = (ActivateOpticalCamo200Response) o;
    return Objects.equals(this.success, activateOpticalCamo200Response.success) &&
        Objects.equals(this.activeDuration, activateOpticalCamo200Response.activeDuration) &&
        Objects.equals(this.energyCost, activateOpticalCamo200Response.energyCost) &&
        Objects.equals(this.visibilityReduction, activateOpticalCamo200Response.visibilityReduction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, activeDuration, energyCost, visibilityReduction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivateOpticalCamo200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    activeDuration: ").append(toIndentedString(activeDuration)).append("\n");
    sb.append("    energyCost: ").append(toIndentedString(energyCost)).append("\n");
    sb.append("    visibilityReduction: ").append(toIndentedString(visibilityReduction)).append("\n");
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

