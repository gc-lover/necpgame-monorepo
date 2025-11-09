package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Action;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DamagePreviewRequest
 */


public class DamagePreviewRequest {

  private Action action;

  private Boolean includeMitigation = true;

  public DamagePreviewRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DamagePreviewRequest(Action action) {
    this.action = action;
  }

  public DamagePreviewRequest action(Action action) {
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

  public DamagePreviewRequest includeMitigation(Boolean includeMitigation) {
    this.includeMitigation = includeMitigation;
    return this;
  }

  /**
   * Get includeMitigation
   * @return includeMitigation
   */
  
  @Schema(name = "includeMitigation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeMitigation")
  public Boolean getIncludeMitigation() {
    return includeMitigation;
  }

  public void setIncludeMitigation(Boolean includeMitigation) {
    this.includeMitigation = includeMitigation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamagePreviewRequest damagePreviewRequest = (DamagePreviewRequest) o;
    return Objects.equals(this.action, damagePreviewRequest.action) &&
        Objects.equals(this.includeMitigation, damagePreviewRequest.includeMitigation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, includeMitigation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamagePreviewRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    includeMitigation: ").append(toIndentedString(includeMitigation)).append("\n");
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

