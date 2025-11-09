package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.PlayerOrderPenalty;
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
 * PlayerOrderPenaltyListResponse
 */


public class PlayerOrderPenaltyListResponse {

  @Valid
  private List<@Valid PlayerOrderPenalty> data = new ArrayList<>();

  public PlayerOrderPenaltyListResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderPenaltyListResponse(List<@Valid PlayerOrderPenalty> data) {
    this.data = data;
  }

  public PlayerOrderPenaltyListResponse data(List<@Valid PlayerOrderPenalty> data) {
    this.data = data;
    return this;
  }

  public PlayerOrderPenaltyListResponse addDataItem(PlayerOrderPenalty dataItem) {
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
  public List<@Valid PlayerOrderPenalty> getData() {
    return data;
  }

  public void setData(List<@Valid PlayerOrderPenalty> data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderPenaltyListResponse playerOrderPenaltyListResponse = (PlayerOrderPenaltyListResponse) o;
    return Objects.equals(this.data, playerOrderPenaltyListResponse.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderPenaltyListResponse {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

