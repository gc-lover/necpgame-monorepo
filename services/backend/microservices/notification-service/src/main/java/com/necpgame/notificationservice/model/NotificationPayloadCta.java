package com.necpgame.notificationservice.model;

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
 * NotificationPayloadCta
 */

@JsonTypeName("NotificationPayload_cta")

public class NotificationPayloadCta {

  private @Nullable String label;

  private @Nullable String action;

  private @Nullable String target;

  public NotificationPayloadCta label(@Nullable String label) {
    this.label = label;
    return this;
  }

  /**
   * Get label
   * @return label
   */
  
  @Schema(name = "label", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("label")
  public @Nullable String getLabel() {
    return label;
  }

  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  public NotificationPayloadCta action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  public NotificationPayloadCta target(@Nullable String target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  
  @Schema(name = "target", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target")
  public @Nullable String getTarget() {
    return target;
  }

  public void setTarget(@Nullable String target) {
    this.target = target;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPayloadCta notificationPayloadCta = (NotificationPayloadCta) o;
    return Objects.equals(this.label, notificationPayloadCta.label) &&
        Objects.equals(this.action, notificationPayloadCta.action) &&
        Objects.equals(this.target, notificationPayloadCta.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, action, target);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPayloadCta {\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
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

