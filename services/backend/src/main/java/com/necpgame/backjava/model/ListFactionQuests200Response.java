package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.FactionQuest;
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
 * ListFactionQuests200Response
 */

@JsonTypeName("listFactionQuests_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ListFactionQuests200Response {

  @Valid
  private List<@Valid FactionQuest> data = new ArrayList<>();

  private PaginationMeta meta;

  public ListFactionQuests200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ListFactionQuests200Response(List<@Valid FactionQuest> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public ListFactionQuests200Response data(List<@Valid FactionQuest> data) {
    this.data = data;
    return this;
  }

  public ListFactionQuests200Response addDataItem(FactionQuest dataItem) {
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
  public List<@Valid FactionQuest> getData() {
    return data;
  }

  public void setData(List<@Valid FactionQuest> data) {
    this.data = data;
  }

  public ListFactionQuests200Response meta(PaginationMeta meta) {
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
    ListFactionQuests200Response listFactionQuests200Response = (ListFactionQuests200Response) o;
    return Objects.equals(this.data, listFactionQuests200Response.data) &&
        Objects.equals(this.meta, listFactionQuests200Response.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListFactionQuests200Response {\n");
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

