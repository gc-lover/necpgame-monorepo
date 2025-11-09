package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.AbilitySynergy;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetAbilitySynergies200Response
 */

@JsonTypeName("getAbilitySynergies_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:49:04.787810800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetAbilitySynergies200Response {

  @Valid
  private List<@Valid AbilitySynergy> synergies = new ArrayList<>();

  public GetAbilitySynergies200Response synergies(List<@Valid AbilitySynergy> synergies) {
    this.synergies = synergies;
    return this;
  }

  public GetAbilitySynergies200Response addSynergiesItem(AbilitySynergy synergiesItem) {
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
  public List<@Valid AbilitySynergy> getSynergies() {
    return synergies;
  }

  public void setSynergies(List<@Valid AbilitySynergy> synergies) {
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
    GetAbilitySynergies200Response getAbilitySynergies200Response = (GetAbilitySynergies200Response) o;
    return Objects.equals(this.synergies, getAbilitySynergies200Response.synergies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(synergies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAbilitySynergies200Response {\n");
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

