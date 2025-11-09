package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.CharacterSummaryCurrentLocationCoordinates;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterSummaryCurrentLocation
 */

@JsonTypeName("CharacterSummary_currentLocation")

public class CharacterSummaryCurrentLocation {

  private @Nullable String zone;

  private @Nullable CharacterSummaryCurrentLocationCoordinates coordinates;

  public CharacterSummaryCurrentLocation zone(@Nullable String zone) {
    this.zone = zone;
    return this;
  }

  /**
   * Get zone
   * @return zone
   */
  
  @Schema(name = "zone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone")
  public @Nullable String getZone() {
    return zone;
  }

  public void setZone(@Nullable String zone) {
    this.zone = zone;
  }

  public CharacterSummaryCurrentLocation coordinates(@Nullable CharacterSummaryCurrentLocationCoordinates coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  /**
   * Get coordinates
   * @return coordinates
   */
  @Valid 
  @Schema(name = "coordinates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("coordinates")
  public @Nullable CharacterSummaryCurrentLocationCoordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(@Nullable CharacterSummaryCurrentLocationCoordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSummaryCurrentLocation characterSummaryCurrentLocation = (CharacterSummaryCurrentLocation) o;
    return Objects.equals(this.zone, characterSummaryCurrentLocation.zone) &&
        Objects.equals(this.coordinates, characterSummaryCurrentLocation.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zone, coordinates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSummaryCurrentLocation {\n");
    sb.append("    zone: ").append(toIndentedString(zone)).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
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

