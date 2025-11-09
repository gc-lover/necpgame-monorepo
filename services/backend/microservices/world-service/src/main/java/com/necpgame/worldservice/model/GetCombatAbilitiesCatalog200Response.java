package com.necpgame.worldservice.model;

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
 * GetCombatAbilitiesCatalog200Response
 */

@JsonTypeName("getCombatAbilitiesCatalog_200_response")

public class GetCombatAbilitiesCatalog200Response {

  @Valid
  private List<Object> abilities = new ArrayList<>();

  public GetCombatAbilitiesCatalog200Response abilities(List<Object> abilities) {
    this.abilities = abilities;
    return this;
  }

  public GetCombatAbilitiesCatalog200Response addAbilitiesItem(Object abilitiesItem) {
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
  
  @Schema(name = "abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities")
  public List<Object> getAbilities() {
    return abilities;
  }

  public void setAbilities(List<Object> abilities) {
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
    GetCombatAbilitiesCatalog200Response getCombatAbilitiesCatalog200Response = (GetCombatAbilitiesCatalog200Response) o;
    return Objects.equals(this.abilities, getCombatAbilitiesCatalog200Response.abilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCombatAbilitiesCatalog200Response {\n");
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

