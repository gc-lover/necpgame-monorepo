package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ConnectedLocationRequirements;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ConnectedLocation
 */


public class ConnectedLocation {

  private String id;

  private String name;

  private String distance;

  private Integer travelTime;

  private Boolean accessible;

  private @Nullable ConnectedLocationRequirements requirements;

  private @Nullable Boolean fastTravelAvailable;

  public ConnectedLocation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ConnectedLocation(String id, String name, String distance, Integer travelTime, Boolean accessible) {
    this.id = id;
    this.name = name;
    this.distance = distance;
    this.travelTime = travelTime;
    this.accessible = accessible;
  }

  public ConnectedLocation id(String id) {
    this.id = id;
    return this;
  }

  /**
   * ID локации
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "watson_kabuki", description = "ID локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ConnectedLocation name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название локации
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Watson - Kabuki", description = "Название локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ConnectedLocation distance(String distance) {
    this.distance = distance;
    return this;
  }

  /**
   * Расстояние до локации
   * @return distance
   */
  @NotNull 
  @Schema(name = "distance", example = "2.5 km", description = "Расстояние до локации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("distance")
  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

  public ConnectedLocation travelTime(Integer travelTime) {
    this.travelTime = travelTime;
    return this;
  }

  /**
   * Время пути пешком в минутах
   * @return travelTime
   */
  @NotNull 
  @Schema(name = "travelTime", example = "30", description = "Время пути пешком в минутах", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("travelTime")
  public Integer getTravelTime() {
    return travelTime;
  }

  public void setTravelTime(Integer travelTime) {
    this.travelTime = travelTime;
  }

  public ConnectedLocation accessible(Boolean accessible) {
    this.accessible = accessible;
    return this;
  }

  /**
   * Доступна ли локация для персонажа
   * @return accessible
   */
  @NotNull 
  @Schema(name = "accessible", example = "true", description = "Доступна ли локация для персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accessible")
  public Boolean getAccessible() {
    return accessible;
  }

  public void setAccessible(Boolean accessible) {
    this.accessible = accessible;
  }

  public ConnectedLocation requirements(@Nullable ConnectedLocationRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable ConnectedLocationRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable ConnectedLocationRequirements requirements) {
    this.requirements = requirements;
  }

  public ConnectedLocation fastTravelAvailable(@Nullable Boolean fastTravelAvailable) {
    this.fastTravelAvailable = fastTravelAvailable;
    return this;
  }

  /**
   * Доступно ли быстрое перемещение
   * @return fastTravelAvailable
   */
  
  @Schema(name = "fastTravelAvailable", example = "false", description = "Доступно ли быстрое перемещение", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fastTravelAvailable")
  public @Nullable Boolean getFastTravelAvailable() {
    return fastTravelAvailable;
  }

  public void setFastTravelAvailable(@Nullable Boolean fastTravelAvailable) {
    this.fastTravelAvailable = fastTravelAvailable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnectedLocation connectedLocation = (ConnectedLocation) o;
    return Objects.equals(this.id, connectedLocation.id) &&
        Objects.equals(this.name, connectedLocation.name) &&
        Objects.equals(this.distance, connectedLocation.distance) &&
        Objects.equals(this.travelTime, connectedLocation.travelTime) &&
        Objects.equals(this.accessible, connectedLocation.accessible) &&
        Objects.equals(this.requirements, connectedLocation.requirements) &&
        Objects.equals(this.fastTravelAvailable, connectedLocation.fastTravelAvailable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, distance, travelTime, accessible, requirements, fastTravelAvailable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnectedLocation {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    distance: ").append(toIndentedString(distance)).append("\n");
    sb.append("    travelTime: ").append(toIndentedString(travelTime)).append("\n");
    sb.append("    accessible: ").append(toIndentedString(accessible)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    fastTravelAvailable: ").append(toIndentedString(fastTravelAvailable)).append("\n");
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

