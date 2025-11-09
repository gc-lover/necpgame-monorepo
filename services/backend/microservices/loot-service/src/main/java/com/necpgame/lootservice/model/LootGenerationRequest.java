package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.lootservice.model.LootGenerationContext;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootGenerationRequest
 */


public class LootGenerationRequest {

  private String tableId;

  /**
   * Gets or Sets distributionHint
   */
  public enum DistributionHintEnum {
    PERSONAL("PERSONAL"),
    
    SHARED("SHARED"),
    
    RAID("RAID");

    private final String value;

    DistributionHintEnum(String value) {
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
    public static DistributionHintEnum fromValue(String value) {
      for (DistributionHintEnum b : DistributionHintEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DistributionHintEnum distributionHint;

  private LootGenerationContext context;

  public LootGenerationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootGenerationRequest(String tableId, LootGenerationContext context) {
    this.tableId = tableId;
    this.context = context;
  }

  public LootGenerationRequest tableId(String tableId) {
    this.tableId = tableId;
    return this;
  }

  /**
   * Get tableId
   * @return tableId
   */
  @NotNull 
  @Schema(name = "tableId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tableId")
  public String getTableId() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  public LootGenerationRequest distributionHint(@Nullable DistributionHintEnum distributionHint) {
    this.distributionHint = distributionHint;
    return this;
  }

  /**
   * Get distributionHint
   * @return distributionHint
   */
  
  @Schema(name = "distributionHint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distributionHint")
  public @Nullable DistributionHintEnum getDistributionHint() {
    return distributionHint;
  }

  public void setDistributionHint(@Nullable DistributionHintEnum distributionHint) {
    this.distributionHint = distributionHint;
  }

  public LootGenerationRequest context(LootGenerationContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @NotNull @Valid 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("context")
  public LootGenerationContext getContext() {
    return context;
  }

  public void setContext(LootGenerationContext context) {
    this.context = context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootGenerationRequest lootGenerationRequest = (LootGenerationRequest) o;
    return Objects.equals(this.tableId, lootGenerationRequest.tableId) &&
        Objects.equals(this.distributionHint, lootGenerationRequest.distributionHint) &&
        Objects.equals(this.context, lootGenerationRequest.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tableId, distributionHint, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootGenerationRequest {\n");
    sb.append("    tableId: ").append(toIndentedString(tableId)).append("\n");
    sb.append("    distributionHint: ").append(toIndentedString(distributionHint)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

