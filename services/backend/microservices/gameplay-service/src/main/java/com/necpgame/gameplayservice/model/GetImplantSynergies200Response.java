package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.ImplantSynergy;
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
 * GetImplantSynergies200Response
 */

@JsonTypeName("getImplantSynergies_200_response")

public class GetImplantSynergies200Response {

  @Valid
  private List<@Valid ImplantSynergy> synergies = new ArrayList<>();

  public GetImplantSynergies200Response synergies(List<@Valid ImplantSynergy> synergies) {
    this.synergies = synergies;
    return this;
  }

  public GetImplantSynergies200Response addSynergiesItem(ImplantSynergy synergiesItem) {
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
  @Valid 
  @Schema(name = "synergies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergies")
  public List<@Valid ImplantSynergy> getSynergies() {
    return synergies;
  }

  public void setSynergies(List<@Valid ImplantSynergy> synergies) {
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
    GetImplantSynergies200Response getImplantSynergies200Response = (GetImplantSynergies200Response) o;
    return Objects.equals(this.synergies, getImplantSynergies200Response.synergies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(synergies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetImplantSynergies200Response {\n");
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

