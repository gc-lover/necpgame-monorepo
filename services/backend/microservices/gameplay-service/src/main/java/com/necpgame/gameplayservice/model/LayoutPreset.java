package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.FurniturePlacement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * LayoutPreset
 */


public class LayoutPreset {

  private String presetId;

  private String name;

  @Valid
  private List<@Valid FurniturePlacement> placements = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  public LayoutPreset() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LayoutPreset(String presetId, String name, List<@Valid FurniturePlacement> placements) {
    this.presetId = presetId;
    this.name = name;
    this.placements = placements;
  }

  public LayoutPreset presetId(String presetId) {
    this.presetId = presetId;
    return this;
  }

  /**
   * Get presetId
   * @return presetId
   */
  @NotNull 
  @Schema(name = "presetId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("presetId")
  public String getPresetId() {
    return presetId;
  }

  public void setPresetId(String presetId) {
    this.presetId = presetId;
  }

  public LayoutPreset name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LayoutPreset placements(List<@Valid FurniturePlacement> placements) {
    this.placements = placements;
    return this;
  }

  public LayoutPreset addPlacementsItem(FurniturePlacement placementsItem) {
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
  @NotNull @Valid 
  @Schema(name = "placements", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("placements")
  public List<@Valid FurniturePlacement> getPlacements() {
    return placements;
  }

  public void setPlacements(List<@Valid FurniturePlacement> placements) {
    this.placements = placements;
  }

  public LayoutPreset lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "lastUpdated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdated")
  public @Nullable OffsetDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LayoutPreset layoutPreset = (LayoutPreset) o;
    return Objects.equals(this.presetId, layoutPreset.presetId) &&
        Objects.equals(this.name, layoutPreset.name) &&
        Objects.equals(this.placements, layoutPreset.placements) &&
        Objects.equals(this.lastUpdated, layoutPreset.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(presetId, name, placements, lastUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LayoutPreset {\n");
    sb.append("    presetId: ").append(toIndentedString(presetId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    placements: ").append(toIndentedString(placements)).append("\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
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

