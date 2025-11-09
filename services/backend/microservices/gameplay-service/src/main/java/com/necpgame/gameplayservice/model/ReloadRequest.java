package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReloadRequest
 */

@JsonTypeName("reload_request")

public class ReloadRequest {

  private String characterId;

  private String weaponId;

  public ReloadRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReloadRequest(String characterId, String weaponId) {
    this.characterId = characterId;
    this.weaponId = weaponId;
  }

  public ReloadRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID персонажа
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", description = "ID персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ReloadRequest weaponId(String weaponId) {
    this.weaponId = weaponId;
    return this;
  }

  /**
   * ID оружия
   * @return weaponId
   */
  @NotNull 
  @Schema(name = "weapon_id", description = "ID оружия", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weapon_id")
  public String getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(String weaponId) {
    this.weaponId = weaponId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReloadRequest reloadRequest = (ReloadRequest) o;
    return Objects.equals(this.characterId, reloadRequest.characterId) &&
        Objects.equals(this.weaponId, reloadRequest.weaponId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, weaponId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReloadRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    weaponId: ").append(toIndentedString(weaponId)).append("\n");
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

