package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.BlockEntry;
import java.time.OffsetDateTime;
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
 * BlockEvent
 */


public class BlockEvent {

  private @Nullable BlockEntry block;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public BlockEvent block(@Nullable BlockEntry block) {
    this.block = block;
    return this;
  }

  /**
   * Get block
   * @return block
   */
  @Valid 
  @Schema(name = "block", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("block")
  public @Nullable BlockEntry getBlock() {
    return block;
  }

  public void setBlock(@Nullable BlockEntry block) {
    this.block = block;
  }

  public BlockEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurredAt")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlockEvent blockEvent = (BlockEvent) o;
    return Objects.equals(this.block, blockEvent.block) &&
        Objects.equals(this.occurredAt, blockEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(block, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlockEvent {\n");
    sb.append("    block: ").append(toIndentedString(block)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

