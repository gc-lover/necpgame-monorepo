package com.necpgame.gameplayservice.model;

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
 * NetworkInfoConnectionsInner
 */

@JsonTypeName("NetworkInfo_connections_inner")

public class NetworkInfoConnectionsInner {

  private @Nullable String from;

  private @Nullable String to;

  public NetworkInfoConnectionsInner from(@Nullable String from) {
    this.from = from;
    return this;
  }

  /**
   * Get from
   * @return from
   */
  
  @Schema(name = "from", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from")
  public @Nullable String getFrom() {
    return from;
  }

  public void setFrom(@Nullable String from) {
    this.from = from;
  }

  public NetworkInfoConnectionsInner to(@Nullable String to) {
    this.to = to;
    return this;
  }

  /**
   * Get to
   * @return to
   */
  
  @Schema(name = "to", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to")
  public @Nullable String getTo() {
    return to;
  }

  public void setTo(@Nullable String to) {
    this.to = to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkInfoConnectionsInner networkInfoConnectionsInner = (NetworkInfoConnectionsInner) o;
    return Objects.equals(this.from, networkInfoConnectionsInner.from) &&
        Objects.equals(this.to, networkInfoConnectionsInner.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkInfoConnectionsInner {\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
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

