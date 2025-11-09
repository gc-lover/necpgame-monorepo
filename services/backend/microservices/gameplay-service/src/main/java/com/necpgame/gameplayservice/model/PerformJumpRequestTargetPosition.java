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
 * PerformJumpRequestTargetPosition
 */

@JsonTypeName("performJump_request_target_position")

public class PerformJumpRequestTargetPosition {

  private BigDecimal x;

  private BigDecimal y;

  private BigDecimal z;

  public PerformJumpRequestTargetPosition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformJumpRequestTargetPosition(BigDecimal x, BigDecimal y, BigDecimal z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public PerformJumpRequestTargetPosition x(BigDecimal x) {
    this.x = x;
    return this;
  }

  /**
   * Get x
   * @return x
   */
  @NotNull @Valid 
  @Schema(name = "x", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("x")
  public BigDecimal getX() {
    return x;
  }

  public void setX(BigDecimal x) {
    this.x = x;
  }

  public PerformJumpRequestTargetPosition y(BigDecimal y) {
    this.y = y;
    return this;
  }

  /**
   * Get y
   * @return y
   */
  @NotNull @Valid 
  @Schema(name = "y", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("y")
  public BigDecimal getY() {
    return y;
  }

  public void setY(BigDecimal y) {
    this.y = y;
  }

  public PerformJumpRequestTargetPosition z(BigDecimal z) {
    this.z = z;
    return this;
  }

  /**
   * Get z
   * @return z
   */
  @NotNull @Valid 
  @Schema(name = "z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("z")
  public BigDecimal getZ() {
    return z;
  }

  public void setZ(BigDecimal z) {
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
    PerformJumpRequestTargetPosition performJumpRequestTargetPosition = (PerformJumpRequestTargetPosition) o;
    return Objects.equals(this.x, performJumpRequestTargetPosition.x) &&
        Objects.equals(this.y, performJumpRequestTargetPosition.y) &&
        Objects.equals(this.z, performJumpRequestTargetPosition.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformJumpRequestTargetPosition {\n");
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

