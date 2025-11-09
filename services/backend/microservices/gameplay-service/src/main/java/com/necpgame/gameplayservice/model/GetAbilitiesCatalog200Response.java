package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.AbilityDetail;
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
 * GetAbilitiesCatalog200Response
 */

@JsonTypeName("getAbilitiesCatalog_200_response")

public class GetAbilitiesCatalog200Response {

  @Valid
  private List<@Valid AbilityDetail> abilities = new ArrayList<>();

  public GetAbilitiesCatalog200Response abilities(List<@Valid AbilityDetail> abilities) {
    this.abilities = abilities;
    return this;
  }

  public GetAbilitiesCatalog200Response addAbilitiesItem(AbilityDetail abilitiesItem) {
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
  public List<@Valid AbilityDetail> getAbilities() {
    return abilities;
  }

  public void setAbilities(List<@Valid AbilityDetail> abilities) {
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
    GetAbilitiesCatalog200Response getAbilitiesCatalog200Response = (GetAbilitiesCatalog200Response) o;
    return Objects.equals(this.abilities, getAbilitiesCatalog200Response.abilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAbilitiesCatalog200Response {\n");
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

