package com.necpgame.adminservice.model;

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
 * ServerInfo
 */


public class ServerInfo {

  private @Nullable String serverId;

  private @Nullable String name;

  private @Nullable String region;

  /**
   * Gets or Sets population
   */
  public enum PopulationEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH"),
    
    FULL("FULL");

    private final String value;

    PopulationEnum(String value) {
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
    public static PopulationEnum fromValue(String value) {
      for (PopulationEnum b : PopulationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PopulationEnum population;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ONLINE("ONLINE"),
    
    MAINTENANCE("MAINTENANCE"),
    
    OFFLINE("OFFLINE");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer pingMs;

  public ServerInfo serverId(@Nullable String serverId) {
    this.serverId = serverId;
    return this;
  }

  /**
   * Get serverId
   * @return serverId
   */
  
  @Schema(name = "server_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server_id")
  public @Nullable String getServerId() {
    return serverId;
  }

  public void setServerId(@Nullable String serverId) {
    this.serverId = serverId;
  }

  public ServerInfo name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public ServerInfo region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public ServerInfo population(@Nullable PopulationEnum population) {
    this.population = population;
    return this;
  }

  /**
   * Get population
   * @return population
   */
  
  @Schema(name = "population", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("population")
  public @Nullable PopulationEnum getPopulation() {
    return population;
  }

  public void setPopulation(@Nullable PopulationEnum population) {
    this.population = population;
  }

  public ServerInfo status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ServerInfo pingMs(@Nullable Integer pingMs) {
    this.pingMs = pingMs;
    return this;
  }

  /**
   * Get pingMs
   * @return pingMs
   */
  
  @Schema(name = "ping_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ping_ms")
  public @Nullable Integer getPingMs() {
    return pingMs;
  }

  public void setPingMs(@Nullable Integer pingMs) {
    this.pingMs = pingMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServerInfo serverInfo = (ServerInfo) o;
    return Objects.equals(this.serverId, serverInfo.serverId) &&
        Objects.equals(this.name, serverInfo.name) &&
        Objects.equals(this.region, serverInfo.region) &&
        Objects.equals(this.population, serverInfo.population) &&
        Objects.equals(this.status, serverInfo.status) &&
        Objects.equals(this.pingMs, serverInfo.pingMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serverId, name, region, population, status, pingMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServerInfo {\n");
    sb.append("    serverId: ").append(toIndentedString(serverId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    population: ").append(toIndentedString(population)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    pingMs: ").append(toIndentedString(pingMs)).append("\n");
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

