package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.DistrictDetailed;
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
 * GetNightCityDistricts200Response
 */

@JsonTypeName("getNightCityDistricts_200_response")

public class GetNightCityDistricts200Response {

  @Valid
  private List<@Valid DistrictDetailed> districts = new ArrayList<>();

  public GetNightCityDistricts200Response districts(List<@Valid DistrictDetailed> districts) {
    this.districts = districts;
    return this;
  }

  public GetNightCityDistricts200Response addDistrictsItem(DistrictDetailed districtsItem) {
    if (this.districts == null) {
      this.districts = new ArrayList<>();
    }
    this.districts.add(districtsItem);
    return this;
  }

  /**
   * Get districts
   * @return districts
   */
  @Valid 
  @Schema(name = "districts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districts")
  public List<@Valid DistrictDetailed> getDistricts() {
    return districts;
  }

  public void setDistricts(List<@Valid DistrictDetailed> districts) {
    this.districts = districts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNightCityDistricts200Response getNightCityDistricts200Response = (GetNightCityDistricts200Response) o;
    return Objects.equals(this.districts, getNightCityDistricts200Response.districts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNightCityDistricts200Response {\n");
    sb.append("    districts: ").append(toIndentedString(districts)).append("\n");
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

