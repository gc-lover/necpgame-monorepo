package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * GameCharacterSummary
 */


public class GameCharacterSummary {

  private UUID id;

  private String name;

  private String propertyClass;

  private Integer level;

  private JsonNullable<String> factionName = JsonNullable.<String>undefined();

  private String cityName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> lastLogin = JsonNullable.<OffsetDateTime>undefined();

  public GameCharacterSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacterSummary(UUID id, String name, String propertyClass, Integer level, String cityName) {
    this.id = id;
    this.name = name;
    this.propertyClass = propertyClass;
    this.level = level;
    this.cityName = cityName;
  }

  public GameCharacterSummary id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор персонажа
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", example = "550e8400-e29b-41d4-a716-446655440000", description = "Уникальный идентификатор персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public GameCharacterSummary name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Имя персонажа
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "John Doe", description = "Имя персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameCharacterSummary propertyClass(String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Класс персонажа
   * @return propertyClass
   */
  @NotNull 
  @Schema(name = "class", example = "Solo", description = "Класс персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("class")
  public String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public GameCharacterSummary level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Уровень персонажа
   * @return level
   */
  @NotNull 
  @Schema(name = "level", example = "5", description = "Уровень персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public GameCharacterSummary factionName(String factionName) {
    this.factionName = JsonNullable.of(factionName);
    return this;
  }

  /**
   * Название фракции
   * @return factionName
   */
  
  @Schema(name = "faction_name", example = "Arasaka", description = "Название фракции", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_name")
  public JsonNullable<String> getFactionName() {
    return factionName;
  }

  public void setFactionName(JsonNullable<String> factionName) {
    this.factionName = factionName;
  }

  public GameCharacterSummary cityName(String cityName) {
    this.cityName = cityName;
    return this;
  }

  /**
   * Название города
   * @return cityName
   */
  @NotNull 
  @Schema(name = "city_name", example = "Night City", description = "Название города", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city_name")
  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public GameCharacterSummary lastLogin(OffsetDateTime lastLogin) {
    this.lastLogin = JsonNullable.of(lastLogin);
    return this;
  }

  /**
   * Дата последнего входа в игру
   * @return lastLogin
   */
  @Valid 
  @Schema(name = "last_login", example = "2025-01-27T10:00Z", description = "Дата последнего входа в игру", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_login")
  public JsonNullable<OffsetDateTime> getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(JsonNullable<OffsetDateTime> lastLogin) {
    this.lastLogin = lastLogin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacterSummary gameCharacterSummary = (GameCharacterSummary) o;
    return Objects.equals(this.id, gameCharacterSummary.id) &&
        Objects.equals(this.name, gameCharacterSummary.name) &&
        Objects.equals(this.propertyClass, gameCharacterSummary.propertyClass) &&
        Objects.equals(this.level, gameCharacterSummary.level) &&
        equalsNullable(this.factionName, gameCharacterSummary.factionName) &&
        Objects.equals(this.cityName, gameCharacterSummary.cityName) &&
        equalsNullable(this.lastLogin, gameCharacterSummary.lastLogin);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, propertyClass, level, hashCodeNullable(factionName), cityName, hashCodeNullable(lastLogin));
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
    sb.append("class GameCharacterSummary {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    factionName: ").append(toIndentedString(factionName)).append("\n");
    sb.append("    cityName: ").append(toIndentedString(cityName)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
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

