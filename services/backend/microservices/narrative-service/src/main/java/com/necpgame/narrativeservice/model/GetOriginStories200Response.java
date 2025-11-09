package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.OriginStory;
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
 * GetOriginStories200Response
 */

@JsonTypeName("getOriginStories_200_response")

public class GetOriginStories200Response {

  @Valid
  private List<@Valid OriginStory> origins = new ArrayList<>();

  public GetOriginStories200Response origins(List<@Valid OriginStory> origins) {
    this.origins = origins;
    return this;
  }

  public GetOriginStories200Response addOriginsItem(OriginStory originsItem) {
    if (this.origins == null) {
      this.origins = new ArrayList<>();
    }
    this.origins.add(originsItem);
    return this;
  }

  /**
   * Get origins
   * @return origins
   */
  @Valid 
  @Schema(name = "origins", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("origins")
  public List<@Valid OriginStory> getOrigins() {
    return origins;
  }

  public void setOrigins(List<@Valid OriginStory> origins) {
    this.origins = origins;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetOriginStories200Response getOriginStories200Response = (GetOriginStories200Response) o;
    return Objects.equals(this.origins, getOriginStories200Response.origins);
  }

  @Override
  public int hashCode() {
    return Objects.hash(origins);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetOriginStories200Response {\n");
    sb.append("    origins: ").append(toIndentedString(origins)).append("\n");
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

