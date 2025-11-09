package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.inventoryservice.model.InventoryItemRequirements;
import com.necpgame.inventoryservice.model.ItemCategory;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InventoryItem
 */


public class InventoryItem {

  private String id;

  private String name;

  private String description;

  private ItemCategory category;

  private Float weight;

  private Integer quantity;

  private Boolean stackable;

  /**
   * Редкость предмета
   */
  public enum RarityEnum {
    COMMON("common"),
    
    UNCOMMON("uncommon"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary");

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

  private @Nullable Integer value;

  private @Nullable Boolean equippable;

  private @Nullable Boolean usable;

  private @Nullable Boolean questItem;

  private @Nullable InventoryItemRequirements requirements;

  public InventoryItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InventoryItem(String id, String name, String description, ItemCategory category, Float weight, Integer quantity, Boolean stackable) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.category = category;
    this.weight = weight;
    this.quantity = quantity;
    this.stackable = stackable;
  }

  public InventoryItem id(String id) {
    this.id = id;
    return this;
  }

  /**
   * ID предмета
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "item_pistol_01", description = "ID предмета", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public InventoryItem name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название предмета
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "M-10AF Lexington", description = "Название предмета", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public InventoryItem description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание предмета
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Стандартный пистолет калибра 10мм", description = "Описание предмета", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public InventoryItem category(ItemCategory category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull @Valid 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public ItemCategory getCategory() {
    return category;
  }

  public void setCategory(ItemCategory category) {
    this.category = category;
  }

  public InventoryItem weight(Float weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Вес одного предмета (кг)
   * @return weight
   */
  @NotNull 
  @Schema(name = "weight", example = "1.5", description = "Вес одного предмета (кг)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weight")
  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public InventoryItem quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Количество предметов (для stackable)
   * minimum: 1
   * @return quantity
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "quantity", example = "1", description = "Количество предметов (для stackable)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public InventoryItem stackable(Boolean stackable) {
    this.stackable = stackable;
    return this;
  }

  /**
   * Можно ли складывать предметы
   * @return stackable
   */
  @NotNull 
  @Schema(name = "stackable", example = "false", description = "Можно ли складывать предметы", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stackable")
  public Boolean getStackable() {
    return stackable;
  }

  public void setStackable(Boolean stackable) {
    this.stackable = stackable;
  }

  public InventoryItem rarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Редкость предмета
   * @return rarity
   */
  
  @Schema(name = "rarity", example = "common", description = "Редкость предмета", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
  }

  public InventoryItem value(@Nullable Integer value) {
    this.value = value;
    return this;
  }

  /**
   * Стоимость предмета (eddies)
   * @return value
   */
  
  @Schema(name = "value", example = "500", description = "Стоимость предмета (eddies)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable Integer getValue() {
    return value;
  }

  public void setValue(@Nullable Integer value) {
    this.value = value;
  }

  public InventoryItem equippable(@Nullable Boolean equippable) {
    this.equippable = equippable;
    return this;
  }

  /**
   * Можно ли экипировать предмет
   * @return equippable
   */
  
  @Schema(name = "equippable", example = "true", description = "Можно ли экипировать предмет", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equippable")
  public @Nullable Boolean getEquippable() {
    return equippable;
  }

  public void setEquippable(@Nullable Boolean equippable) {
    this.equippable = equippable;
  }

  public InventoryItem usable(@Nullable Boolean usable) {
    this.usable = usable;
    return this;
  }

  /**
   * Можно ли использовать предмет
   * @return usable
   */
  
  @Schema(name = "usable", example = "false", description = "Можно ли использовать предмет", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usable")
  public @Nullable Boolean getUsable() {
    return usable;
  }

  public void setUsable(@Nullable Boolean usable) {
    this.usable = usable;
  }

  public InventoryItem questItem(@Nullable Boolean questItem) {
    this.questItem = questItem;
    return this;
  }

  /**
   * Является ли квестовым предметом
   * @return questItem
   */
  
  @Schema(name = "questItem", example = "false", description = "Является ли квестовым предметом", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questItem")
  public @Nullable Boolean getQuestItem() {
    return questItem;
  }

  public void setQuestItem(@Nullable Boolean questItem) {
    this.questItem = questItem;
  }

  public InventoryItem requirements(@Nullable InventoryItemRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable InventoryItemRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable InventoryItemRequirements requirements) {
    this.requirements = requirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryItem inventoryItem = (InventoryItem) o;
    return Objects.equals(this.id, inventoryItem.id) &&
        Objects.equals(this.name, inventoryItem.name) &&
        Objects.equals(this.description, inventoryItem.description) &&
        Objects.equals(this.category, inventoryItem.category) &&
        Objects.equals(this.weight, inventoryItem.weight) &&
        Objects.equals(this.quantity, inventoryItem.quantity) &&
        Objects.equals(this.stackable, inventoryItem.stackable) &&
        Objects.equals(this.rarity, inventoryItem.rarity) &&
        Objects.equals(this.value, inventoryItem.value) &&
        Objects.equals(this.equippable, inventoryItem.equippable) &&
        Objects.equals(this.usable, inventoryItem.usable) &&
        Objects.equals(this.questItem, inventoryItem.questItem) &&
        Objects.equals(this.requirements, inventoryItem.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, category, weight, quantity, stackable, rarity, value, equippable, usable, questItem, requirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryItem {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    stackable: ").append(toIndentedString(stackable)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    equippable: ").append(toIndentedString(equippable)).append("\n");
    sb.append("    usable: ").append(toIndentedString(usable)).append("\n");
    sb.append("    questItem: ").append(toIndentedString(questItem)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
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

