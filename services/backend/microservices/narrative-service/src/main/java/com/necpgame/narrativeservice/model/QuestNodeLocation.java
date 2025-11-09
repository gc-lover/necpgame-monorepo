package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.QuestNodeLocationCoordinates;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestNodeLocation
 */

@JsonTypeName("QuestNode_location")

public class QuestNodeLocation {

  private @Nullable String region;

  private @Nullable String district;

  private @Nullable QuestNodeLocationCoordinates coordinates;

  public QuestNodeLocation region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public QuestNodeLocation district(@Nullable String district) {
    this.district = district;
    return this;
  }

  /**
   * Get district
   * @return district
   */
  
  @Schema(name = "district", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("district")
  public @Nullable String getDistrict() {
    return district;
  }

  public void setDistrict(@Nullable String district) {
    this.district = district;
  }

  public QuestNodeLocation coordinates(@Nullable QuestNodeLocationCoordinates coordinates) {
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
  public @Nullable QuestNodeLocationCoordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(@Nullable QuestNodeLocationCoordinates coordinates) {
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
    QuestNodeLocation questNodeLocation = (QuestNodeLocation) o;
    return Objects.equals(this.region, questNodeLocation.region) &&
        Objects.equals(this.district, questNodeLocation.district) &&
        Objects.equals(this.coordinates, questNodeLocation.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, district, coordinates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestNodeLocation {\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    district: ").append(toIndentedString(district)).append("\n");
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

