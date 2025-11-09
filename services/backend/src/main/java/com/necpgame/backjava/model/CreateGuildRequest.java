package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CreateGuildRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CreateGuildRequest {

  private String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    MERCHANT("MERCHANT"),
    
    CRAFTSMAN("CRAFTSMAN"),
    
    TRANSPORT("TRANSPORT"),
    
    FINANCIAL("FINANCIAL"),
    
    MIXED("MIXED");

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

  private UUID characterId;

  private @Nullable String headquartersLocation;

  private @Nullable String description;

  public CreateGuildRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateGuildRequest(String name, TypeEnum type, UUID characterId) {
    this.name = name;
    this.type = type;
    this.characterId = characterId;
  }

  public CreateGuildRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Size(min = 3, max = 50) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateGuildRequest type(TypeEnum type) {
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

  public CreateGuildRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Guild Master
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", description = "Guild Master", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public CreateGuildRequest headquartersLocation(@Nullable String headquartersLocation) {
    this.headquartersLocation = headquartersLocation;
    return this;
  }

  /**
   * Get headquartersLocation
   * @return headquartersLocation
   */
  
  @Schema(name = "headquarters_location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("headquarters_location")
  public @Nullable String getHeadquartersLocation() {
    return headquartersLocation;
  }

  public void setHeadquartersLocation(@Nullable String headquartersLocation) {
    this.headquartersLocation = headquartersLocation;
  }

  public CreateGuildRequest description(@Nullable String description) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateGuildRequest createGuildRequest = (CreateGuildRequest) o;
    return Objects.equals(this.name, createGuildRequest.name) &&
        Objects.equals(this.type, createGuildRequest.type) &&
        Objects.equals(this.characterId, createGuildRequest.characterId) &&
        Objects.equals(this.headquartersLocation, createGuildRequest.headquartersLocation) &&
        Objects.equals(this.description, createGuildRequest.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, characterId, headquartersLocation, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateGuildRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    headquartersLocation: ").append(toIndentedString(headquartersLocation)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

