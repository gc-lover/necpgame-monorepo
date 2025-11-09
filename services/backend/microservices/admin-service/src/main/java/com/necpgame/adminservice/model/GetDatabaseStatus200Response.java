package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.DatabaseStatus;
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
 * GetDatabaseStatus200Response
 */

@JsonTypeName("getDatabaseStatus_200_response")

public class GetDatabaseStatus200Response {

  @Valid
  private List<@Valid DatabaseStatus> databases = new ArrayList<>();

  /**
   * Gets or Sets overallHealth
   */
  public enum OverallHealthEnum {
    HEALTHY("healthy"),
    
    DEGRADED("degraded"),
    
    CRITICAL("critical");

    private final String value;

    OverallHealthEnum(String value) {
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
    public static OverallHealthEnum fromValue(String value) {
      for (OverallHealthEnum b : OverallHealthEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OverallHealthEnum overallHealth;

  public GetDatabaseStatus200Response databases(List<@Valid DatabaseStatus> databases) {
    this.databases = databases;
    return this;
  }

  public GetDatabaseStatus200Response addDatabasesItem(DatabaseStatus databasesItem) {
    if (this.databases == null) {
      this.databases = new ArrayList<>();
    }
    this.databases.add(databasesItem);
    return this;
  }

  /**
   * Get databases
   * @return databases
   */
  @Valid 
  @Schema(name = "databases", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("databases")
  public List<@Valid DatabaseStatus> getDatabases() {
    return databases;
  }

  public void setDatabases(List<@Valid DatabaseStatus> databases) {
    this.databases = databases;
  }

  public GetDatabaseStatus200Response overallHealth(@Nullable OverallHealthEnum overallHealth) {
    this.overallHealth = overallHealth;
    return this;
  }

  /**
   * Get overallHealth
   * @return overallHealth
   */
  
  @Schema(name = "overall_health", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overall_health")
  public @Nullable OverallHealthEnum getOverallHealth() {
    return overallHealth;
  }

  public void setOverallHealth(@Nullable OverallHealthEnum overallHealth) {
    this.overallHealth = overallHealth;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetDatabaseStatus200Response getDatabaseStatus200Response = (GetDatabaseStatus200Response) o;
    return Objects.equals(this.databases, getDatabaseStatus200Response.databases) &&
        Objects.equals(this.overallHealth, getDatabaseStatus200Response.overallHealth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(databases, overallHealth);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetDatabaseStatus200Response {\n");
    sb.append("    databases: ").append(toIndentedString(databases)).append("\n");
    sb.append("    overallHealth: ").append(toIndentedString(overallHealth)).append("\n");
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

