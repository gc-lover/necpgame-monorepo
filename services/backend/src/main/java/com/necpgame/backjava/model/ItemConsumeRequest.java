package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ItemConsumeRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ItemConsumeRequest {

  private String itemInstanceId;

  private @Nullable Boolean consumeAll;

  public ItemConsumeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemConsumeRequest(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public ItemConsumeRequest itemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
    return this;
  }

  /**
   * Get itemInstanceId
   * @return itemInstanceId
   */
  @NotNull 
  @Schema(name = "itemInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemInstanceId")
  public String getItemInstanceId() {
    return itemInstanceId;
  }

  public void setItemInstanceId(String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public ItemConsumeRequest consumeAll(@Nullable Boolean consumeAll) {
    this.consumeAll = consumeAll;
    return this;
  }

  /**
   * Get consumeAll
   * @return consumeAll
   */
  
  @Schema(name = "consumeAll", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consumeAll")
  public @Nullable Boolean getConsumeAll() {
    return consumeAll;
  }

  public void setConsumeAll(@Nullable Boolean consumeAll) {
    this.consumeAll = consumeAll;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemConsumeRequest itemConsumeRequest = (ItemConsumeRequest) o;
    return Objects.equals(this.itemInstanceId, itemConsumeRequest.itemInstanceId) &&
        Objects.equals(this.consumeAll, itemConsumeRequest.consumeAll);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemInstanceId, consumeAll);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemConsumeRequest {\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
    sb.append("    consumeAll: ").append(toIndentedString(consumeAll)).append("\n");
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

