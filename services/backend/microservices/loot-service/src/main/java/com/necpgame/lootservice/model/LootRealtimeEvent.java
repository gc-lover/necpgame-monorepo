package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LootRealtimeEvent
 */


public class LootRealtimeEvent {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    LOOT_GENERATED("loot-generated"),
    
    LOOT_DISTRIBUTED("loot-distributed"),
    
    ROLL_UPDATED("roll-updated"),
    
    RARE_DROP("rare-drop"),
    
    PITY_TRIGGERED("pity-triggered"),
    
    LOOT_TABLE_UPDATED("loot-table-updated");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  public LootRealtimeEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootRealtimeEvent(TypeEnum type, Map<String, Object> payload) {
    this.type = type;
    this.payload = payload;
  }

  public LootRealtimeEvent type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public LootRealtimeEvent payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public LootRealtimeEvent putPayloadItem(String key, Object payloadItem) {
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
    LootRealtimeEvent lootRealtimeEvent = (LootRealtimeEvent) o;
    return Objects.equals(this.type, lootRealtimeEvent.type) &&
        Objects.equals(this.payload, lootRealtimeEvent.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootRealtimeEvent {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

