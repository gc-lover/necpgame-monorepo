package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GameNPC
 */


public class GameNPC {

  private String id;

  private String name;

  private @Nullable String description;

  /**
   * Тип NPC: - trader: торговец - quest_giver: квестодатель - citizen: обычный житель - enemy: враг 
   */
  public enum TypeEnum {
    TRADER("trader"),
    
    QUEST_GIVER("quest_giver"),
    
    CITIZEN("citizen"),
    
    ENEMY("enemy");

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

  private JsonNullable<String> faction = JsonNullable.<String>undefined();

  private String greeting;

  @Valid
  private List<String> availableQuests = new ArrayList<>();

  public GameNPC() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameNPC(String id, String name, TypeEnum type, String greeting) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.greeting = greeting;
  }

  public GameNPC id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор NPC
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "npc-sarah-miller", description = "Уникальный идентификатор NPC", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameNPC name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Имя NPC
   * @return name
   */
  @NotNull @Size(min = 1, max = 100) 
  @Schema(name = "name", example = "Сара Миллер", description = "Имя NPC", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameNPC description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание NPC
   * @return description
   */
  @Size(max = 500) 
  @Schema(name = "description", example = "Офицер NCPD, работает в корпоративном районе Downtown.", description = "Описание NPC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GameNPC type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Тип NPC: - trader: торговец - quest_giver: квестодатель - citizen: обычный житель - enemy: враг 
   * @return type
   */
  @NotNull 
  @Schema(name = "type", example = "quest_giver", description = "Тип NPC: - trader: торговец - quest_giver: квестодатель - citizen: обычный житель - enemy: враг ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public GameNPC faction(String faction) {
    this.faction = JsonNullable.of(faction);
    return this;
  }

  /**
   * Фракция NPC (если есть)
   * @return faction
   */
  
  @Schema(name = "faction", example = "ncpd", description = "Фракция NPC (если есть)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public JsonNullable<String> getFaction() {
    return faction;
  }

  public void setFaction(JsonNullable<String> faction) {
    this.faction = faction;
  }

  public GameNPC greeting(String greeting) {
    this.greeting = greeting;
    return this;
  }

  /**
   * Приветствие NPC при первом взаимодействии
   * @return greeting
   */
  @NotNull @Size(min = 10, max = 500) 
  @Schema(name = "greeting", example = "Привет. Я офицер Миллер. Если ты хочешь помочь полиции, у меня есть несколько заданий.", description = "Приветствие NPC при первом взаимодействии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("greeting")
  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }

  public GameNPC availableQuests(List<String> availableQuests) {
    this.availableQuests = availableQuests;
    return this;
  }

  public GameNPC addAvailableQuestsItem(String availableQuestsItem) {
    if (this.availableQuests == null) {
      this.availableQuests = new ArrayList<>();
    }
    this.availableQuests.add(availableQuestsItem);
    return this;
  }

  /**
   * Список ID доступных квестов от этого NPC
   * @return availableQuests
   */
  
  @Schema(name = "availableQuests", example = "[\"quest-delivery-001\"]", description = "Список ID доступных квестов от этого NPC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availableQuests")
  public List<String> getAvailableQuests() {
    return availableQuests;
  }

  public void setAvailableQuests(List<String> availableQuests) {
    this.availableQuests = availableQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameNPC gameNPC = (GameNPC) o;
    return Objects.equals(this.id, gameNPC.id) &&
        Objects.equals(this.name, gameNPC.name) &&
        Objects.equals(this.description, gameNPC.description) &&
        Objects.equals(this.type, gameNPC.type) &&
        equalsNullable(this.faction, gameNPC.faction) &&
        Objects.equals(this.greeting, gameNPC.greeting) &&
        Objects.equals(this.availableQuests, gameNPC.availableQuests);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type, hashCodeNullable(faction), greeting, availableQuests);
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
    sb.append("class GameNPC {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    greeting: ").append(toIndentedString(greeting)).append("\n");
    sb.append("    availableQuests: ").append(toIndentedString(availableQuests)).append("\n");
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

