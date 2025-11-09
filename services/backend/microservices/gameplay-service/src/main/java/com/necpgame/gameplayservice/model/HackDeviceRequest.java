package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * HackDeviceRequest
 */

@JsonTypeName("hackDevice_request")

public class HackDeviceRequest {

  private String characterId;

  private String deviceId;

  /**
   * Gets or Sets hackAction
   */
  public enum HackActionEnum {
    DISABLE("disable"),
    
    CONTROL("control"),
    
    REPROGRAM("reprogram"),
    
    EXPLODE("explode"),
    
    UNLOCK("unlock");

    private final String value;

    HackActionEnum(String value) {
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
    public static HackActionEnum fromValue(String value) {
      for (HackActionEnum b : HackActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private HackActionEnum hackAction;

  public HackDeviceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HackDeviceRequest(String characterId, String deviceId, HackActionEnum hackAction) {
    this.characterId = characterId;
    this.deviceId = deviceId;
    this.hackAction = hackAction;
  }

  public HackDeviceRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public HackDeviceRequest deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  /**
   * Get deviceId
   * @return deviceId
   */
  @NotNull 
  @Schema(name = "device_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("device_id")
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public HackDeviceRequest hackAction(HackActionEnum hackAction) {
    this.hackAction = hackAction;
    return this;
  }

  /**
   * Get hackAction
   * @return hackAction
   */
  @NotNull 
  @Schema(name = "hack_action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hack_action")
  public HackActionEnum getHackAction() {
    return hackAction;
  }

  public void setHackAction(HackActionEnum hackAction) {
    this.hackAction = hackAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackDeviceRequest hackDeviceRequest = (HackDeviceRequest) o;
    return Objects.equals(this.characterId, hackDeviceRequest.characterId) &&
        Objects.equals(this.deviceId, hackDeviceRequest.deviceId) &&
        Objects.equals(this.hackAction, hackDeviceRequest.hackAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, deviceId, hackAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackDeviceRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    hackAction: ").append(toIndentedString(hackAction)).append("\n");
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

