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
 * GetWeaponsByClass200Response
 */

@JsonTypeName("getWeaponsByClass_200_response")

public class GetWeaponsByClass200Response {

  private @Nullable String weaponClass;

  @Valid
  private List<@Valid WeaponSummary> weapons = new ArrayList<>();

  public GetWeaponsByClass200Response weaponClass(@Nullable String weaponClass) {
    this.weaponClass = weaponClass;
    return this;
  }

  /**
   * Get weaponClass
   * @return weaponClass
   */
  
  @Schema(name = "weapon_class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weapon_class")
  public @Nullable String getWeaponClass() {
    return weaponClass;
  }

  public void setWeaponClass(@Nullable String weaponClass) {
    this.weaponClass = weaponClass;
  }

  public GetWeaponsByClass200Response weapons(List<@Valid WeaponSummary> weapons) {
    this.weapons = weapons;
    return this;
  }

  public GetWeaponsByClass200Response addWeaponsItem(WeaponSummary weaponsItem) {
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
    GetWeaponsByClass200Response getWeaponsByClass200Response = (GetWeaponsByClass200Response) o;
    return Objects.equals(this.weaponClass, getWeaponsByClass200Response.weaponClass) &&
        Objects.equals(this.weapons, getWeaponsByClass200Response.weapons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weaponClass, weapons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetWeaponsByClass200Response {\n");
    sb.append("    weaponClass: ").append(toIndentedString(weaponClass)).append("\n");
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

