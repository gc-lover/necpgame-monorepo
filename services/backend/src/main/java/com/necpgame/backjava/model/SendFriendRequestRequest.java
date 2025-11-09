package com.necpgame.backjava.model;

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
 * SendFriendRequestRequest
 */

@JsonTypeName("sendFriendRequest_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SendFriendRequestRequest {

  private String playerId;

  private String targetPlayerName;

  public SendFriendRequestRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SendFriendRequestRequest(String playerId, String targetPlayerName) {
    this.playerId = playerId;
    this.targetPlayerName = targetPlayerName;
  }

  public SendFriendRequestRequest playerId(String playerId) {
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

  public SendFriendRequestRequest targetPlayerName(String targetPlayerName) {
    this.targetPlayerName = targetPlayerName;
    return this;
  }

  /**
   * Get targetPlayerName
   * @return targetPlayerName
   */
  @NotNull 
  @Schema(name = "target_player_name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_player_name")
  public String getTargetPlayerName() {
    return targetPlayerName;
  }

  public void setTargetPlayerName(String targetPlayerName) {
    this.targetPlayerName = targetPlayerName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendFriendRequestRequest sendFriendRequestRequest = (SendFriendRequestRequest) o;
    return Objects.equals(this.playerId, sendFriendRequestRequest.playerId) &&
        Objects.equals(this.targetPlayerName, sendFriendRequestRequest.targetPlayerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, targetPlayerName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendFriendRequestRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    targetPlayerName: ").append(toIndentedString(targetPlayerName)).append("\n");
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

