package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.authservice.model.GameCharacterOriginStartingResources;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameCharacterOrigin
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GameCharacterOrigin {

  /**
   * Идентификатор происхождения
   */
  public enum IdEnum {
    STREET_KID("street_kid"),
    
    CORPO("corpo"),
    
    NOMAD("nomad");

    private final String value;

    IdEnum(String value) {
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
    public static IdEnum fromValue(String value) {
      for (IdEnum b : IdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private IdEnum id;

  private String name;

  private String description;

  @Valid
  private List<String> startingSkills = new ArrayList<>();

  @Valid
  private List<UUID> availableFactions = new ArrayList<>();

  private GameCharacterOriginStartingResources startingResources;

  public GameCharacterOrigin() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacterOrigin(IdEnum id, String name, String description, List<String> startingSkills, List<UUID> availableFactions, GameCharacterOriginStartingResources startingResources) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.startingSkills = startingSkills;
    this.availableFactions = availableFactions;
    this.startingResources = startingResources;
  }

  public GameCharacterOrigin id(IdEnum id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор происхождения
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "street_kid", description = "Идентификатор происхождения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public IdEnum getId() {
    return id;
  }

  public void setId(IdEnum id) {
    this.id = id;
  }

  public GameCharacterOrigin name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название происхождения
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Уличный бродяга", description = "Название происхождения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameCharacterOrigin description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание происхождения
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Вырос на улицах, выживание любой ценой", description = "Описание происхождения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GameCharacterOrigin startingSkills(List<String> startingSkills) {
    this.startingSkills = startingSkills;
    return this;
  }

  public GameCharacterOrigin addStartingSkillsItem(String startingSkillsItem) {
    if (this.startingSkills == null) {
      this.startingSkills = new ArrayList<>();
    }
    this.startingSkills.add(startingSkillsItem);
    return this;
  }

  /**
   * Список стартовых навыков
   * @return startingSkills
   */
  @NotNull 
  @Schema(name = "starting_skills", example = "[\"street_combat\",\"survival\"]", description = "Список стартовых навыков", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("starting_skills")
  public List<String> getStartingSkills() {
    return startingSkills;
  }

  public void setStartingSkills(List<String> startingSkills) {
    this.startingSkills = startingSkills;
  }

  public GameCharacterOrigin availableFactions(List<UUID> availableFactions) {
    this.availableFactions = availableFactions;
    return this;
  }

  public GameCharacterOrigin addAvailableFactionsItem(UUID availableFactionsItem) {
    if (this.availableFactions == null) {
      this.availableFactions = new ArrayList<>();
    }
    this.availableFactions.add(availableFactionsItem);
    return this;
  }

  /**
   * Список доступных фракций (UUID)
   * @return availableFactions
   */
  @NotNull @Valid 
  @Schema(name = "available_factions", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]", description = "Список доступных фракций (UUID)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("available_factions")
  public List<UUID> getAvailableFactions() {
    return availableFactions;
  }

  public void setAvailableFactions(List<UUID> availableFactions) {
    this.availableFactions = availableFactions;
  }

  public GameCharacterOrigin startingResources(GameCharacterOriginStartingResources startingResources) {
    this.startingResources = startingResources;
    return this;
  }

  /**
   * Get startingResources
   * @return startingResources
   */
  @NotNull @Valid 
  @Schema(name = "starting_resources", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("starting_resources")
  public GameCharacterOriginStartingResources getStartingResources() {
    return startingResources;
  }

  public void setStartingResources(GameCharacterOriginStartingResources startingResources) {
    this.startingResources = startingResources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacterOrigin gameCharacterOrigin = (GameCharacterOrigin) o;
    return Objects.equals(this.id, gameCharacterOrigin.id) &&
        Objects.equals(this.name, gameCharacterOrigin.name) &&
        Objects.equals(this.description, gameCharacterOrigin.description) &&
        Objects.equals(this.startingSkills, gameCharacterOrigin.startingSkills) &&
        Objects.equals(this.availableFactions, gameCharacterOrigin.availableFactions) &&
        Objects.equals(this.startingResources, gameCharacterOrigin.startingResources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, startingSkills, availableFactions, startingResources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCharacterOrigin {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    startingSkills: ").append(toIndentedString(startingSkills)).append("\n");
    sb.append("    availableFactions: ").append(toIndentedString(availableFactions)).append("\n");
    sb.append("    startingResources: ").append(toIndentedString(startingResources)).append("\n");
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

