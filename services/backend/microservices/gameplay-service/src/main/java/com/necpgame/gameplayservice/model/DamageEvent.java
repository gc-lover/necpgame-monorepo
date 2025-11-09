package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.DamagePacket;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * DamageEvent
 */


public class DamageEvent {

  private @Nullable String sessionId;

  @Valid
  private List<@Valid DamagePacket> packets = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public DamageEvent sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public DamageEvent packets(List<@Valid DamagePacket> packets) {
    this.packets = packets;
    return this;
  }

  public DamageEvent addPacketsItem(DamagePacket packetsItem) {
    if (this.packets == null) {
      this.packets = new ArrayList<>();
    }
    this.packets.add(packetsItem);
    return this;
  }

  /**
   * Get packets
   * @return packets
   */
  @Valid 
  @Schema(name = "packets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("packets")
  public List<@Valid DamagePacket> getPackets() {
    return packets;
  }

  public void setPackets(List<@Valid DamagePacket> packets) {
    this.packets = packets;
  }

  public DamageEvent occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurredAt")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamageEvent damageEvent = (DamageEvent) o;
    return Objects.equals(this.sessionId, damageEvent.sessionId) &&
        Objects.equals(this.packets, damageEvent.packets) &&
        Objects.equals(this.occurredAt, damageEvent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, packets, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamageEvent {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    packets: ").append(toIndentedString(packets)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

