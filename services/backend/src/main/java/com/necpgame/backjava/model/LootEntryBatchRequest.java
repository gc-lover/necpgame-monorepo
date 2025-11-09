package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.LootEntryDefinition;
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
 * LootEntryBatchRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootEntryBatchRequest {

  @Valid
  private List<@Valid LootEntryDefinition> entries = new ArrayList<>();

  /**
   * Gets or Sets operation
   */
  public enum OperationEnum {
    UPSERT("UPSERT"),
    
    DELETE("DELETE");

    private final String value;

    OperationEnum(String value) {
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
    public static OperationEnum fromValue(String value) {
      for (OperationEnum b : OperationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OperationEnum operation;

  public LootEntryBatchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootEntryBatchRequest(List<@Valid LootEntryDefinition> entries) {
    this.entries = entries;
  }

  public LootEntryBatchRequest entries(List<@Valid LootEntryDefinition> entries) {
    this.entries = entries;
    return this;
  }

  public LootEntryBatchRequest addEntriesItem(LootEntryDefinition entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @NotNull @Valid 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("entries")
  public List<@Valid LootEntryDefinition> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid LootEntryDefinition> entries) {
    this.entries = entries;
  }

  public LootEntryBatchRequest operation(@Nullable OperationEnum operation) {
    this.operation = operation;
    return this;
  }

  /**
   * Get operation
   * @return operation
   */
  
  @Schema(name = "operation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operation")
  public @Nullable OperationEnum getOperation() {
    return operation;
  }

  public void setOperation(@Nullable OperationEnum operation) {
    this.operation = operation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootEntryBatchRequest lootEntryBatchRequest = (LootEntryBatchRequest) o;
    return Objects.equals(this.entries, lootEntryBatchRequest.entries) &&
        Objects.equals(this.operation, lootEntryBatchRequest.operation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries, operation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootEntryBatchRequest {\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
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

