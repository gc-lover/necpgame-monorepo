package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.realtimeservice.model.ZoneCellSummary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TechnicalRealtimeZonesZoneIdCellsGet200Response
 */

@JsonTypeName("_technical_realtime_zones__zoneId__cells_get_200_response")

public class TechnicalRealtimeZonesZoneIdCellsGet200Response {

  private @Nullable String zoneId;

  @Valid
  private List<@Valid ZoneCellSummary> cells = new ArrayList<>();

  public TechnicalRealtimeZonesZoneIdCellsGet200Response zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zoneId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zoneId")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public TechnicalRealtimeZonesZoneIdCellsGet200Response cells(List<@Valid ZoneCellSummary> cells) {
    this.cells = cells;
    return this;
  }

  public TechnicalRealtimeZonesZoneIdCellsGet200Response addCellsItem(ZoneCellSummary cellsItem) {
    if (this.cells == null) {
      this.cells = new ArrayList<>();
    }
    this.cells.add(cellsItem);
    return this;
  }

  /**
   * Get cells
   * @return cells
   */
  @Valid 
  @Schema(name = "cells", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cells")
  public List<@Valid ZoneCellSummary> getCells() {
    return cells;
  }

  public void setCells(List<@Valid ZoneCellSummary> cells) {
    this.cells = cells;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalRealtimeZonesZoneIdCellsGet200Response technicalRealtimeZonesZoneIdCellsGet200Response = (TechnicalRealtimeZonesZoneIdCellsGet200Response) o;
    return Objects.equals(this.zoneId, technicalRealtimeZonesZoneIdCellsGet200Response.zoneId) &&
        Objects.equals(this.cells, technicalRealtimeZonesZoneIdCellsGet200Response.cells);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, cells);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalRealtimeZonesZoneIdCellsGet200Response {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    cells: ").append(toIndentedString(cells)).append("\n");
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

