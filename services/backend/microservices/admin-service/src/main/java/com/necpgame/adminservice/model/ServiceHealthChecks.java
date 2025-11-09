package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ServiceHealthChecksDependenciesInner;
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
 * ServiceHealthChecks
 */

@JsonTypeName("ServiceHealth_checks")

public class ServiceHealthChecks {

  /**
   * Gets or Sets database
   */
  public enum DatabaseEnum {
    OK("ok"),
    
    SLOW("slow"),
    
    ERROR("error");

    private final String value;

    DatabaseEnum(String value) {
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
    public static DatabaseEnum fromValue(String value) {
      for (DatabaseEnum b : DatabaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DatabaseEnum database;

  /**
   * Gets or Sets cache
   */
  public enum CacheEnum {
    OK("ok"),
    
    SLOW("slow"),
    
    ERROR("error");

    private final String value;

    CacheEnum(String value) {
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
    public static CacheEnum fromValue(String value) {
      for (CacheEnum b : CacheEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CacheEnum cache;

  @Valid
  private List<@Valid ServiceHealthChecksDependenciesInner> dependencies = new ArrayList<>();

  public ServiceHealthChecks database(@Nullable DatabaseEnum database) {
    this.database = database;
    return this;
  }

  /**
   * Get database
   * @return database
   */
  
  @Schema(name = "database", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("database")
  public @Nullable DatabaseEnum getDatabase() {
    return database;
  }

  public void setDatabase(@Nullable DatabaseEnum database) {
    this.database = database;
  }

  public ServiceHealthChecks cache(@Nullable CacheEnum cache) {
    this.cache = cache;
    return this;
  }

  /**
   * Get cache
   * @return cache
   */
  
  @Schema(name = "cache", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cache")
  public @Nullable CacheEnum getCache() {
    return cache;
  }

  public void setCache(@Nullable CacheEnum cache) {
    this.cache = cache;
  }

  public ServiceHealthChecks dependencies(List<@Valid ServiceHealthChecksDependenciesInner> dependencies) {
    this.dependencies = dependencies;
    return this;
  }

  public ServiceHealthChecks addDependenciesItem(ServiceHealthChecksDependenciesInner dependenciesItem) {
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<>();
    }
    this.dependencies.add(dependenciesItem);
    return this;
  }

  /**
   * Get dependencies
   * @return dependencies
   */
  @Valid 
  @Schema(name = "dependencies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dependencies")
  public List<@Valid ServiceHealthChecksDependenciesInner> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<@Valid ServiceHealthChecksDependenciesInner> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceHealthChecks serviceHealthChecks = (ServiceHealthChecks) o;
    return Objects.equals(this.database, serviceHealthChecks.database) &&
        Objects.equals(this.cache, serviceHealthChecks.cache) &&
        Objects.equals(this.dependencies, serviceHealthChecks.dependencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(database, cache, dependencies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceHealthChecks {\n");
    sb.append("    database: ").append(toIndentedString(database)).append("\n");
    sb.append("    cache: ").append(toIndentedString(cache)).append("\n");
    sb.append("    dependencies: ").append(toIndentedString(dependencies)).append("\n");
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

