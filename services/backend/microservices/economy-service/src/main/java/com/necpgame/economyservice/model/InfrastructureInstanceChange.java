package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.InfrastructureInstance;
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
 * InfrastructureInstanceChange
 */


public class InfrastructureInstanceChange {

  private @Nullable UUID instanceId;

  /**
   * Gets or Sets changeType
   */
  public enum ChangeTypeEnum {
    ADDED("added"),
    
    UPDATED("updated"),
    
    REMOVED("removed");

    private final String value;

    ChangeTypeEnum(String value) {
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
    public static ChangeTypeEnum fromValue(String value) {
      for (ChangeTypeEnum b : ChangeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ChangeTypeEnum changeType;

  private @Nullable InfrastructureInstance previous;

  private @Nullable InfrastructureInstance current;

  public InfrastructureInstanceChange instanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @Valid 
  @Schema(name = "instanceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instanceId")
  public @Nullable UUID getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(@Nullable UUID instanceId) {
    this.instanceId = instanceId;
  }

  public InfrastructureInstanceChange changeType(@Nullable ChangeTypeEnum changeType) {
    this.changeType = changeType;
    return this;
  }

  /**
   * Get changeType
   * @return changeType
   */
  
  @Schema(name = "changeType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("changeType")
  public @Nullable ChangeTypeEnum getChangeType() {
    return changeType;
  }

  public void setChangeType(@Nullable ChangeTypeEnum changeType) {
    this.changeType = changeType;
  }

  public InfrastructureInstanceChange previous(@Nullable InfrastructureInstance previous) {
    this.previous = previous;
    return this;
  }

  /**
   * Get previous
   * @return previous
   */
  @Valid 
  @Schema(name = "previous", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous")
  public @Nullable InfrastructureInstance getPrevious() {
    return previous;
  }

  public void setPrevious(@Nullable InfrastructureInstance previous) {
    this.previous = previous;
  }

  public InfrastructureInstanceChange current(@Nullable InfrastructureInstance current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  @Valid 
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable InfrastructureInstance getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable InfrastructureInstance current) {
    this.current = current;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureInstanceChange infrastructureInstanceChange = (InfrastructureInstanceChange) o;
    return Objects.equals(this.instanceId, infrastructureInstanceChange.instanceId) &&
        Objects.equals(this.changeType, infrastructureInstanceChange.changeType) &&
        Objects.equals(this.previous, infrastructureInstanceChange.previous) &&
        Objects.equals(this.current, infrastructureInstanceChange.current);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, changeType, previous, current);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureInstanceChange {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    changeType: ").append(toIndentedString(changeType)).append("\n");
    sb.append("    previous: ").append(toIndentedString(previous)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
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

