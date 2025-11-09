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
 * HackEnemy200ResponseEffect
 */

@JsonTypeName("hackEnemy_200_response_effect")

public class HackEnemy200ResponseEffect {

  private @Nullable String type;

  private @Nullable BigDecimal duration;

  private @Nullable BigDecimal damage;

  public HackEnemy200ResponseEffect type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public HackEnemy200ResponseEffect duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Get duration
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  public HackEnemy200ResponseEffect damage(@Nullable BigDecimal damage) {
    this.damage = damage;
    return this;
  }

  /**
   * Get damage
   * @return damage
   */
  @Valid 
  @Schema(name = "damage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage")
  public @Nullable BigDecimal getDamage() {
    return damage;
  }

  public void setDamage(@Nullable BigDecimal damage) {
    this.damage = damage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackEnemy200ResponseEffect hackEnemy200ResponseEffect = (HackEnemy200ResponseEffect) o;
    return Objects.equals(this.type, hackEnemy200ResponseEffect.type) &&
        Objects.equals(this.duration, hackEnemy200ResponseEffect.duration) &&
        Objects.equals(this.damage, hackEnemy200ResponseEffect.damage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, duration, damage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackEnemy200ResponseEffect {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
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

