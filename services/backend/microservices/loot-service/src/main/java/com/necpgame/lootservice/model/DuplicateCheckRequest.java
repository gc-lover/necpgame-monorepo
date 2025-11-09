package com.necpgame.lootservice.model;

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
 * DuplicateCheckRequest
 */


public class DuplicateCheckRequest {

  private String playerId;

  private String itemId;

  private @Nullable Boolean includeStash;

  public DuplicateCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DuplicateCheckRequest(String playerId, String itemId) {
    this.playerId = playerId;
    this.itemId = itemId;
  }

  public DuplicateCheckRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public DuplicateCheckRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public DuplicateCheckRequest includeStash(@Nullable Boolean includeStash) {
    this.includeStash = includeStash;
    return this;
  }

  /**
   * Get includeStash
   * @return includeStash
   */
  
  @Schema(name = "includeStash", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeStash")
  public @Nullable Boolean getIncludeStash() {
    return includeStash;
  }

  public void setIncludeStash(@Nullable Boolean includeStash) {
    this.includeStash = includeStash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DuplicateCheckRequest duplicateCheckRequest = (DuplicateCheckRequest) o;
    return Objects.equals(this.playerId, duplicateCheckRequest.playerId) &&
        Objects.equals(this.itemId, duplicateCheckRequest.itemId) &&
        Objects.equals(this.includeStash, duplicateCheckRequest.includeStash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, itemId, includeStash);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DuplicateCheckRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    includeStash: ").append(toIndentedString(includeStash)).append("\n");
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

