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
 * PerformSlide200Response
 */

@JsonTypeName("performSlide_200_response")

public class PerformSlide200Response {

  private @Nullable Boolean success;

  private @Nullable BigDecimal distance;

  private @Nullable BigDecimal staminaCost;

  private @Nullable BigDecimal evasionBonus;

  public PerformSlide200Response success(@Nullable Boolean success) {
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

  public PerformSlide200Response distance(@Nullable BigDecimal distance) {
    this.distance = distance;
    return this;
  }

  /**
   * Get distance
   * @return distance
   */
  @Valid 
  @Schema(name = "distance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distance")
  public @Nullable BigDecimal getDistance() {
    return distance;
  }

  public void setDistance(@Nullable BigDecimal distance) {
    this.distance = distance;
  }

  public PerformSlide200Response staminaCost(@Nullable BigDecimal staminaCost) {
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

  public PerformSlide200Response evasionBonus(@Nullable BigDecimal evasionBonus) {
    this.evasionBonus = evasionBonus;
    return this;
  }

  /**
   * Бонус к уклонению во время скольжения
   * @return evasionBonus
   */
  @Valid 
  @Schema(name = "evasion_bonus", description = "Бонус к уклонению во время скольжения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evasion_bonus")
  public @Nullable BigDecimal getEvasionBonus() {
    return evasionBonus;
  }

  public void setEvasionBonus(@Nullable BigDecimal evasionBonus) {
    this.evasionBonus = evasionBonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformSlide200Response performSlide200Response = (PerformSlide200Response) o;
    return Objects.equals(this.success, performSlide200Response.success) &&
        Objects.equals(this.distance, performSlide200Response.distance) &&
        Objects.equals(this.staminaCost, performSlide200Response.staminaCost) &&
        Objects.equals(this.evasionBonus, performSlide200Response.evasionBonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, distance, staminaCost, evasionBonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformSlide200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    distance: ").append(toIndentedString(distance)).append("\n");
    sb.append("    staminaCost: ").append(toIndentedString(staminaCost)).append("\n");
    sb.append("    evasionBonus: ").append(toIndentedString(evasionBonus)).append("\n");
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

