package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.DaemonRequirements;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Daemon
 */


public class Daemon {

  private @Nullable String daemonId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    ENEMY("enemy"),
    
    DEVICE("device"),
    
    INFRASTRUCTURE("infrastructure"),
    
    COMBAT("combat");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Integer tier;

  private @Nullable BigDecimal ramCost;

  private @Nullable BigDecimal heatGeneration;

  private @Nullable BigDecimal cooldown;

  private @Nullable DaemonRequirements requirements;

  @Valid
  private List<Object> effects = new ArrayList<>();

  public Daemon daemonId(@Nullable String daemonId) {
    this.daemonId = daemonId;
    return this;
  }

  /**
   * Get daemonId
   * @return daemonId
   */
  
  @Schema(name = "daemon_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daemon_id")
  public @Nullable String getDaemonId() {
    return daemonId;
  }

  public void setDaemonId(@Nullable String daemonId) {
    this.daemonId = daemonId;
  }

  public Daemon name(@Nullable String name) {
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

  public Daemon type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public Daemon tier(@Nullable Integer tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * minimum: 1
   * maximum: 5
   * @return tier
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable Integer getTier() {
    return tier;
  }

  public void setTier(@Nullable Integer tier) {
    this.tier = tier;
  }

  public Daemon ramCost(@Nullable BigDecimal ramCost) {
    this.ramCost = ramCost;
    return this;
  }

  /**
   * Get ramCost
   * @return ramCost
   */
  @Valid 
  @Schema(name = "ram_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ram_cost")
  public @Nullable BigDecimal getRamCost() {
    return ramCost;
  }

  public void setRamCost(@Nullable BigDecimal ramCost) {
    this.ramCost = ramCost;
  }

  public Daemon heatGeneration(@Nullable BigDecimal heatGeneration) {
    this.heatGeneration = heatGeneration;
    return this;
  }

  /**
   * Get heatGeneration
   * @return heatGeneration
   */
  @Valid 
  @Schema(name = "heat_generation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heat_generation")
  public @Nullable BigDecimal getHeatGeneration() {
    return heatGeneration;
  }

  public void setHeatGeneration(@Nullable BigDecimal heatGeneration) {
    this.heatGeneration = heatGeneration;
  }

  public Daemon cooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
    return this;
  }

  /**
   * Кулдаун (секунды)
   * @return cooldown
   */
  @Valid 
  @Schema(name = "cooldown", description = "Кулдаун (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown")
  public @Nullable BigDecimal getCooldown() {
    return cooldown;
  }

  public void setCooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
  }

  public Daemon requirements(@Nullable DaemonRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable DaemonRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable DaemonRequirements requirements) {
    this.requirements = requirements;
  }

  public Daemon effects(List<Object> effects) {
    this.effects = effects;
    return this;
  }

  public Daemon addEffectsItem(Object effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Get effects
   * @return effects
   */
  
  @Schema(name = "effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public List<Object> getEffects() {
    return effects;
  }

  public void setEffects(List<Object> effects) {
    this.effects = effects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Daemon daemon = (Daemon) o;
    return Objects.equals(this.daemonId, daemon.daemonId) &&
        Objects.equals(this.name, daemon.name) &&
        Objects.equals(this.type, daemon.type) &&
        Objects.equals(this.tier, daemon.tier) &&
        Objects.equals(this.ramCost, daemon.ramCost) &&
        Objects.equals(this.heatGeneration, daemon.heatGeneration) &&
        Objects.equals(this.cooldown, daemon.cooldown) &&
        Objects.equals(this.requirements, daemon.requirements) &&
        Objects.equals(this.effects, daemon.effects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(daemonId, name, type, tier, ramCost, heatGeneration, cooldown, requirements, effects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Daemon {\n");
    sb.append("    daemonId: ").append(toIndentedString(daemonId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    ramCost: ").append(toIndentedString(ramCost)).append("\n");
    sb.append("    heatGeneration: ").append(toIndentedString(heatGeneration)).append("\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
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

