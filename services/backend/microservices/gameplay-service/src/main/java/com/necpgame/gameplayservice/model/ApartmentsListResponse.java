package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Apartment;
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
 * ApartmentsListResponse
 */


public class ApartmentsListResponse {

  @Valid
  private List<@Valid Apartment> apartments = new ArrayList<>();

  public ApartmentsListResponse apartments(List<@Valid Apartment> apartments) {
    this.apartments = apartments;
    return this;
  }

  public ApartmentsListResponse addApartmentsItem(Apartment apartmentsItem) {
    if (this.apartments == null) {
      this.apartments = new ArrayList<>();
    }
    this.apartments.add(apartmentsItem);
    return this;
  }

  /**
   * Get apartments
   * @return apartments
   */
  @Valid 
  @Schema(name = "apartments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("apartments")
  public List<@Valid Apartment> getApartments() {
    return apartments;
  }

  public void setApartments(List<@Valid Apartment> apartments) {
    this.apartments = apartments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentsListResponse apartmentsListResponse = (ApartmentsListResponse) o;
    return Objects.equals(this.apartments, apartmentsListResponse.apartments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apartments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentsListResponse {\n");
    sb.append("    apartments: ").append(toIndentedString(apartments)).append("\n");
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

