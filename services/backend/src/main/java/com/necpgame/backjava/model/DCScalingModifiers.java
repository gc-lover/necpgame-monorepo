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
 * DCScalingModifiers
 */

@JsonTypeName("DCScaling_modifiers")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DCScalingModifiers {

  private @Nullable Integer combat;

  private @Nullable Integer social;

  private @Nullable Integer hacking;

  private @Nullable Integer crafting;

  public DCScalingModifiers combat(@Nullable Integer combat) {
    this.combat = combat;
    return this;
  }

  /**
   * Get combat
   * @return combat
   */
  
  @Schema(name = "combat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat")
  public @Nullable Integer getCombat() {
    return combat;
  }

  public void setCombat(@Nullable Integer combat) {
    this.combat = combat;
  }

  public DCScalingModifiers social(@Nullable Integer social) {
    this.social = social;
    return this;
  }

  /**
   * Get social
   * @return social
   */
  
  @Schema(name = "social", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social")
  public @Nullable Integer getSocial() {
    return social;
  }

  public void setSocial(@Nullable Integer social) {
    this.social = social;
  }

  public DCScalingModifiers hacking(@Nullable Integer hacking) {
    this.hacking = hacking;
    return this;
  }

  /**
   * Get hacking
   * @return hacking
   */
  
  @Schema(name = "hacking", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hacking")
  public @Nullable Integer getHacking() {
    return hacking;
  }

  public void setHacking(@Nullable Integer hacking) {
    this.hacking = hacking;
  }

  public DCScalingModifiers crafting(@Nullable Integer crafting) {
    this.crafting = crafting;
    return this;
  }

  /**
   * Get crafting
   * @return crafting
   */
  
  @Schema(name = "crafting", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crafting")
  public @Nullable Integer getCrafting() {
    return crafting;
  }

  public void setCrafting(@Nullable Integer crafting) {
    this.crafting = crafting;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DCScalingModifiers dcScalingModifiers = (DCScalingModifiers) o;
    return Objects.equals(this.combat, dcScalingModifiers.combat) &&
        Objects.equals(this.social, dcScalingModifiers.social) &&
        Objects.equals(this.hacking, dcScalingModifiers.hacking) &&
        Objects.equals(this.crafting, dcScalingModifiers.crafting);
  }

  @Override
  public int hashCode() {
    return Objects.hash(combat, social, hacking, crafting);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCScalingModifiers {\n");
    sb.append("    combat: ").append(toIndentedString(combat)).append("\n");
    sb.append("    social: ").append(toIndentedString(social)).append("\n");
    sb.append("    hacking: ").append(toIndentedString(hacking)).append("\n");
    sb.append("    crafting: ").append(toIndentedString(crafting)).append("\n");
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

