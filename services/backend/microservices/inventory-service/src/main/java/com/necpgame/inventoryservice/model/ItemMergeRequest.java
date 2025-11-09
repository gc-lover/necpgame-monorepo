package com.necpgame.inventoryservice.model;

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
 * ItemMergeRequest
 */


public class ItemMergeRequest {

  private String sourceItemInstanceId;

  private String targetItemInstanceId;

  public ItemMergeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemMergeRequest(String sourceItemInstanceId, String targetItemInstanceId) {
    this.sourceItemInstanceId = sourceItemInstanceId;
    this.targetItemInstanceId = targetItemInstanceId;
  }

  public ItemMergeRequest sourceItemInstanceId(String sourceItemInstanceId) {
    this.sourceItemInstanceId = sourceItemInstanceId;
    return this;
  }

  /**
   * Get sourceItemInstanceId
   * @return sourceItemInstanceId
   */
  @NotNull 
  @Schema(name = "sourceItemInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sourceItemInstanceId")
  public String getSourceItemInstanceId() {
    return sourceItemInstanceId;
  }

  public void setSourceItemInstanceId(String sourceItemInstanceId) {
    this.sourceItemInstanceId = sourceItemInstanceId;
  }

  public ItemMergeRequest targetItemInstanceId(String targetItemInstanceId) {
    this.targetItemInstanceId = targetItemInstanceId;
    return this;
  }

  /**
   * Get targetItemInstanceId
   * @return targetItemInstanceId
   */
  @NotNull 
  @Schema(name = "targetItemInstanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetItemInstanceId")
  public String getTargetItemInstanceId() {
    return targetItemInstanceId;
  }

  public void setTargetItemInstanceId(String targetItemInstanceId) {
    this.targetItemInstanceId = targetItemInstanceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemMergeRequest itemMergeRequest = (ItemMergeRequest) o;
    return Objects.equals(this.sourceItemInstanceId, itemMergeRequest.sourceItemInstanceId) &&
        Objects.equals(this.targetItemInstanceId, itemMergeRequest.targetItemInstanceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceItemInstanceId, targetItemInstanceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemMergeRequest {\n");
    sb.append("    sourceItemInstanceId: ").append(toIndentedString(sourceItemInstanceId)).append("\n");
    sb.append("    targetItemInstanceId: ").append(toIndentedString(targetItemInstanceId)).append("\n");
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

