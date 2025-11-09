package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.HireableNPC;
import com.necpgame.backjava.model.PaginationMeta;
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
 * GetAvailableNPCsForHire200Response
 */

@JsonTypeName("getAvailableNPCsForHire_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetAvailableNPCsForHire200Response {

  @Valid
  private List<@Valid HireableNPC> data = new ArrayList<>();

  private PaginationMeta meta;

  public GetAvailableNPCsForHire200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetAvailableNPCsForHire200Response(List<@Valid HireableNPC> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public GetAvailableNPCsForHire200Response data(List<@Valid HireableNPC> data) {
    this.data = data;
    return this;
  }

  public GetAvailableNPCsForHire200Response addDataItem(HireableNPC dataItem) {
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
  public List<@Valid HireableNPC> getData() {
    return data;
  }

  public void setData(List<@Valid HireableNPC> data) {
    this.data = data;
  }

  public GetAvailableNPCsForHire200Response meta(PaginationMeta meta) {
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
    GetAvailableNPCsForHire200Response getAvailableNPCsForHire200Response = (GetAvailableNPCsForHire200Response) o;
    return Objects.equals(this.data, getAvailableNPCsForHire200Response.data) &&
        Objects.equals(this.meta, getAvailableNPCsForHire200Response.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableNPCsForHire200Response {\n");
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

