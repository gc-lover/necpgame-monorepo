package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ShardInfoPlayerIdRange;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ShardInfo
 */


public class ShardInfo {

  private @Nullable String shardId;

  private @Nullable ShardInfoPlayerIdRange playerIdRange;

  private @Nullable BigDecimal sizeGb;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    READONLY("readonly"),
    
    MIGRATING("migrating");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  public ShardInfo shardId(@Nullable String shardId) {
    this.shardId = shardId;
    return this;
  }

  /**
   * Get shardId
   * @return shardId
   */
  
  @Schema(name = "shard_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shard_id")
  public @Nullable String getShardId() {
    return shardId;
  }

  public void setShardId(@Nullable String shardId) {
    this.shardId = shardId;
  }

  public ShardInfo playerIdRange(@Nullable ShardInfoPlayerIdRange playerIdRange) {
    this.playerIdRange = playerIdRange;
    return this;
  }

  /**
   * Get playerIdRange
   * @return playerIdRange
   */
  @Valid 
  @Schema(name = "player_id_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id_range")
  public @Nullable ShardInfoPlayerIdRange getPlayerIdRange() {
    return playerIdRange;
  }

  public void setPlayerIdRange(@Nullable ShardInfoPlayerIdRange playerIdRange) {
    this.playerIdRange = playerIdRange;
  }

  public ShardInfo sizeGb(@Nullable BigDecimal sizeGb) {
    this.sizeGb = sizeGb;
    return this;
  }

  /**
   * Get sizeGb
   * @return sizeGb
   */
  @Valid 
  @Schema(name = "size_gb", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size_gb")
  public @Nullable BigDecimal getSizeGb() {
    return sizeGb;
  }

  public void setSizeGb(@Nullable BigDecimal sizeGb) {
    this.sizeGb = sizeGb;
  }

  public ShardInfo status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShardInfo shardInfo = (ShardInfo) o;
    return Objects.equals(this.shardId, shardInfo.shardId) &&
        Objects.equals(this.playerIdRange, shardInfo.playerIdRange) &&
        Objects.equals(this.sizeGb, shardInfo.sizeGb) &&
        Objects.equals(this.status, shardInfo.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shardId, playerIdRange, sizeGb, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShardInfo {\n");
    sb.append("    shardId: ").append(toIndentedString(shardId)).append("\n");
    sb.append("    playerIdRange: ").append(toIndentedString(playerIdRange)).append("\n");
    sb.append("    sizeGb: ").append(toIndentedString(sizeGb)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

