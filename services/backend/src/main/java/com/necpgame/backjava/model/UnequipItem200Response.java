package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * UnequipItem200Response
 */

@JsonTypeName("unequipItem_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:45.778329200+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class UnequipItem200Response {

  private Boolean success;

  private String message;

  private InventoryItem item;

  public UnequipItem200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UnequipItem200Response(Boolean success, String message, InventoryItem item) {
    this.success = success;
    this.message = message;
    this.item = item;
  }

  public UnequipItem200Response success(Boolean success) {
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

  public UnequipItem200Response message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "РџСЂРµРґРјРµС‚ СѓСЃРїРµС€РЅРѕ СЃРЅСЏС‚", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public UnequipItem200Response item(InventoryItem item) {
    this.item = item;
    return this;
  }

  /**
   * Get item
   * @return item
   */
  @NotNull @Valid 
  @Schema(name = "item", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item")
  public InventoryItem getItem() {
    return item;
  }

  public void setItem(InventoryItem item) {
    this.item = item;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnequipItem200Response unequipItem200Response = (UnequipItem200Response) o;
    return Objects.equals(this.success, unequipItem200Response.success) &&
        Objects.equals(this.message, unequipItem200Response.message) &&
        Objects.equals(this.item, unequipItem200Response.item);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, item);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnequipItem200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
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


