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
 * RoleSummary
 */


public class RoleSummary {

  private Role role;

  private @Nullable Integer required;

  private Integer assigned;

  private @Nullable Integer deficit;

  public RoleSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleSummary(Role role, Integer assigned) {
    this.role = role;
    this.assigned = assigned;
  }

  public RoleSummary role(Role role) {
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

  public RoleSummary required(@Nullable Integer required) {
    this.required = required;
    return this;
  }

  /**
   * Get required
   * minimum: 0
   * maximum: 5
   * @return required
   */
  @Min(value = 0) @Max(value = 5) 
  @Schema(name = "required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required")
  public @Nullable Integer getRequired() {
    return required;
  }

  public void setRequired(@Nullable Integer required) {
    this.required = required;
  }

  public RoleSummary assigned(Integer assigned) {
    this.assigned = assigned;
    return this;
  }

  /**
   * Get assigned
   * minimum: 0
   * maximum: 5
   * @return assigned
   */
  @NotNull @Min(value = 0) @Max(value = 5) 
  @Schema(name = "assigned", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("assigned")
  public Integer getAssigned() {
    return assigned;
  }

  public void setAssigned(Integer assigned) {
    this.assigned = assigned;
  }

  public RoleSummary deficit(@Nullable Integer deficit) {
    this.deficit = deficit;
    return this;
  }

  /**
   * Get deficit
   * minimum: 0
   * maximum: 5
   * @return deficit
   */
  @Min(value = 0) @Max(value = 5) 
  @Schema(name = "deficit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deficit")
  public @Nullable Integer getDeficit() {
    return deficit;
  }

  public void setDeficit(@Nullable Integer deficit) {
    this.deficit = deficit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleSummary roleSummary = (RoleSummary) o;
    return Objects.equals(this.role, roleSummary.role) &&
        Objects.equals(this.required, roleSummary.required) &&
        Objects.equals(this.assigned, roleSummary.assigned) &&
        Objects.equals(this.deficit, roleSummary.deficit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, required, assigned, deficit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleSummary {\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    assigned: ").append(toIndentedString(assigned)).append("\n");
    sb.append("    deficit: ").append(toIndentedString(deficit)).append("\n");
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

