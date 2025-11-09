package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Objects;

/**
 * GameStartingItem - СЃС‚Р°СЂС‚РѕРІС‹Р№ РїСЂРµРґРјРµС‚
 */
@Schema(description = "РЎС‚Р°СЂС‚РѕРІС‹Р№ РїСЂРµРґРјРµС‚")
public class GameStartingItem {

    @JsonProperty("itemId")
    private String itemId;

    @JsonProperty("quantity")
    private Integer quantity;

    @Schema(description = "ID РїСЂРµРґРјРµС‚Р° РёР· Р±Р°Р·С‹ РґР°РЅРЅС‹С…", example = "item-pistol-liberty", required = true)
    @NotNull
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Schema(description = "РљРѕР»РёС‡РµСЃС‚РІРѕ РїСЂРµРґРјРµС‚РѕРІ", example = "1", required = true)
    @NotNull
    @Min(1)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStartingItem that = (GameStartingItem) o;
        return Objects.equals(itemId, that.itemId) &&
               Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, quantity);
    }

    @Override
    public String toString() {
        return "GameStartingItem{" +
                "itemId='" + itemId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

