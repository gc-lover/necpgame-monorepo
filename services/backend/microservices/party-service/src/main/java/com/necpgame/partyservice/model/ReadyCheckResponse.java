package com.necpgame.partyservice.model;

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
 * ReadyCheckResponse
 */


public class ReadyCheckResponse {

  private @Nullable String memberId;

  private @Nullable Boolean ready;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime respondedAt;

  public ReadyCheckResponse memberId(@Nullable String memberId) {
    this.memberId = memberId;
    return this;
  }

  /**
   * Get memberId
   * @return memberId
   */
  
  @Schema(name = "memberId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("memberId")
  public @Nullable String getMemberId() {
    return memberId;
  }

  public void setMemberId(@Nullable String memberId) {
    this.memberId = memberId;
  }

  public ReadyCheckResponse ready(@Nullable Boolean ready) {
    this.ready = ready;
    return this;
  }

  /**
   * Get ready
   * @return ready
   */
  
  @Schema(name = "ready", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ready")
  public @Nullable Boolean getReady() {
    return ready;
  }

  public void setReady(@Nullable Boolean ready) {
    this.ready = ready;
  }

  public ReadyCheckResponse respondedAt(@Nullable OffsetDateTime respondedAt) {
    this.respondedAt = respondedAt;
    return this;
  }

  /**
   * Get respondedAt
   * @return respondedAt
   */
  @Valid 
  @Schema(name = "respondedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("respondedAt")
  public @Nullable OffsetDateTime getRespondedAt() {
    return respondedAt;
  }

  public void setRespondedAt(@Nullable OffsetDateTime respondedAt) {
    this.respondedAt = respondedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadyCheckResponse readyCheckResponse = (ReadyCheckResponse) o;
    return Objects.equals(this.memberId, readyCheckResponse.memberId) &&
        Objects.equals(this.ready, readyCheckResponse.ready) &&
        Objects.equals(this.respondedAt, readyCheckResponse.respondedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberId, ready, respondedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheckResponse {\n");
    sb.append("    memberId: ").append(toIndentedString(memberId)).append("\n");
    sb.append("    ready: ").append(toIndentedString(ready)).append("\n");
    sb.append("    respondedAt: ").append(toIndentedString(respondedAt)).append("\n");
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

