package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ComponentRequirementAlternativesInner;
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
 * ComponentRequirement
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ComponentRequirement {

  private @Nullable UUID componentId;

  private @Nullable String name;

  private @Nullable Integer quantity;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("COMMON"),
    
    UNCOMMON("UNCOMMON"),
    
    RARE("RARE"),
    
    EPIC("EPIC"),
    
    LEGENDARY("LEGENDARY");

    private final String value;

    RarityEnum(String value) {
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
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RarityEnum rarity;

  @Valid
  private List<@Valid ComponentRequirementAlternativesInner> alternatives = new ArrayList<>();

  public ComponentRequirement componentId(@Nullable UUID componentId) {
    this.componentId = componentId;
    return this;
  }

  /**
   * Get componentId
   * @return componentId
   */
  @Valid 
  @Schema(name = "component_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("component_id")
  public @Nullable UUID getComponentId() {
    return componentId;
  }

  public void setComponentId(@Nullable UUID componentId) {
    this.componentId = componentId;
  }

  public ComponentRequirement name(@Nullable String name) {
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

  public ComponentRequirement quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public ComponentRequirement rarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
  }

  public ComponentRequirement alternatives(List<@Valid ComponentRequirementAlternativesInner> alternatives) {
    this.alternatives = alternatives;
    return this;
  }

  public ComponentRequirement addAlternativesItem(ComponentRequirementAlternativesInner alternativesItem) {
    if (this.alternatives == null) {
      this.alternatives = new ArrayList<>();
    }
    this.alternatives.add(alternativesItem);
    return this;
  }

  /**
   * Альтернативные компоненты
   * @return alternatives
   */
  @Valid 
  @Schema(name = "alternatives", description = "Альтернативные компоненты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alternatives")
  public List<@Valid ComponentRequirementAlternativesInner> getAlternatives() {
    return alternatives;
  }

  public void setAlternatives(List<@Valid ComponentRequirementAlternativesInner> alternatives) {
    this.alternatives = alternatives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponentRequirement componentRequirement = (ComponentRequirement) o;
    return Objects.equals(this.componentId, componentRequirement.componentId) &&
        Objects.equals(this.name, componentRequirement.name) &&
        Objects.equals(this.quantity, componentRequirement.quantity) &&
        Objects.equals(this.rarity, componentRequirement.rarity) &&
        Objects.equals(this.alternatives, componentRequirement.alternatives);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentId, name, quantity, rarity, alternatives);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComponentRequirement {\n");
    sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    alternatives: ").append(toIndentedString(alternatives)).append("\n");
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

