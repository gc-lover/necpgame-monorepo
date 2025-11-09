package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.JoinVoiceRequestClientInfo;
import com.necpgame.backjava.model.JoinVoiceRequestLocation;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * JoinVoiceRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class JoinVoiceRequest {

  private String playerId;

  /**
   * Gets or Sets preferredQuality
   */
  public enum PreferredQualityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    ULTRA("ultra");

    private final String value;

    PreferredQualityEnum(String value) {
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
    public static PreferredQualityEnum fromValue(String value) {
      for (PreferredQualityEnum b : PreferredQualityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PreferredQualityEnum preferredQuality;

  private @Nullable JoinVoiceRequestClientInfo clientInfo;

  private @Nullable JoinVoiceRequestLocation location;

  public JoinVoiceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public JoinVoiceRequest(String playerId) {
    this.playerId = playerId;
  }

  public JoinVoiceRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public JoinVoiceRequest preferredQuality(@Nullable PreferredQualityEnum preferredQuality) {
    this.preferredQuality = preferredQuality;
    return this;
  }

  /**
   * Get preferredQuality
   * @return preferredQuality
   */
  
  @Schema(name = "preferredQuality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredQuality")
  public @Nullable PreferredQualityEnum getPreferredQuality() {
    return preferredQuality;
  }

  public void setPreferredQuality(@Nullable PreferredQualityEnum preferredQuality) {
    this.preferredQuality = preferredQuality;
  }

  public JoinVoiceRequest clientInfo(@Nullable JoinVoiceRequestClientInfo clientInfo) {
    this.clientInfo = clientInfo;
    return this;
  }

  /**
   * Get clientInfo
   * @return clientInfo
   */
  @Valid 
  @Schema(name = "clientInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clientInfo")
  public @Nullable JoinVoiceRequestClientInfo getClientInfo() {
    return clientInfo;
  }

  public void setClientInfo(@Nullable JoinVoiceRequestClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  public JoinVoiceRequest location(@Nullable JoinVoiceRequestLocation location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  @Valid 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable JoinVoiceRequestLocation getLocation() {
    return location;
  }

  public void setLocation(@Nullable JoinVoiceRequestLocation location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JoinVoiceRequest joinVoiceRequest = (JoinVoiceRequest) o;
    return Objects.equals(this.playerId, joinVoiceRequest.playerId) &&
        Objects.equals(this.preferredQuality, joinVoiceRequest.preferredQuality) &&
        Objects.equals(this.clientInfo, joinVoiceRequest.clientInfo) &&
        Objects.equals(this.location, joinVoiceRequest.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, preferredQuality, clientInfo, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinVoiceRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    preferredQuality: ").append(toIndentedString(preferredQuality)).append("\n");
    sb.append("    clientInfo: ").append(toIndentedString(clientInfo)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

