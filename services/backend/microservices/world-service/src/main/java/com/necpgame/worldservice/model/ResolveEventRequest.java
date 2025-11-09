package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ResolveEventRequest
 */


public class ResolveEventRequest {

  private UUID instanceId;

  private String choiceId;

  public ResolveEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResolveEventRequest(UUID instanceId, String choiceId) {
    this.instanceId = instanceId;
    this.choiceId = choiceId;
  }

  public ResolveEventRequest instanceId(UUID instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @NotNull @Valid 
  @Schema(name = "instance_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("instance_id")
  public UUID getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(UUID instanceId) {
    this.instanceId = instanceId;
  }

  public ResolveEventRequest choiceId(String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  @NotNull 
  @Schema(name = "choice_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("choice_id")
  public String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(String choiceId) {
    this.choiceId = choiceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResolveEventRequest resolveEventRequest = (ResolveEventRequest) o;
    return Objects.equals(this.instanceId, resolveEventRequest.instanceId) &&
        Objects.equals(this.choiceId, resolveEventRequest.choiceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, choiceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResolveEventRequest {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
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

