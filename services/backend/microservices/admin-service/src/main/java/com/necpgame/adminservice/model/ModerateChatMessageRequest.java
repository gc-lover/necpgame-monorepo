package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ModerateChatMessageRequest
 */

@JsonTypeName("moderateChatMessage_request")

public class ModerateChatMessageRequest {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    DELETE("DELETE"),
    
    WARN("WARN"),
    
    MUTE_USER("MUTE_USER"),
    
    BAN_USER("BAN_USER");

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

  private JsonNullable<Integer> durationMinutes = JsonNullable.<Integer>undefined();

  private @Nullable String reason;

  public ModerateChatMessageRequest action(@Nullable ActionEnum action) {
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

  public ModerateChatMessageRequest durationMinutes(Integer durationMinutes) {
    this.durationMinutes = JsonNullable.of(durationMinutes);
    return this;
  }

  /**
   * Get durationMinutes
   * @return durationMinutes
   */
  
  @Schema(name = "duration_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_minutes")
  public JsonNullable<Integer> getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(JsonNullable<Integer> durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public ModerateChatMessageRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModerateChatMessageRequest moderateChatMessageRequest = (ModerateChatMessageRequest) o;
    return Objects.equals(this.action, moderateChatMessageRequest.action) &&
        equalsNullable(this.durationMinutes, moderateChatMessageRequest.durationMinutes) &&
        Objects.equals(this.reason, moderateChatMessageRequest.reason);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, hashCodeNullable(durationMinutes), reason);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModerateChatMessageRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

