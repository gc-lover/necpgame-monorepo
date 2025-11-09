package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.StockIndex;
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
 * GetStockIndices200Response
 */

@JsonTypeName("getStockIndices_200_response")

public class GetStockIndices200Response {

  @Valid
  private List<@Valid StockIndex> indices = new ArrayList<>();

  public GetStockIndices200Response indices(List<@Valid StockIndex> indices) {
    this.indices = indices;
    return this;
  }

  public GetStockIndices200Response addIndicesItem(StockIndex indicesItem) {
    if (this.indices == null) {
      this.indices = new ArrayList<>();
    }
    this.indices.add(indicesItem);
    return this;
  }

  /**
   * Get indices
   * @return indices
   */
  @Valid 
  @Schema(name = "indices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indices")
  public List<@Valid StockIndex> getIndices() {
    return indices;
  }

  public void setIndices(List<@Valid StockIndex> indices) {
    this.indices = indices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStockIndices200Response getStockIndices200Response = (GetStockIndices200Response) o;
    return Objects.equals(this.indices, getStockIndices200Response.indices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStockIndices200Response {\n");
    sb.append("    indices: ").append(toIndentedString(indices)).append("\n");
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

