package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.BodyPartModifiersCyber;
import com.necpgame.gameplayservice.model.BodyPartModifiersOrganic;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BodyPartModifiers
 */


public class BodyPartModifiers {

  private @Nullable BodyPartModifiersOrganic organic;

  private @Nullable BodyPartModifiersCyber cyber;

  public BodyPartModifiers organic(@Nullable BodyPartModifiersOrganic organic) {
    this.organic = organic;
    return this;
  }

  /**
   * Get organic
   * @return organic
   */
  @Valid 
  @Schema(name = "organic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("organic")
  public @Nullable BodyPartModifiersOrganic getOrganic() {
    return organic;
  }

  public void setOrganic(@Nullable BodyPartModifiersOrganic organic) {
    this.organic = organic;
  }

  public BodyPartModifiers cyber(@Nullable BodyPartModifiersCyber cyber) {
    this.cyber = cyber;
    return this;
  }

  /**
   * Get cyber
   * @return cyber
   */
  @Valid 
  @Schema(name = "cyber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyber")
  public @Nullable BodyPartModifiersCyber getCyber() {
    return cyber;
  }

  public void setCyber(@Nullable BodyPartModifiersCyber cyber) {
    this.cyber = cyber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BodyPartModifiers bodyPartModifiers = (BodyPartModifiers) o;
    return Objects.equals(this.organic, bodyPartModifiers.organic) &&
        Objects.equals(this.cyber, bodyPartModifiers.cyber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organic, cyber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BodyPartModifiers {\n");
    sb.append("    organic: ").append(toIndentedString(organic)).append("\n");
    sb.append("    cyber: ").append(toIndentedString(cyber)).append("\n");
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

