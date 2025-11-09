package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.DistrictPopulationState;
import com.necpgame.worldservice.model.PaginationMeta;
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
 * DistrictPopulationListResponse
 */


public class DistrictPopulationListResponse {

  private UUID cityId;

  @Valid
  private List<@Valid DistrictPopulationState> data = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public DistrictPopulationListResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DistrictPopulationListResponse(UUID cityId, List<@Valid DistrictPopulationState> data) {
    this.cityId = cityId;
    this.data = data;
  }

  public DistrictPopulationListResponse cityId(UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @NotNull @Valid 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cityId")
  public UUID getCityId() {
    return cityId;
  }

  public void setCityId(UUID cityId) {
    this.cityId = cityId;
  }

  public DistrictPopulationListResponse data(List<@Valid DistrictPopulationState> data) {
    this.data = data;
    return this;
  }

  public DistrictPopulationListResponse addDataItem(DistrictPopulationState dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid DistrictPopulationState> getData() {
    return data;
  }

  public void setData(List<@Valid DistrictPopulationState> data) {
    this.data = data;
  }

  public DistrictPopulationListResponse pagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagination")
  public @Nullable PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistrictPopulationListResponse districtPopulationListResponse = (DistrictPopulationListResponse) o;
    return Objects.equals(this.cityId, districtPopulationListResponse.cityId) &&
        Objects.equals(this.data, districtPopulationListResponse.data) &&
        Objects.equals(this.pagination, districtPopulationListResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, data, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistrictPopulationListResponse {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
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

