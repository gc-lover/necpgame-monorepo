package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * EventAck
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EventAck {

  private String acknowledgementId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime processedAt;

  public EventAck() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EventAck(String acknowledgementId, OffsetDateTime processedAt) {
    this.acknowledgementId = acknowledgementId;
    this.processedAt = processedAt;
  }

  public EventAck acknowledgementId(String acknowledgementId) {
    this.acknowledgementId = acknowledgementId;
    return this;
  }

  /**
   * Get acknowledgementId
   * @return acknowledgementId
   */
  @NotNull 
  @Schema(name = "acknowledgementId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("acknowledgementId")
  public String getAcknowledgementId() {
    return acknowledgementId;
  }

  public void setAcknowledgementId(String acknowledgementId) {
    this.acknowledgementId = acknowledgementId;
  }

  public EventAck processedAt(OffsetDateTime processedAt) {
    this.processedAt = processedAt;
    return this;
  }

  /**
   * Get processedAt
   * @return processedAt
   */
  @NotNull @Valid 
  @Schema(name = "processedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("processedAt")
  public OffsetDateTime getProcessedAt() {
    return processedAt;
  }

  public void setProcessedAt(OffsetDateTime processedAt) {
    this.processedAt = processedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventAck eventAck = (EventAck) o;
    return Objects.equals(this.acknowledgementId, eventAck.acknowledgementId) &&
        Objects.equals(this.processedAt, eventAck.processedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(acknowledgementId, processedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventAck {\n");
    sb.append("    acknowledgementId: ").append(toIndentedString(acknowledgementId)).append("\n");
    sb.append("    processedAt: ").append(toIndentedString(processedAt)).append("\n");
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

