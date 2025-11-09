package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.FurniturePlacement;
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
 * ApartmentLayoutState
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ApartmentLayoutState {

  private @Nullable String appliedPresetId;

  @Valid
  private List<@Valid FurniturePlacement> placements = new ArrayList<>();

  public ApartmentLayoutState appliedPresetId(@Nullable String appliedPresetId) {
    this.appliedPresetId = appliedPresetId;
    return this;
  }

  /**
   * Get appliedPresetId
   * @return appliedPresetId
   */
  
  @Schema(name = "appliedPresetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliedPresetId")
  public @Nullable String getAppliedPresetId() {
    return appliedPresetId;
  }

  public void setAppliedPresetId(@Nullable String appliedPresetId) {
    this.appliedPresetId = appliedPresetId;
  }

  public ApartmentLayoutState placements(List<@Valid FurniturePlacement> placements) {
    this.placements = placements;
    return this;
  }

  public ApartmentLayoutState addPlacementsItem(FurniturePlacement placementsItem) {
    if (this.placements == null) {
      this.placements = new ArrayList<>();
    }
    this.placements.add(placementsItem);
    return this;
  }

  /**
   * Get placements
   * @return placements
   */
  @Valid 
  @Schema(name = "placements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("placements")
  public List<@Valid FurniturePlacement> getPlacements() {
    return placements;
  }

  public void setPlacements(List<@Valid FurniturePlacement> placements) {
    this.placements = placements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentLayoutState apartmentLayoutState = (ApartmentLayoutState) o;
    return Objects.equals(this.appliedPresetId, apartmentLayoutState.appliedPresetId) &&
        Objects.equals(this.placements, apartmentLayoutState.placements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appliedPresetId, placements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentLayoutState {\n");
    sb.append("    appliedPresetId: ").append(toIndentedString(appliedPresetId)).append("\n");
    sb.append("    placements: ").append(toIndentedString(placements)).append("\n");
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

