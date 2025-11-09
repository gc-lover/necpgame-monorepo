package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.PaginationMeta;
import com.necpgame.worldservice.model.WorldEvent;
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
 * GetActiveWorldEvents200Response
 */

@JsonTypeName("getActiveWorldEvents_200_response")

public class GetActiveWorldEvents200Response {

  @Valid
  private List<@Valid WorldEvent> data = new ArrayList<>();

  private PaginationMeta meta;

  private @Nullable String currentEra;

  public GetActiveWorldEvents200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetActiveWorldEvents200Response(List<@Valid WorldEvent> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public GetActiveWorldEvents200Response data(List<@Valid WorldEvent> data) {
    this.data = data;
    return this;
  }

  public GetActiveWorldEvents200Response addDataItem(WorldEvent dataItem) {
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
  public List<@Valid WorldEvent> getData() {
    return data;
  }

  public void setData(List<@Valid WorldEvent> data) {
    this.data = data;
  }

  public GetActiveWorldEvents200Response meta(PaginationMeta meta) {
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

  public GetActiveWorldEvents200Response currentEra(@Nullable String currentEra) {
    this.currentEra = currentEra;
    return this;
  }

  /**
   * Get currentEra
   * @return currentEra
   */
  
  @Schema(name = "current_era", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_era")
  public @Nullable String getCurrentEra() {
    return currentEra;
  }

  public void setCurrentEra(@Nullable String currentEra) {
    this.currentEra = currentEra;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveWorldEvents200Response getActiveWorldEvents200Response = (GetActiveWorldEvents200Response) o;
    return Objects.equals(this.data, getActiveWorldEvents200Response.data) &&
        Objects.equals(this.meta, getActiveWorldEvents200Response.meta) &&
        Objects.equals(this.currentEra, getActiveWorldEvents200Response.currentEra);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta, currentEra);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveWorldEvents200Response {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
    sb.append("    currentEra: ").append(toIndentedString(currentEra)).append("\n");
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

