package com.necpgame.gameplayservice.model;

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
 * Модификаторы для органических частей тела
 */

@Schema(name = "BodyPartModifiers_organic", description = "Модификаторы для органических частей тела")
@JsonTypeName("BodyPartModifiers_organic")

public class BodyPartModifiersOrganic {

  private @Nullable BigDecimal head;

  private @Nullable BigDecimal torso;

  private @Nullable BigDecimal arms;

  private @Nullable BigDecimal legs;

  public BodyPartModifiersOrganic head(@Nullable BigDecimal head) {
    this.head = head;
    return this;
  }

  /**
   * Get head
   * @return head
   */
  @Valid 
  @Schema(name = "head", example = "2.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("head")
  public @Nullable BigDecimal getHead() {
    return head;
  }

  public void setHead(@Nullable BigDecimal head) {
    this.head = head;
  }

  public BodyPartModifiersOrganic torso(@Nullable BigDecimal torso) {
    this.torso = torso;
    return this;
  }

  /**
   * Get torso
   * @return torso
   */
  @Valid 
  @Schema(name = "torso", example = "1.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("torso")
  public @Nullable BigDecimal getTorso() {
    return torso;
  }

  public void setTorso(@Nullable BigDecimal torso) {
    this.torso = torso;
  }

  public BodyPartModifiersOrganic arms(@Nullable BigDecimal arms) {
    this.arms = arms;
    return this;
  }

  /**
   * Get arms
   * @return arms
   */
  @Valid 
  @Schema(name = "arms", example = "0.7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arms")
  public @Nullable BigDecimal getArms() {
    return arms;
  }

  public void setArms(@Nullable BigDecimal arms) {
    this.arms = arms;
  }

  public BodyPartModifiersOrganic legs(@Nullable BigDecimal legs) {
    this.legs = legs;
    return this;
  }

  /**
   * Get legs
   * @return legs
   */
  @Valid 
  @Schema(name = "legs", example = "0.7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("legs")
  public @Nullable BigDecimal getLegs() {
    return legs;
  }

  public void setLegs(@Nullable BigDecimal legs) {
    this.legs = legs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BodyPartModifiersOrganic bodyPartModifiersOrganic = (BodyPartModifiersOrganic) o;
    return Objects.equals(this.head, bodyPartModifiersOrganic.head) &&
        Objects.equals(this.torso, bodyPartModifiersOrganic.torso) &&
        Objects.equals(this.arms, bodyPartModifiersOrganic.arms) &&
        Objects.equals(this.legs, bodyPartModifiersOrganic.legs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(head, torso, arms, legs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BodyPartModifiersOrganic {\n");
    sb.append("    head: ").append(toIndentedString(head)).append("\n");
    sb.append("    torso: ").append(toIndentedString(torso)).append("\n");
    sb.append("    arms: ").append(toIndentedString(arms)).append("\n");
    sb.append("    legs: ").append(toIndentedString(legs)).append("\n");
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

