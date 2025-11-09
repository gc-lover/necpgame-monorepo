package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.CheatReport;
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
 * GetCheatReports200Response
 */

@JsonTypeName("getCheatReports_200_response")

public class GetCheatReports200Response {

  @Valid
  private List<@Valid CheatReport> data = new ArrayList<>();

  private PaginationMeta meta;

  public GetCheatReports200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetCheatReports200Response(List<@Valid CheatReport> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public GetCheatReports200Response data(List<@Valid CheatReport> data) {
    this.data = data;
    return this;
  }

  public GetCheatReports200Response addDataItem(CheatReport dataItem) {
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
  public List<@Valid CheatReport> getData() {
    return data;
  }

  public void setData(List<@Valid CheatReport> data) {
    this.data = data;
  }

  public GetCheatReports200Response meta(PaginationMeta meta) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCheatReports200Response getCheatReports200Response = (GetCheatReports200Response) o;
    return Objects.equals(this.data, getCheatReports200Response.data) &&
        Objects.equals(this.meta, getCheatReports200Response.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCheatReports200Response {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
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

