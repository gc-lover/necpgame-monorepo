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
 * RepairImplant200Response
 */

@JsonTypeName("repairImplant_200_response")

public class RepairImplant200Response {

  private @Nullable Boolean success;

  private @Nullable String implantId;

  private @Nullable BigDecimal durabilityBefore;

  private @Nullable BigDecimal durabilityAfter;

  private @Nullable BigDecimal cost;

  public RepairImplant200Response success(@Nullable Boolean success) {
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

  public RepairImplant200Response implantId(@Nullable String implantId) {
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

  public RepairImplant200Response durabilityBefore(@Nullable BigDecimal durabilityBefore) {
    this.durabilityBefore = durabilityBefore;
    return this;
  }

  /**
   * Get durabilityBefore
   * @return durabilityBefore
   */
  @Valid 
  @Schema(name = "durability_before", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durability_before")
  public @Nullable BigDecimal getDurabilityBefore() {
    return durabilityBefore;
  }

  public void setDurabilityBefore(@Nullable BigDecimal durabilityBefore) {
    this.durabilityBefore = durabilityBefore;
  }

  public RepairImplant200Response durabilityAfter(@Nullable BigDecimal durabilityAfter) {
    this.durabilityAfter = durabilityAfter;
    return this;
  }

  /**
   * Get durabilityAfter
   * @return durabilityAfter
   */
  @Valid 
  @Schema(name = "durability_after", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durability_after")
  public @Nullable BigDecimal getDurabilityAfter() {
    return durabilityAfter;
  }

  public void setDurabilityAfter(@Nullable BigDecimal durabilityAfter) {
    this.durabilityAfter = durabilityAfter;
  }

  public RepairImplant200Response cost(@Nullable BigDecimal cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  @Valid 
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable BigDecimal getCost() {
    return cost;
  }

  public void setCost(@Nullable BigDecimal cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RepairImplant200Response repairImplant200Response = (RepairImplant200Response) o;
    return Objects.equals(this.success, repairImplant200Response.success) &&
        Objects.equals(this.implantId, repairImplant200Response.implantId) &&
        Objects.equals(this.durabilityBefore, repairImplant200Response.durabilityBefore) &&
        Objects.equals(this.durabilityAfter, repairImplant200Response.durabilityAfter) &&
        Objects.equals(this.cost, repairImplant200Response.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, implantId, durabilityBefore, durabilityAfter, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RepairImplant200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    durabilityBefore: ").append(toIndentedString(durabilityBefore)).append("\n");
    sb.append("    durabilityAfter: ").append(toIndentedString(durabilityAfter)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

