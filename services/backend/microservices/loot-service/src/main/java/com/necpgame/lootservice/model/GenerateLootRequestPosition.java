package com.necpgame.lootservice.model;

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
 * GenerateLootRequestPosition
 */

@JsonTypeName("generateLoot_request_position")

public class GenerateLootRequestPosition {

  private @Nullable BigDecimal x;

  private @Nullable BigDecimal y;

  private @Nullable BigDecimal z;

  public GenerateLootRequestPosition x(@Nullable BigDecimal x) {
    this.x = x;
    return this;
  }

  /**
   * Get x
   * @return x
   */
  @Valid 
  @Schema(name = "x", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("x")
  public @Nullable BigDecimal getX() {
    return x;
  }

  public void setX(@Nullable BigDecimal x) {
    this.x = x;
  }

  public GenerateLootRequestPosition y(@Nullable BigDecimal y) {
    this.y = y;
    return this;
  }

  /**
   * Get y
   * @return y
   */
  @Valid 
  @Schema(name = "y", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("y")
  public @Nullable BigDecimal getY() {
    return y;
  }

  public void setY(@Nullable BigDecimal y) {
    this.y = y;
  }

  public GenerateLootRequestPosition z(@Nullable BigDecimal z) {
    this.z = z;
    return this;
  }

  /**
   * Get z
   * @return z
   */
  @Valid 
  @Schema(name = "z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("z")
  public @Nullable BigDecimal getZ() {
    return z;
  }

  public void setZ(@Nullable BigDecimal z) {
    this.z = z;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateLootRequestPosition generateLootRequestPosition = (GenerateLootRequestPosition) o;
    return Objects.equals(this.x, generateLootRequestPosition.x) &&
        Objects.equals(this.y, generateLootRequestPosition.y) &&
        Objects.equals(this.z, generateLootRequestPosition.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateLootRequestPosition {\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
    sb.append("    z: ").append(toIndentedString(z)).append("\n");
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

