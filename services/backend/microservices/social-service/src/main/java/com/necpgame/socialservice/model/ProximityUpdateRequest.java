package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ProximityUpdateRequestCoordinates;
import com.necpgame.socialservice.model.ProximityUpdateRequestVelocity;
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
 * ProximityUpdateRequest
 */


public class ProximityUpdateRequest {

  private String playerId;

  private String worldId;

  private ProximityUpdateRequestCoordinates coordinates;

  private @Nullable ProximityUpdateRequestVelocity velocity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  public ProximityUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProximityUpdateRequest(String playerId, String worldId, ProximityUpdateRequestCoordinates coordinates, OffsetDateTime timestamp) {
    this.playerId = playerId;
    this.worldId = worldId;
    this.coordinates = coordinates;
    this.timestamp = timestamp;
  }

  public ProximityUpdateRequest playerId(String playerId) {
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

  public ProximityUpdateRequest worldId(String worldId) {
    this.worldId = worldId;
    return this;
  }

  /**
   * Get worldId
   * @return worldId
   */
  @NotNull 
  @Schema(name = "worldId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("worldId")
  public String getWorldId() {
    return worldId;
  }

  public void setWorldId(String worldId) {
    this.worldId = worldId;
  }

  public ProximityUpdateRequest coordinates(ProximityUpdateRequestCoordinates coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  /**
   * Get coordinates
   * @return coordinates
   */
  @NotNull @Valid 
  @Schema(name = "coordinates", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("coordinates")
  public ProximityUpdateRequestCoordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(ProximityUpdateRequestCoordinates coordinates) {
    this.coordinates = coordinates;
  }

  public ProximityUpdateRequest velocity(@Nullable ProximityUpdateRequestVelocity velocity) {
    this.velocity = velocity;
    return this;
  }

  /**
   * Get velocity
   * @return velocity
   */
  @Valid 
  @Schema(name = "velocity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("velocity")
  public @Nullable ProximityUpdateRequestVelocity getVelocity() {
    return velocity;
  }

  public void setVelocity(@Nullable ProximityUpdateRequestVelocity velocity) {
    this.velocity = velocity;
  }

  public ProximityUpdateRequest timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProximityUpdateRequest proximityUpdateRequest = (ProximityUpdateRequest) o;
    return Objects.equals(this.playerId, proximityUpdateRequest.playerId) &&
        Objects.equals(this.worldId, proximityUpdateRequest.worldId) &&
        Objects.equals(this.coordinates, proximityUpdateRequest.coordinates) &&
        Objects.equals(this.velocity, proximityUpdateRequest.velocity) &&
        Objects.equals(this.timestamp, proximityUpdateRequest.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, worldId, coordinates, velocity, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProximityUpdateRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    worldId: ").append(toIndentedString(worldId)).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
    sb.append("    velocity: ").append(toIndentedString(velocity)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

