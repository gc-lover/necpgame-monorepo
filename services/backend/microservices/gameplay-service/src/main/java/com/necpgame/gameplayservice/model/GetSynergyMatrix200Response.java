package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetSynergyMatrix200Response
 */

@JsonTypeName("getSynergyMatrix_200_response")

public class GetSynergyMatrix200Response {

  @Valid
  private List<Object> synergies = new ArrayList<>();

  public GetSynergyMatrix200Response synergies(List<Object> synergies) {
    this.synergies = synergies;
    return this;
  }

  public GetSynergyMatrix200Response addSynergiesItem(Object synergiesItem) {
    if (this.synergies == null) {
      this.synergies = new ArrayList<>();
    }
    this.synergies.add(synergiesItem);
    return this;
  }

  /**
   * Get synergies
   * @return synergies
   */
  
  @Schema(name = "synergies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergies")
  public List<Object> getSynergies() {
    return synergies;
  }

  public void setSynergies(List<Object> synergies) {
    this.synergies = synergies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSynergyMatrix200Response getSynergyMatrix200Response = (GetSynergyMatrix200Response) o;
    return Objects.equals(this.synergies, getSynergyMatrix200Response.synergies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(synergies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSynergyMatrix200Response {\n");
    sb.append("    synergies: ").append(toIndentedString(synergies)).append("\n");
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

