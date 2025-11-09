package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.authservice.model.City;
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
 * GetCities200Response
 */

@JsonTypeName("getCities_200_response")

public class GetCities200Response {

  @Valid
  private List<@Valid City> cities = new ArrayList<>();

  public GetCities200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetCities200Response(List<@Valid City> cities) {
    this.cities = cities;
  }

  public GetCities200Response cities(List<@Valid City> cities) {
    this.cities = cities;
    return this;
  }

  public GetCities200Response addCitiesItem(City citiesItem) {
    if (this.cities == null) {
      this.cities = new ArrayList<>();
    }
    this.cities.add(citiesItem);
    return this;
  }

  /**
   * Список доступных городов
   * @return cities
   */
  @NotNull @Valid 
  @Schema(name = "cities", description = "Список доступных городов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cities")
  public List<@Valid City> getCities() {
    return cities;
  }

  public void setCities(List<@Valid City> cities) {
    this.cities = cities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCities200Response getCities200Response = (GetCities200Response) o;
    return Objects.equals(this.cities, getCities200Response.cities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCities200Response {\n");
    sb.append("    cities: ").append(toIndentedString(cities)).append("\n");
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

