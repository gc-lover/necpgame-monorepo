package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Action;
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
 * ActionRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ActionRequest {

  private Action action;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime clientTimestamp;

  private @Nullable String idempotencyKey;

  public ActionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionRequest(Action action) {
    this.action = action;
  }

  public ActionRequest action(Action action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull @Valid 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public ActionRequest clientTimestamp(@Nullable OffsetDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
    return this;
  }

  /**
   * Get clientTimestamp
   * @return clientTimestamp
   */
  @Valid 
  @Schema(name = "clientTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clientTimestamp")
  public @Nullable OffsetDateTime getClientTimestamp() {
    return clientTimestamp;
  }

  public void setClientTimestamp(@Nullable OffsetDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
  }

  public ActionRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionRequest actionRequest = (ActionRequest) o;
    return Objects.equals(this.action, actionRequest.action) &&
        Objects.equals(this.clientTimestamp, actionRequest.clientTimestamp) &&
        Objects.equals(this.idempotencyKey, actionRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, clientTimestamp, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    clientTimestamp: ").append(toIndentedString(clientTimestamp)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

