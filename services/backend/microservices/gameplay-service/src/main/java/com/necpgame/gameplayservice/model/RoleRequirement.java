package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Role;
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

  private Role role;

  private Integer minimum;

  private Integer maximum;

  public RoleRequirement() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleRequirement(Role role, Integer minimum, Integer maximum) {
    this.role = role;
    this.minimum = minimum;
    this.maximum = maximum;
  }

  public RoleRequirement role(Role role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull @Valid 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public RoleRequirement minimum(Integer minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * Get minimum
   * minimum: 0
   * maximum: 3
   * @return minimum
   */
  @NotNull @Min(value = 0) @Max(value = 3) 
  @Schema(name = "minimum", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("minimum")
  public Integer getMinimum() {
    return minimum;
  }

  public void setMinimum(Integer minimum) {
    this.minimum = minimum;
  }

  public RoleRequirement maximum(Integer maximum) {
    this.maximum = maximum;
    return this;
  }

  /**
   * Get maximum
   * minimum: 1
   * maximum: 5
   * @return maximum
   */
  @NotNull @Min(value = 1) @Max(value = 5) 
  @Schema(name = "maximum", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maximum")
  public Integer getMaximum() {
    return maximum;
  }

  public void setMaximum(Integer maximum) {
    this.maximum = maximum;
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
        Objects.equals(this.minimum, roleRequirement.minimum) &&
        Objects.equals(this.maximum, roleRequirement.maximum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, minimum, maximum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleRequirement {\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    minimum: ").append(toIndentedString(minimum)).append("\n");
    sb.append("    maximum: ").append(toIndentedString(maximum)).append("\n");
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

