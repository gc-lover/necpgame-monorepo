package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.EndpointInfo;
import com.necpgame.adminservice.model.GetAllEndpoints200ResponseAllOfSummary;
import com.necpgame.adminservice.model.PaginationMeta;
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
 * GetAllEndpoints200Response
 */

@JsonTypeName("getAllEndpoints_200_response")

public class GetAllEndpoints200Response {

  @Valid
  private List<@Valid EndpointInfo> data = new ArrayList<>();

  private PaginationMeta meta;

  private @Nullable GetAllEndpoints200ResponseAllOfSummary summary;

  public GetAllEndpoints200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetAllEndpoints200Response(List<@Valid EndpointInfo> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public GetAllEndpoints200Response data(List<@Valid EndpointInfo> data) {
    this.data = data;
    return this;
  }

  public GetAllEndpoints200Response addDataItem(EndpointInfo dataItem) {
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
  public List<@Valid EndpointInfo> getData() {
    return data;
  }

  public void setData(List<@Valid EndpointInfo> data) {
    this.data = data;
  }

  public GetAllEndpoints200Response meta(PaginationMeta meta) {
    this.meta = meta;
    return this;
  }

  /**
   * Get meta
   * @return meta
   */
  @NotNull @Valid 
  @Schema(name = "meta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("meta")
  public PaginationMeta getMeta() {
    return meta;
  }

  public void setMeta(PaginationMeta meta) {
    this.meta = meta;
  }

  public GetAllEndpoints200Response summary(@Nullable GetAllEndpoints200ResponseAllOfSummary summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @Valid 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public @Nullable GetAllEndpoints200ResponseAllOfSummary getSummary() {
    return summary;
  }

  public void setSummary(@Nullable GetAllEndpoints200ResponseAllOfSummary summary) {
    this.summary = summary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAllEndpoints200Response getAllEndpoints200Response = (GetAllEndpoints200Response) o;
    return Objects.equals(this.data, getAllEndpoints200Response.data) &&
        Objects.equals(this.meta, getAllEndpoints200Response.meta) &&
        Objects.equals(this.summary, getAllEndpoints200Response.summary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta, summary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAllEndpoints200Response {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
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

