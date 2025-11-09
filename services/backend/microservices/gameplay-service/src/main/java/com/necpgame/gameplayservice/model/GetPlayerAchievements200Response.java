package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.PaginationMeta;
import com.necpgame.gameplayservice.model.PlayerAchievement;
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
 * GetPlayerAchievements200Response
 */

@JsonTypeName("getPlayerAchievements_200_response")

public class GetPlayerAchievements200Response {

  @Valid
  private List<@Valid PlayerAchievement> data = new ArrayList<>();

  private PaginationMeta meta;

  public GetPlayerAchievements200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetPlayerAchievements200Response(List<@Valid PlayerAchievement> data, PaginationMeta meta) {
    this.data = data;
    this.meta = meta;
  }

  public GetPlayerAchievements200Response data(List<@Valid PlayerAchievement> data) {
    this.data = data;
    return this;
  }

  public GetPlayerAchievements200Response addDataItem(PlayerAchievement dataItem) {
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
  public List<@Valid PlayerAchievement> getData() {
    return data;
  }

  public void setData(List<@Valid PlayerAchievement> data) {
    this.data = data;
  }

  public GetPlayerAchievements200Response meta(PaginationMeta meta) {
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
    GetPlayerAchievements200Response getPlayerAchievements200Response = (GetPlayerAchievements200Response) o;
    return Objects.equals(this.data, getPlayerAchievements200Response.data) &&
        Objects.equals(this.meta, getPlayerAchievements200Response.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPlayerAchievements200Response {\n");
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

