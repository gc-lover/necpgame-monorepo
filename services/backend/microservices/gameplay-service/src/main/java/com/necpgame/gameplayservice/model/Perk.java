package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Perk
 */


public class Perk {

  private @Nullable String perkId;

  private @Nullable String name;

  public Perk perkId(@Nullable String perkId) {
    this.perkId = perkId;
    return this;
  }

  /**
   * Get perkId
   * @return perkId
   */
  
  @Schema(name = "perk_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perk_id")
  public @Nullable String getPerkId() {
    return perkId;
  }

  public void setPerkId(@Nullable String perkId) {
    this.perkId = perkId;
  }

  public Perk name(@Nullable String name) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Perk perk = (Perk) o;
    return Objects.equals(this.perkId, perk.perkId) &&
        Objects.equals(this.name, perk.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(perkId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Perk {\n");
    sb.append("    perkId: ").append(toIndentedString(perkId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

