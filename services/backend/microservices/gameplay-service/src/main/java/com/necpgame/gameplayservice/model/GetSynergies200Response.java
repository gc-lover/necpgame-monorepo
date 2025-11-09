package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Synergy;
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
 * GetSynergies200Response
 */

@JsonTypeName("getSynergies_200_response")

public class GetSynergies200Response {

  @Valid
  private List<@Valid Synergy> synergies = new ArrayList<>();

  public GetSynergies200Response synergies(List<@Valid Synergy> synergies) {
    this.synergies = synergies;
    return this;
  }

  public GetSynergies200Response addSynergiesItem(Synergy synergiesItem) {
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
  public List<@Valid Synergy> getSynergies() {
    return synergies;
  }

  public void setSynergies(List<@Valid Synergy> synergies) {
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
    GetSynergies200Response getSynergies200Response = (GetSynergies200Response) o;
    return Objects.equals(this.synergies, getSynergies200Response.synergies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(synergies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSynergies200Response {\n");
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

