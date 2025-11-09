package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ConcurrentSessionPolicy
 */


public class ConcurrentSessionPolicy {

  private @Nullable Boolean allowMultiple;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    TERMINATE_PREVIOUS("terminate_previous"),
    
    REJECT_NEW("reject_new");

    private final String value;

    ActionEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ActionEnum action;

  private @Nullable Boolean notifyPlayer;

  public ConcurrentSessionPolicy allowMultiple(@Nullable Boolean allowMultiple) {
    this.allowMultiple = allowMultiple;
    return this;
  }

  /**
   * Get allowMultiple
   * @return allowMultiple
   */
  
  @Schema(name = "allowMultiple", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowMultiple")
  public @Nullable Boolean getAllowMultiple() {
    return allowMultiple;
  }

  public void setAllowMultiple(@Nullable Boolean allowMultiple) {
    this.allowMultiple = allowMultiple;
  }

  public ConcurrentSessionPolicy action(@Nullable ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable ActionEnum getAction() {
    return action;
  }

  public void setAction(@Nullable ActionEnum action) {
    this.action = action;
  }

  public ConcurrentSessionPolicy notifyPlayer(@Nullable Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
    return this;
  }

  /**
   * Get notifyPlayer
   * @return notifyPlayer
   */
  
  @Schema(name = "notifyPlayer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayer")
  public @Nullable Boolean getNotifyPlayer() {
    return notifyPlayer;
  }

  public void setNotifyPlayer(@Nullable Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConcurrentSessionPolicy concurrentSessionPolicy = (ConcurrentSessionPolicy) o;
    return Objects.equals(this.allowMultiple, concurrentSessionPolicy.allowMultiple) &&
        Objects.equals(this.action, concurrentSessionPolicy.action) &&
        Objects.equals(this.notifyPlayer, concurrentSessionPolicy.notifyPlayer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowMultiple, action, notifyPlayer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConcurrentSessionPolicy {\n");
    sb.append("    allowMultiple: ").append(toIndentedString(allowMultiple)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    notifyPlayer: ").append(toIndentedString(notifyPlayer)).append("\n");
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

