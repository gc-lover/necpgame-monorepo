package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.TechnologyAccess;
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
 * GetTechnologyAccess200Response
 */

@JsonTypeName("getTechnologyAccess_200_response")

public class GetTechnologyAccess200Response {

  @Valid
  private List<@Valid TechnologyAccess> availableTech = new ArrayList<>();

  public GetTechnologyAccess200Response availableTech(List<@Valid TechnologyAccess> availableTech) {
    this.availableTech = availableTech;
    return this;
  }

  public GetTechnologyAccess200Response addAvailableTechItem(TechnologyAccess availableTechItem) {
    if (this.availableTech == null) {
      this.availableTech = new ArrayList<>();
    }
    this.availableTech.add(availableTechItem);
    return this;
  }

  /**
   * Get availableTech
   * @return availableTech
   */
  @Valid 
  @Schema(name = "available_tech", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_tech")
  public List<@Valid TechnologyAccess> getAvailableTech() {
    return availableTech;
  }

  public void setAvailableTech(List<@Valid TechnologyAccess> availableTech) {
    this.availableTech = availableTech;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetTechnologyAccess200Response getTechnologyAccess200Response = (GetTechnologyAccess200Response) o;
    return Objects.equals(this.availableTech, getTechnologyAccess200Response.availableTech);
  }

  @Override
  public int hashCode() {
    return Objects.hash(availableTech);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTechnologyAccess200Response {\n");
    sb.append("    availableTech: ").append(toIndentedString(availableTech)).append("\n");
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

