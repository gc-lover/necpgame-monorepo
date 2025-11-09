package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Boss
 */


public class Boss {

  private @Nullable String bossId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    RAID("raid"),
    
    STORY("story"),
    
    WORLD("world");

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

  private @Nullable BigDecimal hp;

  @Valid
  private List<Object> phases = new ArrayList<>();

  @Valid
  private List<Object> mechanics = new ArrayList<>();

  public Boss bossId(@Nullable String bossId) {
    this.bossId = bossId;
    return this;
  }

  /**
   * Get bossId
   * @return bossId
   */
  
  @Schema(name = "boss_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boss_id")
  public @Nullable String getBossId() {
    return bossId;
  }

  public void setBossId(@Nullable String bossId) {
    this.bossId = bossId;
  }

  public Boss name(@Nullable String name) {
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

  public Boss type(@Nullable TypeEnum type) {
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

  public Boss hp(@Nullable BigDecimal hp) {
    this.hp = hp;
    return this;
  }

  /**
   * Get hp
   * @return hp
   */
  @Valid 
  @Schema(name = "hp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hp")
  public @Nullable BigDecimal getHp() {
    return hp;
  }

  public void setHp(@Nullable BigDecimal hp) {
    this.hp = hp;
  }

  public Boss phases(List<Object> phases) {
    this.phases = phases;
    return this;
  }

  public Boss addPhasesItem(Object phasesItem) {
    if (this.phases == null) {
      this.phases = new ArrayList<>();
    }
    this.phases.add(phasesItem);
    return this;
  }

  /**
   * Get phases
   * @return phases
   */
  
  @Schema(name = "phases", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phases")
  public List<Object> getPhases() {
    return phases;
  }

  public void setPhases(List<Object> phases) {
    this.phases = phases;
  }

  public Boss mechanics(List<Object> mechanics) {
    this.mechanics = mechanics;
    return this;
  }

  public Boss addMechanicsItem(Object mechanicsItem) {
    if (this.mechanics == null) {
      this.mechanics = new ArrayList<>();
    }
    this.mechanics.add(mechanicsItem);
    return this;
  }

  /**
   * Get mechanics
   * @return mechanics
   */
  
  @Schema(name = "mechanics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mechanics")
  public List<Object> getMechanics() {
    return mechanics;
  }

  public void setMechanics(List<Object> mechanics) {
    this.mechanics = mechanics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Boss boss = (Boss) o;
    return Objects.equals(this.bossId, boss.bossId) &&
        Objects.equals(this.name, boss.name) &&
        Objects.equals(this.type, boss.type) &&
        Objects.equals(this.hp, boss.hp) &&
        Objects.equals(this.phases, boss.phases) &&
        Objects.equals(this.mechanics, boss.mechanics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bossId, name, type, hp, phases, mechanics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Boss {\n");
    sb.append("    bossId: ").append(toIndentedString(bossId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    hp: ").append(toIndentedString(hp)).append("\n");
    sb.append("    phases: ").append(toIndentedString(phases)).append("\n");
    sb.append("    mechanics: ").append(toIndentedString(mechanics)).append("\n");
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

