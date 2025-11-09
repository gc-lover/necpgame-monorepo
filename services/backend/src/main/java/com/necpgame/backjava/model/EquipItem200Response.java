package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.EquipmentSlot;
import com.necpgame.backjava.model.InventoryItem;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EquipItem200Response
 */

@JsonTypeName("equipItem_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class EquipItem200Response {

  private Boolean success;

  private String message;

  private EquipmentSlot equipment;

  private @Nullable InventoryItem unequippedItem;

  public EquipItem200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EquipItem200Response(Boolean success, String message, EquipmentSlot equipment) {
    this.success = success;
    this.message = message;
    this.equipment = equipment;
  }

  public EquipItem200Response success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  @NotNull 
  @Schema(name = "success", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public EquipItem200Response message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "РџСЂРµРґРјРµС‚ СѓСЃРїРµС€РЅРѕ СЌРєРёРїРёСЂРѕРІР°РЅ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public EquipItem200Response equipment(EquipmentSlot equipment) {
    this.equipment = equipment;
    return this;
  }

  /**
   * Get equipment
   * @return equipment
   */
  @NotNull @Valid 
  @Schema(name = "equipment", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("equipment")
  public EquipmentSlot getEquipment() {
    return equipment;
  }

  public void setEquipment(EquipmentSlot equipment) {
    this.equipment = equipment;
  }

  public EquipItem200Response unequippedItem(@Nullable InventoryItem unequippedItem) {
    this.unequippedItem = unequippedItem;
    return this;
  }

  /**
   * Get unequippedItem
   * @return unequippedItem
   */
  @Valid 
  @Schema(name = "unequippedItem", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unequippedItem")
  public @Nullable InventoryItem getUnequippedItem() {
    return unequippedItem;
  }

  public void setUnequippedItem(@Nullable InventoryItem unequippedItem) {
    this.unequippedItem = unequippedItem;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EquipItem200Response equipItem200Response = (EquipItem200Response) o;
    return Objects.equals(this.success, equipItem200Response.success) &&
        Objects.equals(this.message, equipItem200Response.message) &&
        Objects.equals(this.equipment, equipItem200Response.equipment) &&
        Objects.equals(this.unequippedItem, equipItem200Response.unequippedItem);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, equipment, unequippedItem);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EquipItem200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    equipment: ").append(toIndentedString(equipment)).append("\n");
    sb.append("    unequippedItem: ").append(toIndentedString(unequippedItem)).append("\n");
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


