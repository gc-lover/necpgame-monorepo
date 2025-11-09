package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Consequence
 */


public class Consequence {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    REPUTATION_CHANGE("reputation_change"),
    
    RELATIONSHIP_CHANGE("relationship_change"),
    
    ITEM_REWARD("item_reward"),
    
    QUEST_UNLOCK("quest_unlock"),
    
    QUEST_LOCK("quest_lock"),
    
    WORLD_STATE_CHANGE("world_state_change"),
    
    FACTION_STANDING_CHANGE("faction_standing_change");

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

  @Valid
  private Map<String, Object> value = new HashMap<>();

  private @Nullable String description;

  public Consequence() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Consequence(TypeEnum type) {
    this.type = type;
  }

  public Consequence type(TypeEnum type) {
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

  public Consequence value(Map<String, Object> value) {
    this.value = value;
    return this;
  }

  public Consequence putValueItem(String key, Object valueItem) {
    if (this.value == null) {
      this.value = new HashMap<>();
    }
    this.value.put(key, valueItem);
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public Map<String, Object> getValue() {
    return value;
  }

  public void setValue(Map<String, Object> value) {
    this.value = value;
  }

  public Consequence description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", example = "Arasaka reputation +50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    Consequence consequence = (Consequence) o;
    return Objects.equals(this.type, consequence.type) &&
        Objects.equals(this.value, consequence.value) &&
        Objects.equals(this.description, consequence.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Consequence {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

