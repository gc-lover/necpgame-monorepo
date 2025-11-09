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
 * Модификаторы характеристик
 */

@Schema(name = "Implant_stat_modifiers", description = "Модификаторы характеристик")
@JsonTypeName("Implant_stat_modifiers")

public class ImplantStatModifiers {

  private @Nullable BigDecimal accuracy;

  private @Nullable BigDecimal damage;

  private @Nullable BigDecimal fireRate;

  private @Nullable BigDecimal armor;

  private @Nullable BigDecimal speed;

  private @Nullable BigDecimal health;

  public ImplantStatModifiers accuracy(@Nullable BigDecimal accuracy) {
    this.accuracy = accuracy;
    return this;
  }

  /**
   * Get accuracy
   * @return accuracy
   */
  @Valid 
  @Schema(name = "accuracy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accuracy")
  public @Nullable BigDecimal getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(@Nullable BigDecimal accuracy) {
    this.accuracy = accuracy;
  }

  public ImplantStatModifiers damage(@Nullable BigDecimal damage) {
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

  public ImplantStatModifiers fireRate(@Nullable BigDecimal fireRate) {
    this.fireRate = fireRate;
    return this;
  }

  /**
   * Get fireRate
   * @return fireRate
   */
  @Valid 
  @Schema(name = "fire_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fire_rate")
  public @Nullable BigDecimal getFireRate() {
    return fireRate;
  }

  public void setFireRate(@Nullable BigDecimal fireRate) {
    this.fireRate = fireRate;
  }

  public ImplantStatModifiers armor(@Nullable BigDecimal armor) {
    this.armor = armor;
    return this;
  }

  /**
   * Get armor
   * @return armor
   */
  @Valid 
  @Schema(name = "armor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("armor")
  public @Nullable BigDecimal getArmor() {
    return armor;
  }

  public void setArmor(@Nullable BigDecimal armor) {
    this.armor = armor;
  }

  public ImplantStatModifiers speed(@Nullable BigDecimal speed) {
    this.speed = speed;
    return this;
  }

  /**
   * Get speed
   * @return speed
   */
  @Valid 
  @Schema(name = "speed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speed")
  public @Nullable BigDecimal getSpeed() {
    return speed;
  }

  public void setSpeed(@Nullable BigDecimal speed) {
    this.speed = speed;
  }

  public ImplantStatModifiers health(@Nullable BigDecimal health) {
    this.health = health;
    return this;
  }

  /**
   * Get health
   * @return health
   */
  @Valid 
  @Schema(name = "health", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("health")
  public @Nullable BigDecimal getHealth() {
    return health;
  }

  public void setHealth(@Nullable BigDecimal health) {
    this.health = health;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantStatModifiers implantStatModifiers = (ImplantStatModifiers) o;
    return Objects.equals(this.accuracy, implantStatModifiers.accuracy) &&
        Objects.equals(this.damage, implantStatModifiers.damage) &&
        Objects.equals(this.fireRate, implantStatModifiers.fireRate) &&
        Objects.equals(this.armor, implantStatModifiers.armor) &&
        Objects.equals(this.speed, implantStatModifiers.speed) &&
        Objects.equals(this.health, implantStatModifiers.health);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accuracy, damage, fireRate, armor, speed, health);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantStatModifiers {\n");
    sb.append("    accuracy: ").append(toIndentedString(accuracy)).append("\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
    sb.append("    fireRate: ").append(toIndentedString(fireRate)).append("\n");
    sb.append("    armor: ").append(toIndentedString(armor)).append("\n");
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
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

