package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ImplantDurability
 */


public class ImplantDurability {

  private @Nullable String implantId;

  private @Nullable BigDecimal durability;

  private @Nullable BigDecimal efficiency;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PERFECT("perfect"),
    
    GOOD("good"),
    
    DEGRADED("degraded"),
    
    CRITICAL("critical"),
    
    BROKEN("broken");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Boolean repairRequired;

  private @Nullable BigDecimal estimatedFailure;

  public ImplantDurability implantId(@Nullable String implantId) {
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

  public ImplantDurability durability(@Nullable BigDecimal durability) {
    this.durability = durability;
    return this;
  }

  /**
   * Долговечность (%)
   * minimum: 0
   * maximum: 100
   * @return durability
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "durability", description = "Долговечность (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durability")
  public @Nullable BigDecimal getDurability() {
    return durability;
  }

  public void setDurability(@Nullable BigDecimal durability) {
    this.durability = durability;
  }

  public ImplantDurability efficiency(@Nullable BigDecimal efficiency) {
    this.efficiency = efficiency;
    return this;
  }

  /**
   * Эффективность (%)
   * minimum: 0
   * maximum: 100
   * @return efficiency
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "efficiency", description = "Эффективность (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("efficiency")
  public @Nullable BigDecimal getEfficiency() {
    return efficiency;
  }

  public void setEfficiency(@Nullable BigDecimal efficiency) {
    this.efficiency = efficiency;
  }

  public ImplantDurability status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ImplantDurability repairRequired(@Nullable Boolean repairRequired) {
    this.repairRequired = repairRequired;
    return this;
  }

  /**
   * Get repairRequired
   * @return repairRequired
   */
  
  @Schema(name = "repair_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("repair_required")
  public @Nullable Boolean getRepairRequired() {
    return repairRequired;
  }

  public void setRepairRequired(@Nullable Boolean repairRequired) {
    this.repairRequired = repairRequired;
  }

  public ImplantDurability estimatedFailure(@Nullable BigDecimal estimatedFailure) {
    this.estimatedFailure = estimatedFailure;
    return this;
  }

  /**
   * Оценка до поломки (часов использования)
   * @return estimatedFailure
   */
  @Valid 
  @Schema(name = "estimated_failure", description = "Оценка до поломки (часов использования)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_failure")
  public @Nullable BigDecimal getEstimatedFailure() {
    return estimatedFailure;
  }

  public void setEstimatedFailure(@Nullable BigDecimal estimatedFailure) {
    this.estimatedFailure = estimatedFailure;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantDurability implantDurability = (ImplantDurability) o;
    return Objects.equals(this.implantId, implantDurability.implantId) &&
        Objects.equals(this.durability, implantDurability.durability) &&
        Objects.equals(this.efficiency, implantDurability.efficiency) &&
        Objects.equals(this.status, implantDurability.status) &&
        Objects.equals(this.repairRequired, implantDurability.repairRequired) &&
        Objects.equals(this.estimatedFailure, implantDurability.estimatedFailure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, durability, efficiency, status, repairRequired, estimatedFailure);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantDurability {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    durability: ").append(toIndentedString(durability)).append("\n");
    sb.append("    efficiency: ").append(toIndentedString(efficiency)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    repairRequired: ").append(toIndentedString(repairRequired)).append("\n");
    sb.append("    estimatedFailure: ").append(toIndentedString(estimatedFailure)).append("\n");
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

