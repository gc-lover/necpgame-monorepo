package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.ConnectedLocation;
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
 * GetConnectedLocations200Response
 */

@JsonTypeName("getConnectedLocations_200_response")

public class GetConnectedLocations200Response {

  @Valid
  private List<@Valid ConnectedLocation> connectedLocations = new ArrayList<>();

  public GetConnectedLocations200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetConnectedLocations200Response(List<@Valid ConnectedLocation> connectedLocations) {
    this.connectedLocations = connectedLocations;
  }

  public GetConnectedLocations200Response connectedLocations(List<@Valid ConnectedLocation> connectedLocations) {
    this.connectedLocations = connectedLocations;
    return this;
  }

  public GetConnectedLocations200Response addConnectedLocationsItem(ConnectedLocation connectedLocationsItem) {
    if (this.connectedLocations == null) {
      this.connectedLocations = new ArrayList<>();
    }
    this.connectedLocations.add(connectedLocationsItem);
    return this;
  }

  /**
   * Список связанных локаций
   * @return connectedLocations
   */
  @NotNull @Valid 
  @Schema(name = "connectedLocations", description = "Список связанных локаций", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("connectedLocations")
  public List<@Valid ConnectedLocation> getConnectedLocations() {
    return connectedLocations;
  }

  public void setConnectedLocations(List<@Valid ConnectedLocation> connectedLocations) {
    this.connectedLocations = connectedLocations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetConnectedLocations200Response getConnectedLocations200Response = (GetConnectedLocations200Response) o;
    return Objects.equals(this.connectedLocations, getConnectedLocations200Response.connectedLocations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connectedLocations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetConnectedLocations200Response {\n");
    sb.append("    connectedLocations: ").append(toIndentedString(connectedLocations)).append("\n");
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

