package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ItemGrant;
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
 * ItemPickupRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ItemPickupRequest {

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    LOOT("LOOT"),
    
    QUEST("QUEST"),
    
    GRANT("GRANT"),
    
    TRADE("TRADE"),
    
    MAIL("MAIL");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SourceEnum source;

  @Valid
  private List<@Valid ItemGrant> items = new ArrayList<>();

  private @Nullable String idempotencyKey;

  public ItemPickupRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemPickupRequest(SourceEnum source, List<@Valid ItemGrant> items) {
    this.source = source;
    this.items = items;
  }

  public ItemPickupRequest source(SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public ItemPickupRequest items(List<@Valid ItemGrant> items) {
    this.items = items;
    return this;
  }

  public ItemPickupRequest addItemsItem(ItemGrant itemsItem) {
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
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid ItemGrant> getItems() {
    return items;
  }

  public void setItems(List<@Valid ItemGrant> items) {
    this.items = items;
  }

  public ItemPickupRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemPickupRequest itemPickupRequest = (ItemPickupRequest) o;
    return Objects.equals(this.source, itemPickupRequest.source) &&
        Objects.equals(this.items, itemPickupRequest.items) &&
        Objects.equals(this.idempotencyKey, itemPickupRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, items, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemPickupRequest {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

