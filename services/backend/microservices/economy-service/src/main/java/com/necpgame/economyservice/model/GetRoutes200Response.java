package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.Route;
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
 * GetRoutes200Response
 */

@JsonTypeName("getRoutes_200_response")

public class GetRoutes200Response {

  @Valid
  private List<@Valid Route> routes = new ArrayList<>();

  public GetRoutes200Response routes(List<@Valid Route> routes) {
    this.routes = routes;
    return this;
  }

  public GetRoutes200Response addRoutesItem(Route routesItem) {
    if (this.routes == null) {
      this.routes = new ArrayList<>();
    }
    this.routes.add(routesItem);
    return this;
  }

  /**
   * Get routes
   * @return routes
   */
  @Valid 
  @Schema(name = "routes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routes")
  public List<@Valid Route> getRoutes() {
    return routes;
  }

  public void setRoutes(List<@Valid Route> routes) {
    this.routes = routes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRoutes200Response getRoutes200Response = (GetRoutes200Response) o;
    return Objects.equals(this.routes, getRoutes200Response.routes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRoutes200Response {\n");
    sb.append("    routes: ").append(toIndentedString(routes)).append("\n");
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

