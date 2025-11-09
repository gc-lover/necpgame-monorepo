package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LocationDetailsAllOfPointsOfInterest
 */

@JsonTypeName("LocationDetails_allOf_pointsOfInterest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:04.712198900+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class LocationDetailsAllOfPointsOfInterest {

  private String id;

  private String name;

  private String description;

  public LocationDetailsAllOfPointsOfInterest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LocationDetailsAllOfPointsOfInterest(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public LocationDetailsAllOfPointsOfInterest id(String id) {
    this.id = id;
    return this;
  }

  /**
   * ID С‚РѕС‡РєРё РёРЅС‚РµСЂРµСЃР°
   * @return id
   */
  @NotNull 
  @Schema(name = "id", description = "ID С‚РѕС‡РєРё РёРЅС‚РµСЂРµСЃР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocationDetailsAllOfPointsOfInterest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * РќР°Р·РІР°РЅРёРµ С‚РѕС‡РєРё РёРЅС‚РµСЂРµСЃР°
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "РќР°Р·РІР°РЅРёРµ С‚РѕС‡РєРё РёРЅС‚РµСЂРµСЃР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocationDetailsAllOfPointsOfInterest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * РћРїРёСЃР°РЅРёРµ С‚РѕС‡РєРё РёРЅС‚РµСЂРµСЃР°
   * @return description
   */
  @NotNull 
  @Schema(name = "description", description = "РћРїРёСЃР°РЅРёРµ С‚РѕС‡РєРё РёРЅС‚РµСЂРµСЃР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDetailsAllOfPointsOfInterest locationDetailsAllOfPointsOfInterest = (LocationDetailsAllOfPointsOfInterest) o;
    return Objects.equals(this.id, locationDetailsAllOfPointsOfInterest.id) &&
        Objects.equals(this.name, locationDetailsAllOfPointsOfInterest.name) &&
        Objects.equals(this.description, locationDetailsAllOfPointsOfInterest.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationDetailsAllOfPointsOfInterest {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

