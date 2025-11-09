package com.necpgame.authservice.model;

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
 * City
 */


public class City {

  private UUID id;

  private String name;

  private String region;

  private String description;

  @Valid
  private List<UUID> availableForFactions = new ArrayList<>();

  public City() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public City(UUID id, String name, String region, String description, List<UUID> availableForFactions) {
    this.id = id;
    this.name = name;
    this.region = region;
    this.description = description;
    this.availableForFactions = availableForFactions;
  }

  public City id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор города
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", example = "550e8400-e29b-41d4-a716-446655440000", description = "Уникальный идентификатор города", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public City name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название города
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Night City", description = "Название города", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public City region(String region) {
    this.region = region;
    return this;
  }

  /**
   * Регион/сервер
   * @return region
   */
  @NotNull 
  @Schema(name = "region", example = "EU", description = "Регион/сервер", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public City description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание города
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Главный город, множество возможностей", description = "Описание города", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public City availableForFactions(List<UUID> availableForFactions) {
    this.availableForFactions = availableForFactions;
    return this;
  }

  public City addAvailableForFactionsItem(UUID availableForFactionsItem) {
    if (this.availableForFactions == null) {
      this.availableForFactions = new ArrayList<>();
    }
    this.availableForFactions.add(availableForFactionsItem);
    return this;
  }

  /**
   * Список доступных фракций для старта (UUID)
   * @return availableForFactions
   */
  @NotNull @Valid 
  @Schema(name = "available_for_factions", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]", description = "Список доступных фракций для старта (UUID)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("available_for_factions")
  public List<UUID> getAvailableForFactions() {
    return availableForFactions;
  }

  public void setAvailableForFactions(List<UUID> availableForFactions) {
    this.availableForFactions = availableForFactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    City city = (City) o;
    return Objects.equals(this.id, city.id) &&
        Objects.equals(this.name, city.name) &&
        Objects.equals(this.region, city.region) &&
        Objects.equals(this.description, city.description) &&
        Objects.equals(this.availableForFactions, city.availableForFactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, region, description, availableForFactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class City {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    availableForFactions: ").append(toIndentedString(availableForFactions)).append("\n");
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

