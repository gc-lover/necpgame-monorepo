package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.HireableNPC;
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
 * GetAvailableNPCs200Response
 */

@JsonTypeName("getAvailableNPCs_200_response")

public class GetAvailableNPCs200Response {

  @Valid
  private List<@Valid HireableNPC> availableNpcs = new ArrayList<>();

  public GetAvailableNPCs200Response availableNpcs(List<@Valid HireableNPC> availableNpcs) {
    this.availableNpcs = availableNpcs;
    return this;
  }

  public GetAvailableNPCs200Response addAvailableNpcsItem(HireableNPC availableNpcsItem) {
    if (this.availableNpcs == null) {
      this.availableNpcs = new ArrayList<>();
    }
    this.availableNpcs.add(availableNpcsItem);
    return this;
  }

  /**
   * Get availableNpcs
   * @return availableNpcs
   */
  @Valid 
  @Schema(name = "available_npcs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_npcs")
  public List<@Valid HireableNPC> getAvailableNpcs() {
    return availableNpcs;
  }

  public void setAvailableNpcs(List<@Valid HireableNPC> availableNpcs) {
    this.availableNpcs = availableNpcs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableNPCs200Response getAvailableNPCs200Response = (GetAvailableNPCs200Response) o;
    return Objects.equals(this.availableNpcs, getAvailableNPCs200Response.availableNpcs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(availableNpcs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableNPCs200Response {\n");
    sb.append("    availableNpcs: ").append(toIndentedString(availableNpcs)).append("\n");
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

