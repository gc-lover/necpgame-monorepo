package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PaginationMeta;
import com.necpgame.socialservice.model.ScheduleRecalcJob;
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
 * ScheduleJobListResponse
 */


public class ScheduleJobListResponse {

  @Valid
  private List<@Valid ScheduleRecalcJob> data = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public ScheduleJobListResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleJobListResponse(List<@Valid ScheduleRecalcJob> data) {
    this.data = data;
  }

  public ScheduleJobListResponse data(List<@Valid ScheduleRecalcJob> data) {
    this.data = data;
    return this;
  }

  public ScheduleJobListResponse addDataItem(ScheduleRecalcJob dataItem) {
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
  public List<@Valid ScheduleRecalcJob> getData() {
    return data;
  }

  public void setData(List<@Valid ScheduleRecalcJob> data) {
    this.data = data;
  }

  public ScheduleJobListResponse pagination(@Nullable PaginationMeta pagination) {
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
    ScheduleJobListResponse scheduleJobListResponse = (ScheduleJobListResponse) o;
    return Objects.equals(this.data, scheduleJobListResponse.data) &&
        Objects.equals(this.pagination, scheduleJobListResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleJobListResponse {\n");
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

