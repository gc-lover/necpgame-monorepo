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
 * AbilitySource
 */


public class AbilitySource {

  /**
   * - equipment: способность от экипировки - implants: способность от имплантов - skills: способность из дерева прокачки - cyberdeck: хакерская способность от кибердеки 
   */
  public enum TypeEnum {
    EQUIPMENT("equipment"),
    
    IMPLANTS("implants"),
    
    SKILLS("skills"),
    
    CYBERDECK("cyberdeck");

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

  private @Nullable TypeEnum type;

  private @Nullable String itemId;

  private @Nullable String brand;

  public AbilitySource type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * - equipment: способность от экипировки - implants: способность от имплантов - skills: способность из дерева прокачки - cyberdeck: хакерская способность от кибердеки 
   * @return type
   */
  
  @Schema(name = "type", description = "- equipment: способность от экипировки - implants: способность от имплантов - skills: способность из дерева прокачки - cyberdeck: хакерская способность от кибердеки ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public AbilitySource itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * ID источника (экипировка/имплант/навык)
   * @return itemId
   */
  
  @Schema(name = "item_id", description = "ID источника (экипировка/имплант/навык)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public AbilitySource brand(@Nullable String brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Бренд источника (для сетовых синергий)
   * @return brand
   */
  
  @Schema(name = "brand", description = "Бренд источника (для сетовых синергий)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("brand")
  public @Nullable String getBrand() {
    return brand;
  }

  public void setBrand(@Nullable String brand) {
    this.brand = brand;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilitySource abilitySource = (AbilitySource) o;
    return Objects.equals(this.type, abilitySource.type) &&
        Objects.equals(this.itemId, abilitySource.itemId) &&
        Objects.equals(this.brand, abilitySource.brand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, itemId, brand);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilitySource {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
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

