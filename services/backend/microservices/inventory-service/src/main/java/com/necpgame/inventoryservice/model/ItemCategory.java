package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Категория предмета: - weapons: Оружие (пистолеты, винтовки, ножи) - armor: Броня (голова, тело, руки, ноги) - implants: Импланты (кибернетика) - consumables: Расходники (медикаменты, еда, энергетики) - resources: Ресурсы для крафта - quest_items: Квестовые предметы - misc: Прочее 
 */


public enum ItemCategory {
  
  WEAPONS("weapons"),
  
  ARMOR("armor"),
  
  IMPLANTS("implants"),
  
  CONSUMABLES("consumables"),
  
  RESOURCES("resources"),
  
  QUEST_ITEMS("quest_items"),
  
  MISC("misc");

  private final String value;

  ItemCategory(String value) {
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
  public static ItemCategory fromValue(String value) {
    for (ItemCategory b : ItemCategory.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

