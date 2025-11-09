package com.necpgame.backjava.model;

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
 * UpdateWeaponMasteryRequest
 */

@JsonTypeName("updateWeaponMastery_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:14:20.180301500+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class UpdateWeaponMasteryRequest {

  private String characterId;

  private String weaponId;

  private Integer kills;

  public UpdateWeaponMasteryRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateWeaponMasteryRequest(String characterId, String weaponId, Integer kills) {
    this.characterId = characterId;
    this.weaponId = weaponId;
    this.kills = kills;
  }

  public UpdateWeaponMasteryRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public UpdateWeaponMasteryRequest weaponId(String weaponId) {
    this.weaponId = weaponId;
    return this;
  }

  /**
   * Get weaponId
   * @return weaponId
   */
  @NotNull 
  @Schema(name = "weapon_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weapon_id")
  public String getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(String weaponId) {
    this.weaponId = weaponId;
  }

  public UpdateWeaponMasteryRequest kills(Integer kills) {
    this.kills = kills;
    return this;
  }

  /**
   * Количество убийств этим оружием
   * @return kills
   */
  @NotNull 
  @Schema(name = "kills", description = "Количество убийств этим оружием", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("kills")
  public Integer getKills() {
    return kills;
  }

  public void setKills(Integer kills) {
    this.kills = kills;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateWeaponMasteryRequest updateWeaponMasteryRequest = (UpdateWeaponMasteryRequest) o;
    return Objects.equals(this.characterId, updateWeaponMasteryRequest.characterId) &&
        Objects.equals(this.weaponId, updateWeaponMasteryRequest.weaponId) &&
        Objects.equals(this.kills, updateWeaponMasteryRequest.kills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, weaponId, kills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateWeaponMasteryRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    weaponId: ").append(toIndentedString(weaponId)).append("\n");
    sb.append("    kills: ").append(toIndentedString(kills)).append("\n");
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


