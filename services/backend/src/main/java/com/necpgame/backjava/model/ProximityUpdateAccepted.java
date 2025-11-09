package com.necpgame.backjava.model;

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
 * ProximityUpdateAccepted
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ProximityUpdateAccepted {

  private String trackingId;

  private Integer nextUpdateHint;

  public ProximityUpdateAccepted() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProximityUpdateAccepted(String trackingId, Integer nextUpdateHint) {
    this.trackingId = trackingId;
    this.nextUpdateHint = nextUpdateHint;
  }

  public ProximityUpdateAccepted trackingId(String trackingId) {
    this.trackingId = trackingId;
    return this;
  }

  /**
   * Get trackingId
   * @return trackingId
   */
  @NotNull 
  @Schema(name = "trackingId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trackingId")
  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  public ProximityUpdateAccepted nextUpdateHint(Integer nextUpdateHint) {
    this.nextUpdateHint = nextUpdateHint;
    return this;
  }

  /**
   * Get nextUpdateHint
   * @return nextUpdateHint
   */
  @NotNull 
  @Schema(name = "nextUpdateHint", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nextUpdateHint")
  public Integer getNextUpdateHint() {
    return nextUpdateHint;
  }

  public void setNextUpdateHint(Integer nextUpdateHint) {
    this.nextUpdateHint = nextUpdateHint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProximityUpdateAccepted proximityUpdateAccepted = (ProximityUpdateAccepted) o;
    return Objects.equals(this.trackingId, proximityUpdateAccepted.trackingId) &&
        Objects.equals(this.nextUpdateHint, proximityUpdateAccepted.nextUpdateHint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trackingId, nextUpdateHint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProximityUpdateAccepted {\n");
    sb.append("    trackingId: ").append(toIndentedString(trackingId)).append("\n");
    sb.append("    nextUpdateHint: ").append(toIndentedString(nextUpdateHint)).append("\n");
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

