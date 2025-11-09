package com.necpgame.adminservice.model;

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
 * HUDDataCharacter
 */

@JsonTypeName("HUDData_character")

public class HUDDataCharacter {

  private @Nullable String name;

  private @Nullable Integer level;

  private @Nullable Integer hp;

  private @Nullable Integer hpMax;

  private @Nullable Integer stamina;

  public HUDDataCharacter name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public HUDDataCharacter level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public HUDDataCharacter hp(@Nullable Integer hp) {
    this.hp = hp;
    return this;
  }

  /**
   * Get hp
   * @return hp
   */
  
  @Schema(name = "hp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hp")
  public @Nullable Integer getHp() {
    return hp;
  }

  public void setHp(@Nullable Integer hp) {
    this.hp = hp;
  }

  public HUDDataCharacter hpMax(@Nullable Integer hpMax) {
    this.hpMax = hpMax;
    return this;
  }

  /**
   * Get hpMax
   * @return hpMax
   */
  
  @Schema(name = "hp_max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hp_max")
  public @Nullable Integer getHpMax() {
    return hpMax;
  }

  public void setHpMax(@Nullable Integer hpMax) {
    this.hpMax = hpMax;
  }

  public HUDDataCharacter stamina(@Nullable Integer stamina) {
    this.stamina = stamina;
    return this;
  }

  /**
   * Get stamina
   * @return stamina
   */
  
  @Schema(name = "stamina", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stamina")
  public @Nullable Integer getStamina() {
    return stamina;
  }

  public void setStamina(@Nullable Integer stamina) {
    this.stamina = stamina;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HUDDataCharacter huDDataCharacter = (HUDDataCharacter) o;
    return Objects.equals(this.name, huDDataCharacter.name) &&
        Objects.equals(this.level, huDDataCharacter.level) &&
        Objects.equals(this.hp, huDDataCharacter.hp) &&
        Objects.equals(this.hpMax, huDDataCharacter.hpMax) &&
        Objects.equals(this.stamina, huDDataCharacter.stamina);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, level, hp, hpMax, stamina);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HUDDataCharacter {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    hp: ").append(toIndentedString(hp)).append("\n");
    sb.append("    hpMax: ").append(toIndentedString(hpMax)).append("\n");
    sb.append("    stamina: ").append(toIndentedString(stamina)).append("\n");
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

