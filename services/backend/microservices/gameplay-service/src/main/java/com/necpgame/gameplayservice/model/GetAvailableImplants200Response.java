package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.AvailableImplant;
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
 * GetAvailableImplants200Response
 */

@JsonTypeName("getAvailableImplants_200_response")

public class GetAvailableImplants200Response {

  @Valid
  private List<@Valid AvailableImplant> implants = new ArrayList<>();

  public GetAvailableImplants200Response implants(List<@Valid AvailableImplant> implants) {
    this.implants = implants;
    return this;
  }

  public GetAvailableImplants200Response addImplantsItem(AvailableImplant implantsItem) {
    if (this.implants == null) {
      this.implants = new ArrayList<>();
    }
    this.implants.add(implantsItem);
    return this;
  }

  /**
   * Get implants
   * @return implants
   */
  @Valid 
  @Schema(name = "implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implants")
  public List<@Valid AvailableImplant> getImplants() {
    return implants;
  }

  public void setImplants(List<@Valid AvailableImplant> implants) {
    this.implants = implants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableImplants200Response getAvailableImplants200Response = (GetAvailableImplants200Response) o;
    return Objects.equals(this.implants, getAvailableImplants200Response.implants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableImplants200Response {\n");
    sb.append("    implants: ").append(toIndentedString(implants)).append("\n");
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

