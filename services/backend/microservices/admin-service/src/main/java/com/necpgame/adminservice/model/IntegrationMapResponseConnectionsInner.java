package com.necpgame.adminservice.model;

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
 * IntegrationMapResponseConnectionsInner
 */

@JsonTypeName("IntegrationMapResponse_connections_inner")

public class IntegrationMapResponseConnectionsInner {

  private @Nullable String fromService;

  private @Nullable String toService;

  /**
   * Gets or Sets connectionType
   */
  public enum ConnectionTypeEnum {
    HTTP("http"),
    
    GRPC("grpc"),
    
    EVENT("event"),
    
    WEBSOCKET("websocket");

    private final String value;

    ConnectionTypeEnum(String value) {
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
    public static ConnectionTypeEnum fromValue(String value) {
      for (ConnectionTypeEnum b : ConnectionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConnectionTypeEnum connectionType;

  private @Nullable String protocol;

  public IntegrationMapResponseConnectionsInner fromService(@Nullable String fromService) {
    this.fromService = fromService;
    return this;
  }

  /**
   * Get fromService
   * @return fromService
   */
  
  @Schema(name = "from_service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_service")
  public @Nullable String getFromService() {
    return fromService;
  }

  public void setFromService(@Nullable String fromService) {
    this.fromService = fromService;
  }

  public IntegrationMapResponseConnectionsInner toService(@Nullable String toService) {
    this.toService = toService;
    return this;
  }

  /**
   * Get toService
   * @return toService
   */
  
  @Schema(name = "to_service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to_service")
  public @Nullable String getToService() {
    return toService;
  }

  public void setToService(@Nullable String toService) {
    this.toService = toService;
  }

  public IntegrationMapResponseConnectionsInner connectionType(@Nullable ConnectionTypeEnum connectionType) {
    this.connectionType = connectionType;
    return this;
  }

  /**
   * Get connectionType
   * @return connectionType
   */
  
  @Schema(name = "connection_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connection_type")
  public @Nullable ConnectionTypeEnum getConnectionType() {
    return connectionType;
  }

  public void setConnectionType(@Nullable ConnectionTypeEnum connectionType) {
    this.connectionType = connectionType;
  }

  public IntegrationMapResponseConnectionsInner protocol(@Nullable String protocol) {
    this.protocol = protocol;
    return this;
  }

  /**
   * Get protocol
   * @return protocol
   */
  
  @Schema(name = "protocol", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("protocol")
  public @Nullable String getProtocol() {
    return protocol;
  }

  public void setProtocol(@Nullable String protocol) {
    this.protocol = protocol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntegrationMapResponseConnectionsInner integrationMapResponseConnectionsInner = (IntegrationMapResponseConnectionsInner) o;
    return Objects.equals(this.fromService, integrationMapResponseConnectionsInner.fromService) &&
        Objects.equals(this.toService, integrationMapResponseConnectionsInner.toService) &&
        Objects.equals(this.connectionType, integrationMapResponseConnectionsInner.connectionType) &&
        Objects.equals(this.protocol, integrationMapResponseConnectionsInner.protocol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromService, toService, connectionType, protocol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntegrationMapResponseConnectionsInner {\n");
    sb.append("    fromService: ").append(toIndentedString(fromService)).append("\n");
    sb.append("    toService: ").append(toIndentedString(toService)).append("\n");
    sb.append("    connectionType: ").append(toIndentedString(connectionType)).append("\n");
    sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
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

