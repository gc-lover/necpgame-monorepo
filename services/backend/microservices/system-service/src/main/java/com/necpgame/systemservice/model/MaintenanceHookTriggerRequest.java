package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
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
 * MaintenanceHookTriggerRequest
 */


public class MaintenanceHookTriggerRequest {

  private UUID windowId;

  /**
   * Gets or Sets hookType
   */
  public enum HookTypeEnum {
    DEPLOYMENT("DEPLOYMENT"),
    
    INCIDENT("INCIDENT"),
    
    STATUS_PAGE("STATUS_PAGE");

    private final String value;

    HookTypeEnum(String value) {
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
    public static HookTypeEnum fromValue(String value) {
      for (HookTypeEnum b : HookTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable HookTypeEnum hookType;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  public MaintenanceHookTriggerRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceHookTriggerRequest(UUID windowId, Map<String, Object> payload) {
    this.windowId = windowId;
    this.payload = payload;
  }

  public MaintenanceHookTriggerRequest windowId(UUID windowId) {
    this.windowId = windowId;
    return this;
  }

  /**
   * Get windowId
   * @return windowId
   */
  @NotNull @Valid 
  @Schema(name = "windowId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("windowId")
  public UUID getWindowId() {
    return windowId;
  }

  public void setWindowId(UUID windowId) {
    this.windowId = windowId;
  }

  public MaintenanceHookTriggerRequest hookType(@Nullable HookTypeEnum hookType) {
    this.hookType = hookType;
    return this;
  }

  /**
   * Get hookType
   * @return hookType
   */
  
  @Schema(name = "hookType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hookType")
  public @Nullable HookTypeEnum getHookType() {
    return hookType;
  }

  public void setHookType(@Nullable HookTypeEnum hookType) {
    this.hookType = hookType;
  }

  public MaintenanceHookTriggerRequest payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public MaintenanceHookTriggerRequest putPayloadItem(String key, Object payloadItem) {
    if (this.payload == null) {
      this.payload = new HashMap<>();
    }
    this.payload.put(key, payloadItem);
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  @NotNull 
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("payload")
  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceHookTriggerRequest maintenanceHookTriggerRequest = (MaintenanceHookTriggerRequest) o;
    return Objects.equals(this.windowId, maintenanceHookTriggerRequest.windowId) &&
        Objects.equals(this.hookType, maintenanceHookTriggerRequest.hookType) &&
        Objects.equals(this.payload, maintenanceHookTriggerRequest.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(windowId, hookType, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceHookTriggerRequest {\n");
    sb.append("    windowId: ").append(toIndentedString(windowId)).append("\n");
    sb.append("    hookType: ").append(toIndentedString(hookType)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
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

