package com.necpgame.backjava.model;

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
 * РљР°С‚РµРіРѕСЂРёСЏ РїСЂРµРґРјРµС‚Р°: - weapons: РћСЂСѓР¶РёРµ (РїРёСЃС‚РѕР»РµС‚С‹, РІРёРЅС‚РѕРІРєРё, РЅРѕР¶Рё) - armor: Р‘СЂРѕРЅСЏ (РіРѕР»РѕРІР°, С‚РµР»Рѕ, СЂСѓРєРё, РЅРѕРіРё) - implants: РРјРїР»Р°РЅС‚С‹ (РєРёР±РµСЂРЅРµС‚РёРєР°) - consumables: Р Р°СЃС…РѕРґРЅРёРєРё (РјРµРґРёРєР°РјРµРЅС‚С‹, РµРґР°, СЌРЅРµСЂРіРµС‚РёРєРё) - resources: Р РµСЃСѓСЂСЃС‹ РґР»СЏ РєСЂР°С„С‚Р° - quest_items: РљРІРµСЃС‚РѕРІС‹Рµ РїСЂРµРґРјРµС‚С‹ - misc: РџСЂРѕС‡РµРµ 
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
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


