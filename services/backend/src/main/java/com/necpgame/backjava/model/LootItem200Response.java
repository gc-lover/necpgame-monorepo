package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootItem200Response
 */

@JsonTypeName("lootItem_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootItem200Response {

  private @Nullable Boolean success;

  private @Nullable String itemId;

  private @Nullable Boolean rollRequired;

  private @Nullable String rollId;

  public LootItem200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public LootItem200Response itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public LootItem200Response rollRequired(@Nullable Boolean rollRequired) {
    this.rollRequired = rollRequired;
    return this;
  }

  /**
   * Get rollRequired
   * @return rollRequired
   */
  
  @Schema(name = "roll_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll_required")
  public @Nullable Boolean getRollRequired() {
    return rollRequired;
  }

  public void setRollRequired(@Nullable Boolean rollRequired) {
    this.rollRequired = rollRequired;
  }

  public LootItem200Response rollId(@Nullable String rollId) {
    this.rollId = rollId;
    return this;
  }

  /**
   * Get rollId
   * @return rollId
   */
  
  @Schema(name = "roll_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll_id")
  public @Nullable String getRollId() {
    return rollId;
  }

  public void setRollId(@Nullable String rollId) {
    this.rollId = rollId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootItem200Response lootItem200Response = (LootItem200Response) o;
    return Objects.equals(this.success, lootItem200Response.success) &&
        Objects.equals(this.itemId, lootItem200Response.itemId) &&
        Objects.equals(this.rollRequired, lootItem200Response.rollRequired) &&
        Objects.equals(this.rollId, lootItem200Response.rollId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, itemId, rollRequired, rollId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootItem200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    rollRequired: ").append(toIndentedString(rollRequired)).append("\n");
    sb.append("    rollId: ").append(toIndentedString(rollId)).append("\n");
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

