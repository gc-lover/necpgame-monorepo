package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameClass
 */


public class GameClass {

  private String classId;

  private String name;

  private @Nullable String description;

  /**
   * Источник класса
   */
  public enum SourceEnum {
    CYBERPUNK_CANON("cyberpunk_canon"),
    
    AUTHORED("authored"),
    
    CUSTOM_PATH("custom_path");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SourceEnum source;

  private @Nullable String role;

  private @Nullable Object startingBonuses;

  private @Nullable Integer subclassesCount;

  public GameClass() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameClass(String classId, String name, SourceEnum source) {
    this.classId = classId;
    this.name = name;
    this.source = source;
  }

  public GameClass classId(String classId) {
    this.classId = classId;
    return this;
  }

  /**
   * Get classId
   * @return classId
   */
  @NotNull 
  @Schema(name = "class_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("class_id")
  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public GameClass name(String name) {
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

  public GameClass description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GameClass source(SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Источник класса
   * @return source
   */
  @NotNull 
  @Schema(name = "source", description = "Источник класса", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public GameClass role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Основная роль (Combat, Hacking, Social, etc.)
   * @return role
   */
  
  @Schema(name = "role", description = "Основная роль (Combat, Hacking, Social, etc.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public GameClass startingBonuses(@Nullable Object startingBonuses) {
    this.startingBonuses = startingBonuses;
    return this;
  }

  /**
   * Стартовые бонусы к атрибутам и навыкам
   * @return startingBonuses
   */
  
  @Schema(name = "starting_bonuses", description = "Стартовые бонусы к атрибутам и навыкам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_bonuses")
  public @Nullable Object getStartingBonuses() {
    return startingBonuses;
  }

  public void setStartingBonuses(@Nullable Object startingBonuses) {
    this.startingBonuses = startingBonuses;
  }

  public GameClass subclassesCount(@Nullable Integer subclassesCount) {
    this.subclassesCount = subclassesCount;
    return this;
  }

  /**
   * Количество доступных подклассов
   * @return subclassesCount
   */
  
  @Schema(name = "subclasses_count", description = "Количество доступных подклассов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subclasses_count")
  public @Nullable Integer getSubclassesCount() {
    return subclassesCount;
  }

  public void setSubclassesCount(@Nullable Integer subclassesCount) {
    this.subclassesCount = subclassesCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameClass gameClass = (GameClass) o;
    return Objects.equals(this.classId, gameClass.classId) &&
        Objects.equals(this.name, gameClass.name) &&
        Objects.equals(this.description, gameClass.description) &&
        Objects.equals(this.source, gameClass.source) &&
        Objects.equals(this.role, gameClass.role) &&
        Objects.equals(this.startingBonuses, gameClass.startingBonuses) &&
        Objects.equals(this.subclassesCount, gameClass.subclassesCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, name, description, source, role, startingBonuses, subclassesCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameClass {\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    startingBonuses: ").append(toIndentedString(startingBonuses)).append("\n");
    sb.append("    subclassesCount: ").append(toIndentedString(subclassesCount)).append("\n");
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

