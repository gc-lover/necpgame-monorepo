package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.WeaponMod;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetWeaponMods200Response
 */

@JsonTypeName("getWeaponMods_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:14:20.180301500+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetWeaponMods200Response {

  @Valid
  private List<@Valid WeaponMod> mods = new ArrayList<>();

  public GetWeaponMods200Response mods(List<@Valid WeaponMod> mods) {
    this.mods = mods;
    return this;
  }

  public GetWeaponMods200Response addModsItem(WeaponMod modsItem) {
    if (this.mods == null) {
      this.mods = new ArrayList<>();
    }
    this.mods.add(modsItem);
    return this;
  }

  /**
   * Get mods
   * @return mods
   */
  @Valid 
  @Schema(name = "mods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mods")
  public List<@Valid WeaponMod> getMods() {
    return mods;
  }

  public void setMods(List<@Valid WeaponMod> mods) {
    this.mods = mods;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetWeaponMods200Response getWeaponMods200Response = (GetWeaponMods200Response) o;
    return Objects.equals(this.mods, getWeaponMods200Response.mods);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mods);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetWeaponMods200Response {\n");
    sb.append("    mods: ").append(toIndentedString(mods)).append("\n");
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


