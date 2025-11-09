package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ApartmentUpgradeStateActiveConstruction
 */

@JsonTypeName("ApartmentUpgradeState_activeConstruction")

public class ApartmentUpgradeStateActiveConstruction {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime finishesAt;

  private @Nullable Integer workforceAssigned;

  public ApartmentUpgradeStateActiveConstruction finishesAt(@Nullable OffsetDateTime finishesAt) {
    this.finishesAt = finishesAt;
    return this;
  }

  /**
   * Get finishesAt
   * @return finishesAt
   */
  @Valid 
  @Schema(name = "finishesAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("finishesAt")
  public @Nullable OffsetDateTime getFinishesAt() {
    return finishesAt;
  }

  public void setFinishesAt(@Nullable OffsetDateTime finishesAt) {
    this.finishesAt = finishesAt;
  }

  public ApartmentUpgradeStateActiveConstruction workforceAssigned(@Nullable Integer workforceAssigned) {
    this.workforceAssigned = workforceAssigned;
    return this;
  }

  /**
   * Get workforceAssigned
   * @return workforceAssigned
   */
  
  @Schema(name = "workforceAssigned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("workforceAssigned")
  public @Nullable Integer getWorkforceAssigned() {
    return workforceAssigned;
  }

  public void setWorkforceAssigned(@Nullable Integer workforceAssigned) {
    this.workforceAssigned = workforceAssigned;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentUpgradeStateActiveConstruction apartmentUpgradeStateActiveConstruction = (ApartmentUpgradeStateActiveConstruction) o;
    return Objects.equals(this.finishesAt, apartmentUpgradeStateActiveConstruction.finishesAt) &&
        Objects.equals(this.workforceAssigned, apartmentUpgradeStateActiveConstruction.workforceAssigned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(finishesAt, workforceAssigned);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentUpgradeStateActiveConstruction {\n");
    sb.append("    finishesAt: ").append(toIndentedString(finishesAt)).append("\n");
    sb.append("    workforceAssigned: ").append(toIndentedString(workforceAssigned)).append("\n");
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

