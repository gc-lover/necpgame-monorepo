package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.FriendRequestCreateTarget;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FriendRequestCreate
 */


public class FriendRequestCreate {

  private FriendRequestCreateTarget target;

  private @Nullable String message;

  private @Nullable String idempotencyKey;

  public FriendRequestCreate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FriendRequestCreate(FriendRequestCreateTarget target) {
    this.target = target;
  }

  public FriendRequestCreate target(FriendRequestCreateTarget target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  @NotNull @Valid 
  @Schema(name = "target", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target")
  public FriendRequestCreateTarget getTarget() {
    return target;
  }

  public void setTarget(FriendRequestCreateTarget target) {
    this.target = target;
  }

  public FriendRequestCreate message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public FriendRequestCreate idempotencyKey(@Nullable String idempotencyKey) {
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
    FriendRequestCreate friendRequestCreate = (FriendRequestCreate) o;
    return Objects.equals(this.target, friendRequestCreate.target) &&
        Objects.equals(this.message, friendRequestCreate.message) &&
        Objects.equals(this.idempotencyKey, friendRequestCreate.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(target, message, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FriendRequestCreate {\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

