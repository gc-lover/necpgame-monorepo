package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LayoutPresetPayload;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LayoutPresetRequest
 */


public class LayoutPresetRequest {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    SAVE("save"),
    
    APPLY("apply"),
    
    DELETE("delete");

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

  private ActionEnum action;

  private @Nullable LayoutPresetPayload preset;

  public LayoutPresetRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LayoutPresetRequest(ActionEnum action) {
    this.action = action;
  }

  public LayoutPresetRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public LayoutPresetRequest preset(@Nullable LayoutPresetPayload preset) {
    this.preset = preset;
    return this;
  }

  /**
   * Get preset
   * @return preset
   */
  @Valid 
  @Schema(name = "preset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preset")
  public @Nullable LayoutPresetPayload getPreset() {
    return preset;
  }

  public void setPreset(@Nullable LayoutPresetPayload preset) {
    this.preset = preset;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LayoutPresetRequest layoutPresetRequest = (LayoutPresetRequest) o;
    return Objects.equals(this.action, layoutPresetRequest.action) &&
        Objects.equals(this.preset, layoutPresetRequest.preset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, preset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LayoutPresetRequest {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    preset: ").append(toIndentedString(preset)).append("\n");
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

