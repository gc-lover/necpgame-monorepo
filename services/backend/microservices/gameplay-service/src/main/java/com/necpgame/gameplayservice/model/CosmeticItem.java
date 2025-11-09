package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CosmeticItemAssets;
import com.necpgame.gameplayservice.model.CosmeticItemAvailability;
import com.necpgame.gameplayservice.model.CosmeticItemExclusivity;
import com.necpgame.gameplayservice.model.CosmeticItemMetadata;
import com.necpgame.gameplayservice.model.CosmeticItemPrice;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CosmeticItem
 */


public class CosmeticItem {

  private String itemId;

  private String code;

  private String name;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    SKIN_CHARACTER("skin_character"),
    
    SKIN_WEAPON("skin_weapon"),
    
    EMOTE("emote"),
    
    TITLE("title"),
    
    NAMEPLATE("nameplate"),
    
    POSE("pose"),
    
    EFFECT("effect");

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

  private @Nullable String category;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("common"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary"),
    
    MYTHIC("mythic");

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

  private RarityEnum rarity;

  private @Nullable CosmeticItemPrice price;

  private @Nullable CosmeticItemExclusivity exclusivity;

  private @Nullable CosmeticItemAvailability availability;

  private @Nullable CosmeticItemAssets assets;

  private @Nullable CosmeticItemMetadata metadata;

  public CosmeticItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CosmeticItem(String itemId, String code, String name, TypeEnum type, RarityEnum rarity) {
    this.itemId = itemId;
    this.code = code;
    this.name = name;
    this.type = type;
    this.rarity = rarity;
  }

  public CosmeticItem itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * UUID предмета
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", description = "UUID предмета", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public CosmeticItem code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Уникальный код для интеграций
   * @return code
   */
  @NotNull 
  @Schema(name = "code", description = "Уникальный код для интеграций", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public CosmeticItem name(String name) {
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

  public CosmeticItem description(@Nullable String description) {
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

  public CosmeticItem type(TypeEnum type) {
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

  public CosmeticItem category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Подкатегория (например,枪/нож)
   * @return category
   */
  
  @Schema(name = "category", description = "Подкатегория (например,枪/нож)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public CosmeticItem rarity(RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  @NotNull 
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rarity")
  public RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(RarityEnum rarity) {
    this.rarity = rarity;
  }

  public CosmeticItem price(@Nullable CosmeticItemPrice price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
   */
  @Valid 
  @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable CosmeticItemPrice getPrice() {
    return price;
  }

  public void setPrice(@Nullable CosmeticItemPrice price) {
    this.price = price;
  }

  public CosmeticItem exclusivity(@Nullable CosmeticItemExclusivity exclusivity) {
    this.exclusivity = exclusivity;
    return this;
  }

  /**
   * Get exclusivity
   * @return exclusivity
   */
  @Valid 
  @Schema(name = "exclusivity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exclusivity")
  public @Nullable CosmeticItemExclusivity getExclusivity() {
    return exclusivity;
  }

  public void setExclusivity(@Nullable CosmeticItemExclusivity exclusivity) {
    this.exclusivity = exclusivity;
  }

  public CosmeticItem availability(@Nullable CosmeticItemAvailability availability) {
    this.availability = availability;
    return this;
  }

  /**
   * Get availability
   * @return availability
   */
  @Valid 
  @Schema(name = "availability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availability")
  public @Nullable CosmeticItemAvailability getAvailability() {
    return availability;
  }

  public void setAvailability(@Nullable CosmeticItemAvailability availability) {
    this.availability = availability;
  }

  public CosmeticItem assets(@Nullable CosmeticItemAssets assets) {
    this.assets = assets;
    return this;
  }

  /**
   * Get assets
   * @return assets
   */
  @Valid 
  @Schema(name = "assets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assets")
  public @Nullable CosmeticItemAssets getAssets() {
    return assets;
  }

  public void setAssets(@Nullable CosmeticItemAssets assets) {
    this.assets = assets;
  }

  public CosmeticItem metadata(@Nullable CosmeticItemMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  @Valid 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public @Nullable CosmeticItemMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(@Nullable CosmeticItemMetadata metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticItem cosmeticItem = (CosmeticItem) o;
    return Objects.equals(this.itemId, cosmeticItem.itemId) &&
        Objects.equals(this.code, cosmeticItem.code) &&
        Objects.equals(this.name, cosmeticItem.name) &&
        Objects.equals(this.description, cosmeticItem.description) &&
        Objects.equals(this.type, cosmeticItem.type) &&
        Objects.equals(this.category, cosmeticItem.category) &&
        Objects.equals(this.rarity, cosmeticItem.rarity) &&
        Objects.equals(this.price, cosmeticItem.price) &&
        Objects.equals(this.exclusivity, cosmeticItem.exclusivity) &&
        Objects.equals(this.availability, cosmeticItem.availability) &&
        Objects.equals(this.assets, cosmeticItem.assets) &&
        Objects.equals(this.metadata, cosmeticItem.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, code, name, description, type, category, rarity, price, exclusivity, availability, assets, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    exclusivity: ").append(toIndentedString(exclusivity)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

