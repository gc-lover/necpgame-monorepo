package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GenerateLootRequest
 */

@JsonTypeName("generateLoot_request")

public class GenerateLootRequest {

  /**
   * Gets or Sets sourceType
   */
  public enum SourceTypeEnum {
    QUEST("quest"),
    
    ENEMY("enemy"),
    
    CONTAINER("container"),
    
    EVENT("event"),
    
    BOSS("boss");

    private final String value;

    SourceTypeEnum(String value) {
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
    public static SourceTypeEnum fromValue(String value) {
      for (SourceTypeEnum b : SourceTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SourceTypeEnum sourceType;

  private String sourceId;

  private @Nullable String characterId;

  private @Nullable Object modifiers;

  public GenerateLootRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GenerateLootRequest(SourceTypeEnum sourceType, String sourceId) {
    this.sourceType = sourceType;
    this.sourceId = sourceId;
  }

  public GenerateLootRequest sourceType(SourceTypeEnum sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * Get sourceType
   * @return sourceType
   */
  @NotNull 
  @Schema(name = "source_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source_type")
  public SourceTypeEnum getSourceType() {
    return sourceType;
  }

  public void setSourceType(SourceTypeEnum sourceType) {
    this.sourceType = sourceType;
  }

  public GenerateLootRequest sourceId(String sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * Get sourceId
   * @return sourceId
   */
  @NotNull 
  @Schema(name = "source_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source_id")
  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public GenerateLootRequest characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Для учета удачи персонажа
   * @return characterId
   */
  
  @Schema(name = "character_id", description = "Для учета удачи персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public GenerateLootRequest modifiers(@Nullable Object modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  /**
   * Дополнительные модификаторы
   * @return modifiers
   */
  
  @Schema(name = "modifiers", description = "Дополнительные модификаторы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public @Nullable Object getModifiers() {
    return modifiers;
  }

  public void setModifiers(@Nullable Object modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateLootRequest generateLootRequest = (GenerateLootRequest) o;
    return Objects.equals(this.sourceType, generateLootRequest.sourceType) &&
        Objects.equals(this.sourceId, generateLootRequest.sourceId) &&
        Objects.equals(this.characterId, generateLootRequest.characterId) &&
        Objects.equals(this.modifiers, generateLootRequest.modifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceType, sourceId, characterId, modifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateLootRequest {\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
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

