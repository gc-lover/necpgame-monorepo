package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Запрос на расчет энергетического потребления
 */

@Schema(name = "CalculateEnergyRequest", description = "Запрос на расчет энергетического потребления")

public class CalculateEnergyRequest {

  @Valid
  private List<UUID> implantIds = new ArrayList<>();

  public CalculateEnergyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculateEnergyRequest(List<UUID> implantIds) {
    this.implantIds = implantIds;
  }

  public CalculateEnergyRequest implantIds(List<UUID> implantIds) {
    this.implantIds = implantIds;
    return this;
  }

  public CalculateEnergyRequest addImplantIdsItem(UUID implantIdsItem) {
    if (this.implantIds == null) {
      this.implantIds = new ArrayList<>();
    }
    this.implantIds.add(implantIdsItem);
    return this;
  }

  /**
   * Идентификаторы имплантов для расчета
   * @return implantIds
   */
  @NotNull @Valid 
  @Schema(name = "implant_ids", description = "Идентификаторы имплантов для расчета", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_ids")
  public List<UUID> getImplantIds() {
    return implantIds;
  }

  public void setImplantIds(List<UUID> implantIds) {
    this.implantIds = implantIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateEnergyRequest calculateEnergyRequest = (CalculateEnergyRequest) o;
    return Objects.equals(this.implantIds, calculateEnergyRequest.implantIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateEnergyRequest {\n");
    sb.append("    implantIds: ").append(toIndentedString(implantIds)).append("\n");
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

