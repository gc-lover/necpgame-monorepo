package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.Resource;
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
 * GetResources200Response
 */

@JsonTypeName("getResources_200_response")

public class GetResources200Response {

  @Valid
  private List<@Valid Resource> resources = new ArrayList<>();

  public GetResources200Response resources(List<@Valid Resource> resources) {
    this.resources = resources;
    return this;
  }

  public GetResources200Response addResourcesItem(Resource resourcesItem) {
    if (this.resources == null) {
      this.resources = new ArrayList<>();
    }
    this.resources.add(resourcesItem);
    return this;
  }

  /**
   * Get resources
   * @return resources
   */
  @Valid 
  @Schema(name = "resources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resources")
  public List<@Valid Resource> getResources() {
    return resources;
  }

  public void setResources(List<@Valid Resource> resources) {
    this.resources = resources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetResources200Response getResources200Response = (GetResources200Response) o;
    return Objects.equals(this.resources, getResources200Response.resources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetResources200Response {\n");
    sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
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

