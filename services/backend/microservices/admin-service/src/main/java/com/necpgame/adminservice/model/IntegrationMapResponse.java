package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.EventChannel;
import com.necpgame.adminservice.model.IntegrationMapResponseConnectionsInner;
import com.necpgame.adminservice.model.ServiceInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * IntegrationMapResponse
 */


public class IntegrationMapResponse {

  @Valid
  private List<@Valid ServiceInfo> services = new ArrayList<>();

  @Valid
  private List<@Valid IntegrationMapResponseConnectionsInner> connections = new ArrayList<>();

  @Valid
  private List<@Valid EventChannel> eventChannels = new ArrayList<>();

  private @Nullable String architectureDiagramUrl;

  public IntegrationMapResponse services(List<@Valid ServiceInfo> services) {
    this.services = services;
    return this;
  }

  public IntegrationMapResponse addServicesItem(ServiceInfo servicesItem) {
    if (this.services == null) {
      this.services = new ArrayList<>();
    }
    this.services.add(servicesItem);
    return this;
  }

  /**
   * Get services
   * @return services
   */
  @Valid 
  @Schema(name = "services", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("services")
  public List<@Valid ServiceInfo> getServices() {
    return services;
  }

  public void setServices(List<@Valid ServiceInfo> services) {
    this.services = services;
  }

  public IntegrationMapResponse connections(List<@Valid IntegrationMapResponseConnectionsInner> connections) {
    this.connections = connections;
    return this;
  }

  public IntegrationMapResponse addConnectionsItem(IntegrationMapResponseConnectionsInner connectionsItem) {
    if (this.connections == null) {
      this.connections = new ArrayList<>();
    }
    this.connections.add(connectionsItem);
    return this;
  }

  /**
   * Связи между сервисами
   * @return connections
   */
  @Valid 
  @Schema(name = "connections", description = "Связи между сервисами", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connections")
  public List<@Valid IntegrationMapResponseConnectionsInner> getConnections() {
    return connections;
  }

  public void setConnections(List<@Valid IntegrationMapResponseConnectionsInner> connections) {
    this.connections = connections;
  }

  public IntegrationMapResponse eventChannels(List<@Valid EventChannel> eventChannels) {
    this.eventChannels = eventChannels;
    return this;
  }

  public IntegrationMapResponse addEventChannelsItem(EventChannel eventChannelsItem) {
    if (this.eventChannels == null) {
      this.eventChannels = new ArrayList<>();
    }
    this.eventChannels.add(eventChannelsItem);
    return this;
  }

  /**
   * Get eventChannels
   * @return eventChannels
   */
  @Valid 
  @Schema(name = "event_channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_channels")
  public List<@Valid EventChannel> getEventChannels() {
    return eventChannels;
  }

  public void setEventChannels(List<@Valid EventChannel> eventChannels) {
    this.eventChannels = eventChannels;
  }

  public IntegrationMapResponse architectureDiagramUrl(@Nullable String architectureDiagramUrl) {
    this.architectureDiagramUrl = architectureDiagramUrl;
    return this;
  }

  /**
   * URL to architecture diagram
   * @return architectureDiagramUrl
   */
  
  @Schema(name = "architecture_diagram_url", description = "URL to architecture diagram", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("architecture_diagram_url")
  public @Nullable String getArchitectureDiagramUrl() {
    return architectureDiagramUrl;
  }

  public void setArchitectureDiagramUrl(@Nullable String architectureDiagramUrl) {
    this.architectureDiagramUrl = architectureDiagramUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntegrationMapResponse integrationMapResponse = (IntegrationMapResponse) o;
    return Objects.equals(this.services, integrationMapResponse.services) &&
        Objects.equals(this.connections, integrationMapResponse.connections) &&
        Objects.equals(this.eventChannels, integrationMapResponse.eventChannels) &&
        Objects.equals(this.architectureDiagramUrl, integrationMapResponse.architectureDiagramUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(services, connections, eventChannels, architectureDiagramUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntegrationMapResponse {\n");
    sb.append("    services: ").append(toIndentedString(services)).append("\n");
    sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
    sb.append("    eventChannels: ").append(toIndentedString(eventChannels)).append("\n");
    sb.append("    architectureDiagramUrl: ").append(toIndentedString(architectureDiagramUrl)).append("\n");
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

