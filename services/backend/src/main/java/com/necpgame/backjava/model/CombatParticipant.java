package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatParticipant
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:00.452540100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CombatParticipant {

  private String id;

  private String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PLAYER("player"),
    
    ENEMY("enemy"),
    
    NPC("npc");

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

  private TypeEnum type;

  private Integer health;

  private Integer maxHealth;

  private JsonNullable<Integer> energy = JsonNullable.<Integer>undefined();

  private @Nullable Integer armor;

  private @Nullable Boolean isAlive;

  public CombatParticipant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CombatParticipant(String id, String name, TypeEnum type, Integer health, Integer maxHealth) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.health = health;
    this.maxHealth = maxHealth;
  }

  public CombatParticipant id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CombatParticipant name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CombatParticipant type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public CombatParticipant health(Integer health) {
    this.health = health;
    return this;
  }

  /**
   * Get health
   * @return health
   */
  @NotNull 
  @Schema(name = "health", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("health")
  public Integer getHealth() {
    return health;
  }

  public void setHealth(Integer health) {
    this.health = health;
  }

  public CombatParticipant maxHealth(Integer maxHealth) {
    this.maxHealth = maxHealth;
    return this;
  }

  /**
   * Get maxHealth
   * @return maxHealth
   */
  @NotNull 
  @Schema(name = "maxHealth", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxHealth")
  public Integer getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(Integer maxHealth) {
    this.maxHealth = maxHealth;
  }

  public CombatParticipant energy(Integer energy) {
    this.energy = JsonNullable.of(energy);
    return this;
  }

  /**
   * Get energy
   * @return energy
   */
  
  @Schema(name = "energy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy")
  public JsonNullable<Integer> getEnergy() {
    return energy;
  }

  public void setEnergy(JsonNullable<Integer> energy) {
    this.energy = energy;
  }

  public CombatParticipant armor(@Nullable Integer armor) {
    this.armor = armor;
    return this;
  }

  /**
   * Get armor
   * @return armor
   */
  
  @Schema(name = "armor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("armor")
  public @Nullable Integer getArmor() {
    return armor;
  }

  public void setArmor(@Nullable Integer armor) {
    this.armor = armor;
  }

  public CombatParticipant isAlive(@Nullable Boolean isAlive) {
    this.isAlive = isAlive;
    return this;
  }

  /**
   * Get isAlive
   * @return isAlive
   */
  
  @Schema(name = "isAlive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isAlive")
  public @Nullable Boolean getIsAlive() {
    return isAlive;
  }

  public void setIsAlive(@Nullable Boolean isAlive) {
    this.isAlive = isAlive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatParticipant combatParticipant = (CombatParticipant) o;
    return Objects.equals(this.id, combatParticipant.id) &&
        Objects.equals(this.name, combatParticipant.name) &&
        Objects.equals(this.type, combatParticipant.type) &&
        Objects.equals(this.health, combatParticipant.health) &&
        Objects.equals(this.maxHealth, combatParticipant.maxHealth) &&
        equalsNullable(this.energy, combatParticipant.energy) &&
        Objects.equals(this.armor, combatParticipant.armor) &&
        Objects.equals(this.isAlive, combatParticipant.isAlive);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, type, health, maxHealth, hashCodeNullable(energy), armor, isAlive);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatParticipant {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
    sb.append("    maxHealth: ").append(toIndentedString(maxHealth)).append("\n");
    sb.append("    energy: ").append(toIndentedString(energy)).append("\n");
    sb.append("    armor: ").append(toIndentedString(armor)).append("\n");
    sb.append("    isAlive: ").append(toIndentedString(isAlive)).append("\n");
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

