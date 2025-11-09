package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Faction
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Faction {

  private UUID id;

  private String name;

  /**
   * Тип фракции
   */
  public enum TypeEnum {
    CORPORATION("corporation"),
    
    GANG("gang"),
    
    ORGANIZATION("organization");

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

  private String description;

  /**
   * Gets or Sets availableForOrigins
   */
  public enum AvailableForOriginsEnum {
    STREET_KID("street_kid"),
    
    CORPO("corpo"),
    
    NOMAD("nomad");

    private final String value;

    AvailableForOriginsEnum(String value) {
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
    public static AvailableForOriginsEnum fromValue(String value) {
      for (AvailableForOriginsEnum b : AvailableForOriginsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<AvailableForOriginsEnum> availableForOrigins = new ArrayList<>();

  public Faction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Faction(UUID id, String name, TypeEnum type, String description, List<AvailableForOriginsEnum> availableForOrigins) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.description = description;
    this.availableForOrigins = availableForOrigins;
  }

  public Faction id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор фракции
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", example = "550e8400-e29b-41d4-a716-446655440000", description = "Уникальный идентификатор фракции", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Faction name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название фракции
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Arasaka", description = "Название фракции", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Faction type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Тип фракции
   * @return type
   */
  @NotNull 
  @Schema(name = "type", example = "corporation", description = "Тип фракции", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Faction description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание фракции
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Корпорация, технологии, власть", description = "Описание фракции", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Faction availableForOrigins(List<AvailableForOriginsEnum> availableForOrigins) {
    this.availableForOrigins = availableForOrigins;
    return this;
  }

  public Faction addAvailableForOriginsItem(AvailableForOriginsEnum availableForOriginsItem) {
    if (this.availableForOrigins == null) {
      this.availableForOrigins = new ArrayList<>();
    }
    this.availableForOrigins.add(availableForOriginsItem);
    return this;
  }

  /**
   * Список доступных происхождений для старта
   * @return availableForOrigins
   */
  @NotNull 
  @Schema(name = "available_for_origins", example = "[\"corpo\",\"street_kid\"]", description = "Список доступных происхождений для старта", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("available_for_origins")
  public List<AvailableForOriginsEnum> getAvailableForOrigins() {
    return availableForOrigins;
  }

  public void setAvailableForOrigins(List<AvailableForOriginsEnum> availableForOrigins) {
    this.availableForOrigins = availableForOrigins;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Faction faction = (Faction) o;
    return Objects.equals(this.id, faction.id) &&
        Objects.equals(this.name, faction.name) &&
        Objects.equals(this.type, faction.type) &&
        Objects.equals(this.description, faction.description) &&
        Objects.equals(this.availableForOrigins, faction.availableForOrigins);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, type, description, availableForOrigins);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Faction {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    availableForOrigins: ").append(toIndentedString(availableForOrigins)).append("\n");
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

