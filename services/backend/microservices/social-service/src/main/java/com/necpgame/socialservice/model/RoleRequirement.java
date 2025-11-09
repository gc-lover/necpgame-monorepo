package com.necpgame.socialservice.model;

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
 * RoleRequirement
 */


public class RoleRequirement {

  private String role;

  private Integer needed;

  private Integer current;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    CRITICAL("critical"),
    
    HIGH("high"),
    
    NORMAL("normal"),
    
    LOW("low");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PriorityEnum priority;

  public RoleRequirement() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleRequirement(String role, Integer needed, Integer current) {
    this.role = role;
    this.needed = needed;
    this.current = current;
  }

  public RoleRequirement role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public RoleRequirement needed(Integer needed) {
    this.needed = needed;
    return this;
  }

  /**
   * Get needed
   * @return needed
   */
  @NotNull 
  @Schema(name = "needed", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("needed")
  public Integer getNeeded() {
    return needed;
  }

  public void setNeeded(Integer needed) {
    this.needed = needed;
  }

  public RoleRequirement current(Integer current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  @NotNull 
  @Schema(name = "current", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current")
  public Integer getCurrent() {
    return current;
  }

  public void setCurrent(Integer current) {
    this.current = current;
  }

  public RoleRequirement priority(@Nullable PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(@Nullable PriorityEnum priority) {
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleRequirement roleRequirement = (RoleRequirement) o;
    return Objects.equals(this.role, roleRequirement.role) &&
        Objects.equals(this.needed, roleRequirement.needed) &&
        Objects.equals(this.current, roleRequirement.current) &&
        Objects.equals(this.priority, roleRequirement.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, needed, current, priority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleRequirement {\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    needed: ").append(toIndentedString(needed)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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

