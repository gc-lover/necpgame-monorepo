package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.TradingHub;
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
 * GetTradingHubs200Response
 */

@JsonTypeName("getTradingHubs_200_response")

public class GetTradingHubs200Response {

  @Valid
  private List<@Valid TradingHub> hubs = new ArrayList<>();

  public GetTradingHubs200Response hubs(List<@Valid TradingHub> hubs) {
    this.hubs = hubs;
    return this;
  }

  public GetTradingHubs200Response addHubsItem(TradingHub hubsItem) {
    if (this.hubs == null) {
      this.hubs = new ArrayList<>();
    }
    this.hubs.add(hubsItem);
    return this;
  }

  /**
   * Get hubs
   * @return hubs
   */
  @Valid 
  @Schema(name = "hubs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hubs")
  public List<@Valid TradingHub> getHubs() {
    return hubs;
  }

  public void setHubs(List<@Valid TradingHub> hubs) {
    this.hubs = hubs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetTradingHubs200Response getTradingHubs200Response = (GetTradingHubs200Response) o;
    return Objects.equals(this.hubs, getTradingHubs200Response.hubs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hubs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTradingHubs200Response {\n");
    sb.append("    hubs: ").append(toIndentedString(hubs)).append("\n");
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

