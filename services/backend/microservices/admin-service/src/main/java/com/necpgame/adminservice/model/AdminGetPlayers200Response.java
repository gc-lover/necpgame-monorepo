package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.AdminPlayerView;
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
 * AdminGetPlayers200Response
 */

@JsonTypeName("adminGetPlayers_200_response")

public class AdminGetPlayers200Response {

  @Valid
  private List<@Valid AdminPlayerView> data = new ArrayList<>();

  private PaginationMeta meta;

  public AdminGetPlayers200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AdminGetPlayers200Response(List<@Valid AdminPlayerView> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public AdminGetPlayers200Response data(List<@Valid AdminPlayerView> data) {
    this.data = data;
    return this;
  }

  public AdminGetPlayers200Response addDataItem(AdminPlayerView dataItem) {
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
  public List<@Valid AdminPlayerView> getData() {
    return data;
  }

  public void setData(List<@Valid AdminPlayerView> data) {
    this.data = data;
  }

  public AdminGetPlayers200Response meta(PaginationMeta meta) {
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
    AdminGetPlayers200Response adminGetPlayers200Response = (AdminGetPlayers200Response) o;
    return Objects.equals(this.data, adminGetPlayers200Response.data) &&
        Objects.equals(this.meta, adminGetPlayers200Response.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminGetPlayers200Response {\n");
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

