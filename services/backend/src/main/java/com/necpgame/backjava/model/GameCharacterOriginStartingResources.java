package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameCharacterOriginStartingResources
 */

@JsonTypeName("GameCharacterOrigin_starting_resources")

public class GameCharacterOriginStartingResources {

  private Integer currency;

  @Valid
  private List<String> items = new ArrayList<>();

  public GameCharacterOriginStartingResources() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacterOriginStartingResources(Integer currency, List<String> items) {
    this.currency = currency;
    this.items = items;
  }

  public GameCharacterOriginStartingResources currency(Integer currency) {
    this.currency = currency;
    return this;
  }

  /**
   * РЎС‚Р°СЂС‚РѕРІР°СЏ РІР°Р»СЋС‚Р°
   * @return currency
   */
  @NotNull 
  @Schema(name = "currency", example = "1000", description = "РЎС‚Р°СЂС‚РѕРІР°СЏ РІР°Р»СЋС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currency")
  public Integer getCurrency() {
    return currency;
  }

  public void setCurrency(Integer currency) {
    this.currency = currency;
  }

  public GameCharacterOriginStartingResources items(List<String> items) {
    this.items = items;
    return this;
  }

  public GameCharacterOriginStartingResources addItemsItem(String itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * РЎРїРёСЃРѕРє СЃС‚Р°СЂС‚РѕРІС‹С… РїСЂРµРґРјРµС‚РѕРІ
   * @return items
   */
  @NotNull 
  @Schema(name = "items", example = "[\"basic_pistol\",\"street_clothes\"]", description = "РЎРїРёСЃРѕРє СЃС‚Р°СЂС‚РѕРІС‹С… РїСЂРµРґРјРµС‚РѕРІ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacterOriginStartingResources gameCharacterOriginStartingResources = (GameCharacterOriginStartingResources) o;
    return Objects.equals(this.currency, gameCharacterOriginStartingResources.currency) &&
        Objects.equals(this.items, gameCharacterOriginStartingResources.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currency, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCharacterOriginStartingResources {\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

