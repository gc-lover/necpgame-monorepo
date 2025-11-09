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
 * GetResourcesByCategory200Response
 */

@JsonTypeName("getResourcesByCategory_200_response")

public class GetResourcesByCategory200Response {

  private @Nullable String category;

  @Valid
  private List<@Valid Resource> resources = new ArrayList<>();

  public GetResourcesByCategory200Response category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public GetResourcesByCategory200Response resources(List<@Valid Resource> resources) {
    this.resources = resources;
    return this;
  }

  public GetResourcesByCategory200Response addResourcesItem(Resource resourcesItem) {
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
    GetResourcesByCategory200Response getResourcesByCategory200Response = (GetResourcesByCategory200Response) o;
    return Objects.equals(this.category, getResourcesByCategory200Response.category) &&
        Objects.equals(this.resources, getResourcesByCategory200Response.resources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, resources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetResourcesByCategory200Response {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
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

