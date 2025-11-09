package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WeaponDetailsSpecialAbilitiesInner
 */

@JsonTypeName("WeaponDetails_special_abilities_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:14:20.180301500+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class WeaponDetailsSpecialAbilitiesInner {

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable BigDecimal cooldown;

  public WeaponDetailsSpecialAbilitiesInner name(@Nullable String name) {
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

  public WeaponDetailsSpecialAbilitiesInner description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public WeaponDetailsSpecialAbilitiesInner cooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
    return this;
  }

  /**
   * Get cooldown
   * @return cooldown
   */
  @Valid 
  @Schema(name = "cooldown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown")
  public @Nullable BigDecimal getCooldown() {
    return cooldown;
  }

  public void setCooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponDetailsSpecialAbilitiesInner weaponDetailsSpecialAbilitiesInner = (WeaponDetailsSpecialAbilitiesInner) o;
    return Objects.equals(this.name, weaponDetailsSpecialAbilitiesInner.name) &&
        Objects.equals(this.description, weaponDetailsSpecialAbilitiesInner.description) &&
        Objects.equals(this.cooldown, weaponDetailsSpecialAbilitiesInner.cooldown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, cooldown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponDetailsSpecialAbilitiesInner {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
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


