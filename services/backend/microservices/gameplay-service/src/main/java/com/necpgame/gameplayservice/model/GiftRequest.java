package com.necpgame.gameplayservice.model;

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
 * GiftRequest
 */


public class GiftRequest {

  private String playerId;

  private String targetPlayerId;

  private String itemId;

  private @Nullable String message;

  private @Nullable String region;

  public GiftRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GiftRequest(String playerId, String targetPlayerId, String itemId) {
    this.playerId = playerId;
    this.targetPlayerId = targetPlayerId;
    this.itemId = itemId;
  }

  public GiftRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Отправитель
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", description = "Отправитель", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public GiftRequest targetPlayerId(String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
    return this;
  }

  /**
   * Get targetPlayerId
   * @return targetPlayerId
   */
  @NotNull 
  @Schema(name = "targetPlayerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetPlayerId")
  public String getTargetPlayerId() {
    return targetPlayerId;
  }

  public void setTargetPlayerId(String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
  }

  public GiftRequest itemId(String itemId) {
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

  public GiftRequest message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @Size(max = 250) 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public GiftRequest region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GiftRequest giftRequest = (GiftRequest) o;
    return Objects.equals(this.playerId, giftRequest.playerId) &&
        Objects.equals(this.targetPlayerId, giftRequest.targetPlayerId) &&
        Objects.equals(this.itemId, giftRequest.itemId) &&
        Objects.equals(this.message, giftRequest.message) &&
        Objects.equals(this.region, giftRequest.region);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, targetPlayerId, itemId, message, region);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GiftRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    targetPlayerId: ").append(toIndentedString(targetPlayerId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
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

