package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.ShardInfo;
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
 * GetShards200Response
 */

@JsonTypeName("getShards_200_response")

public class GetShards200Response {

  @Valid
  private List<@Valid ShardInfo> shards = new ArrayList<>();

  private @Nullable Integer totalShards;

  public GetShards200Response shards(List<@Valid ShardInfo> shards) {
    this.shards = shards;
    return this;
  }

  public GetShards200Response addShardsItem(ShardInfo shardsItem) {
    if (this.shards == null) {
      this.shards = new ArrayList<>();
    }
    this.shards.add(shardsItem);
    return this;
  }

  /**
   * Get shards
   * @return shards
   */
  @Valid 
  @Schema(name = "shards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shards")
  public List<@Valid ShardInfo> getShards() {
    return shards;
  }

  public void setShards(List<@Valid ShardInfo> shards) {
    this.shards = shards;
  }

  public GetShards200Response totalShards(@Nullable Integer totalShards) {
    this.totalShards = totalShards;
    return this;
  }

  /**
   * Get totalShards
   * @return totalShards
   */
  
  @Schema(name = "total_shards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_shards")
  public @Nullable Integer getTotalShards() {
    return totalShards;
  }

  public void setTotalShards(@Nullable Integer totalShards) {
    this.totalShards = totalShards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetShards200Response getShards200Response = (GetShards200Response) o;
    return Objects.equals(this.shards, getShards200Response.shards) &&
        Objects.equals(this.totalShards, getShards200Response.totalShards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shards, totalShards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetShards200Response {\n");
    sb.append("    shards: ").append(toIndentedString(shards)).append("\n");
    sb.append("    totalShards: ").append(toIndentedString(totalShards)).append("\n");
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

