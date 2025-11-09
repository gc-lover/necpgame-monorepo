package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.WeaponSummary;
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
 * GetWeaponsCatalog200Response
 */

@JsonTypeName("getWeaponsCatalog_200_response")

public class GetWeaponsCatalog200Response {

  @Valid
  private List<@Valid WeaponSummary> weapons = new ArrayList<>();

  public GetWeaponsCatalog200Response weapons(List<@Valid WeaponSummary> weapons) {
    this.weapons = weapons;
    return this;
  }

  public GetWeaponsCatalog200Response addWeaponsItem(WeaponSummary weaponsItem) {
    if (this.weapons == null) {
      this.weapons = new ArrayList<>();
    }
    this.weapons.add(weaponsItem);
    return this;
  }

  /**
   * Get weapons
   * @return weapons
   */
  @Valid 
  @Schema(name = "weapons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weapons")
  public List<@Valid WeaponSummary> getWeapons() {
    return weapons;
  }

  public void setWeapons(List<@Valid WeaponSummary> weapons) {
    this.weapons = weapons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetWeaponsCatalog200Response getWeaponsCatalog200Response = (GetWeaponsCatalog200Response) o;
    return Objects.equals(this.weapons, getWeaponsCatalog200Response.weapons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weapons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetWeaponsCatalog200Response {\n");
    sb.append("    weapons: ").append(toIndentedString(weapons)).append("\n");
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

