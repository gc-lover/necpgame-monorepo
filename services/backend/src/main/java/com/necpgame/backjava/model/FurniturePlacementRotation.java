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
 * FurniturePlacementRotation
 */

@JsonTypeName("FurniturePlacement_rotation")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class FurniturePlacementRotation {

  private @Nullable BigDecimal yaw;

  private @Nullable BigDecimal pitch;

  private @Nullable BigDecimal roll;

  public FurniturePlacementRotation yaw(@Nullable BigDecimal yaw) {
    this.yaw = yaw;
    return this;
  }

  /**
   * Get yaw
   * @return yaw
   */
  @Valid 
  @Schema(name = "yaw", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("yaw")
  public @Nullable BigDecimal getYaw() {
    return yaw;
  }

  public void setYaw(@Nullable BigDecimal yaw) {
    this.yaw = yaw;
  }

  public FurniturePlacementRotation pitch(@Nullable BigDecimal pitch) {
    this.pitch = pitch;
    return this;
  }

  /**
   * Get pitch
   * @return pitch
   */
  @Valid 
  @Schema(name = "pitch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pitch")
  public @Nullable BigDecimal getPitch() {
    return pitch;
  }

  public void setPitch(@Nullable BigDecimal pitch) {
    this.pitch = pitch;
  }

  public FurniturePlacementRotation roll(@Nullable BigDecimal roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Get roll
   * @return roll
   */
  @Valid 
  @Schema(name = "roll", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll")
  public @Nullable BigDecimal getRoll() {
    return roll;
  }

  public void setRoll(@Nullable BigDecimal roll) {
    this.roll = roll;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FurniturePlacementRotation furniturePlacementRotation = (FurniturePlacementRotation) o;
    return Objects.equals(this.yaw, furniturePlacementRotation.yaw) &&
        Objects.equals(this.pitch, furniturePlacementRotation.pitch) &&
        Objects.equals(this.roll, furniturePlacementRotation.roll);
  }

  @Override
  public int hashCode() {
    return Objects.hash(yaw, pitch, roll);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FurniturePlacementRotation {\n");
    sb.append("    yaw: ").append(toIndentedString(yaw)).append("\n");
    sb.append("    pitch: ").append(toIndentedString(pitch)).append("\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
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

