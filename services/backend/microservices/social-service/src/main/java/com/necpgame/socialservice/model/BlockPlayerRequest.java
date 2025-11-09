package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BlockPlayerRequest
 */

@JsonTypeName("blockPlayer_request")

public class BlockPlayerRequest {

  private String playerId;

  private String targetPlayerId;

  public BlockPlayerRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BlockPlayerRequest(String playerId, String targetPlayerId) {
    this.playerId = playerId;
    this.targetPlayerId = targetPlayerId;
  }

  public BlockPlayerRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player_id")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public BlockPlayerRequest targetPlayerId(String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
    return this;
  }

  /**
   * Get targetPlayerId
   * @return targetPlayerId
   */
  @NotNull 
  @Schema(name = "target_player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_player_id")
  public String getTargetPlayerId() {
    return targetPlayerId;
  }

  public void setTargetPlayerId(String targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlockPlayerRequest blockPlayerRequest = (BlockPlayerRequest) o;
    return Objects.equals(this.playerId, blockPlayerRequest.playerId) &&
        Objects.equals(this.targetPlayerId, blockPlayerRequest.targetPlayerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, targetPlayerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlockPlayerRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    targetPlayerId: ").append(toIndentedString(targetPlayerId)).append("\n");
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

