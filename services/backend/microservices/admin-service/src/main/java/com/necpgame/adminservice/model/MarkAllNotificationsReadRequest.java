package com.necpgame.adminservice.model;

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
 * MarkAllNotificationsReadRequest
 */

@JsonTypeName("markAllNotificationsRead_request")

public class MarkAllNotificationsReadRequest {

  private String playerId;

  public MarkAllNotificationsReadRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MarkAllNotificationsReadRequest(String playerId) {
    this.playerId = playerId;
  }

  public MarkAllNotificationsReadRequest playerId(String playerId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarkAllNotificationsReadRequest markAllNotificationsReadRequest = (MarkAllNotificationsReadRequest) o;
    return Objects.equals(this.playerId, markAllNotificationsReadRequest.playerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarkAllNotificationsReadRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
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

