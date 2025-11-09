package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ShipmentDetailedAllOfTrackingHistory
 */

@JsonTypeName("ShipmentDetailed_allOf_tracking_history")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ShipmentDetailedAllOfTrackingHistory {

  private @Nullable String location;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable String event;

  public ShipmentDetailedAllOfTrackingHistory location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public ShipmentDetailedAllOfTrackingHistory timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ShipmentDetailedAllOfTrackingHistory event(@Nullable String event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable String getEvent() {
    return event;
  }

  public void setEvent(@Nullable String event) {
    this.event = event;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentDetailedAllOfTrackingHistory shipmentDetailedAllOfTrackingHistory = (ShipmentDetailedAllOfTrackingHistory) o;
    return Objects.equals(this.location, shipmentDetailedAllOfTrackingHistory.location) &&
        Objects.equals(this.timestamp, shipmentDetailedAllOfTrackingHistory.timestamp) &&
        Objects.equals(this.event, shipmentDetailedAllOfTrackingHistory.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, timestamp, event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentDetailedAllOfTrackingHistory {\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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

