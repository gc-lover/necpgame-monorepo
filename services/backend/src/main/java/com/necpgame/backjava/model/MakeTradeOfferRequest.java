package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.MakeTradeOfferRequestItemsInner;
import java.math.BigDecimal;
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
 * MakeTradeOfferRequest
 */

@JsonTypeName("makeTradeOffer_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MakeTradeOfferRequest {

  private @Nullable String characterId;

  @Valid
  private List<@Valid MakeTradeOfferRequestItemsInner> items = new ArrayList<>();

  private @Nullable BigDecimal gold;

  public MakeTradeOfferRequest characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public MakeTradeOfferRequest items(List<@Valid MakeTradeOfferRequestItemsInner> items) {
    this.items = items;
    return this;
  }

  public MakeTradeOfferRequest addItemsItem(MakeTradeOfferRequestItemsInner itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid MakeTradeOfferRequestItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid MakeTradeOfferRequestItemsInner> items) {
    this.items = items;
  }

  public MakeTradeOfferRequest gold(@Nullable BigDecimal gold) {
    this.gold = gold;
    return this;
  }

  /**
   * Get gold
   * @return gold
   */
  @Valid 
  @Schema(name = "gold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gold")
  public @Nullable BigDecimal getGold() {
    return gold;
  }

  public void setGold(@Nullable BigDecimal gold) {
    this.gold = gold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MakeTradeOfferRequest makeTradeOfferRequest = (MakeTradeOfferRequest) o;
    return Objects.equals(this.characterId, makeTradeOfferRequest.characterId) &&
        Objects.equals(this.items, makeTradeOfferRequest.items) &&
        Objects.equals(this.gold, makeTradeOfferRequest.gold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, items, gold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MakeTradeOfferRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    gold: ").append(toIndentedString(gold)).append("\n");
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

