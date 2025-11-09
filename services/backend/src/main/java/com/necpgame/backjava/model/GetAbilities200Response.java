package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.Ability;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetAbilities200Response
 */

@JsonTypeName("getAbilities_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:49:04.787810800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetAbilities200Response {

  @Valid
  private List<@Valid Ability> abilities = new ArrayList<>();

  public GetAbilities200Response abilities(List<@Valid Ability> abilities) {
    this.abilities = abilities;
    return this;
  }

  public GetAbilities200Response addAbilitiesItem(Ability abilitiesItem) {
    if (this.abilities == null) {
      this.abilities = new ArrayList<>();
    }
    this.abilities.add(abilitiesItem);
    return this;
  }

  /**
   * Get abilities
   * @return abilities
   */
  @Valid 
  @Schema(name = "abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities")
  public List<@Valid Ability> getAbilities() {
    return abilities;
  }

  public void setAbilities(List<@Valid Ability> abilities) {
    this.abilities = abilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAbilities200Response getAbilities200Response = (GetAbilities200Response) o;
    return Objects.equals(this.abilities, getAbilities200Response.abilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAbilities200Response {\n");
    sb.append("    abilities: ").append(toIndentedString(abilities)).append("\n");
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

