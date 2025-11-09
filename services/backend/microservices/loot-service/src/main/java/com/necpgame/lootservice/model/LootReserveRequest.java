package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.lootservice.model.LootItem;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootReserveRequest
 */


public class LootReserveRequest {

  private UUID resultId;

  private LootItem item;

  /**
   * Gets or Sets target
   */
  public enum TargetEnum {
    INVENTORY("INVENTORY"),
    
    MAIL("MAIL"),
    
    TRADE("TRADE"),
    
    VENDOR("VENDOR");

    private final String value;

    TargetEnum(String value) {
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
    public static TargetEnum fromValue(String value) {
      for (TargetEnum b : TargetEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TargetEnum target;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public LootReserveRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootReserveRequest(UUID resultId, LootItem item) {
    this.resultId = resultId;
    this.item = item;
  }

  public LootReserveRequest resultId(UUID resultId) {
    this.resultId = resultId;
    return this;
  }

  /**
   * Get resultId
   * @return resultId
   */
  @NotNull @Valid 
  @Schema(name = "resultId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resultId")
  public UUID getResultId() {
    return resultId;
  }

  public void setResultId(UUID resultId) {
    this.resultId = resultId;
  }

  public LootReserveRequest item(LootItem item) {
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
  public LootItem getItem() {
    return item;
  }

  public void setItem(LootItem item) {
    this.item = item;
  }

  public LootReserveRequest target(@Nullable TargetEnum target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  
  @Schema(name = "target", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target")
  public @Nullable TargetEnum getTarget() {
    return target;
  }

  public void setTarget(@Nullable TargetEnum target) {
    this.target = target;
  }

  public LootReserveRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootReserveRequest lootReserveRequest = (LootReserveRequest) o;
    return Objects.equals(this.resultId, lootReserveRequest.resultId) &&
        Objects.equals(this.item, lootReserveRequest.item) &&
        Objects.equals(this.target, lootReserveRequest.target) &&
        Objects.equals(this.expiresAt, lootReserveRequest.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultId, item, target, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootReserveRequest {\n");
    sb.append("    resultId: ").append(toIndentedString(resultId)).append("\n");
    sb.append("    item: ").append(toIndentedString(item)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

