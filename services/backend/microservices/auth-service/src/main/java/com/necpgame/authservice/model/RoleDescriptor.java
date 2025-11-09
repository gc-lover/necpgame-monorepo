package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RoleDescriptor
 */


public class RoleDescriptor {

  private @Nullable String name;

  @Valid
  private List<String> inherits = new ArrayList<>();

  @Valid
  private List<String> defaultPermissions = new ArrayList<>();

  private @Nullable String description;

  public RoleDescriptor name(@Nullable String name) {
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

  public RoleDescriptor inherits(List<String> inherits) {
    this.inherits = inherits;
    return this;
  }

  public RoleDescriptor addInheritsItem(String inheritsItem) {
    if (this.inherits == null) {
      this.inherits = new ArrayList<>();
    }
    this.inherits.add(inheritsItem);
    return this;
  }

  /**
   * Get inherits
   * @return inherits
   */
  
  @Schema(name = "inherits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inherits")
  public List<String> getInherits() {
    return inherits;
  }

  public void setInherits(List<String> inherits) {
    this.inherits = inherits;
  }

  public RoleDescriptor defaultPermissions(List<String> defaultPermissions) {
    this.defaultPermissions = defaultPermissions;
    return this;
  }

  public RoleDescriptor addDefaultPermissionsItem(String defaultPermissionsItem) {
    if (this.defaultPermissions == null) {
      this.defaultPermissions = new ArrayList<>();
    }
    this.defaultPermissions.add(defaultPermissionsItem);
    return this;
  }

  /**
   * Get defaultPermissions
   * @return defaultPermissions
   */
  
  @Schema(name = "default_permissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("default_permissions")
  public List<String> getDefaultPermissions() {
    return defaultPermissions;
  }

  public void setDefaultPermissions(List<String> defaultPermissions) {
    this.defaultPermissions = defaultPermissions;
  }

  public RoleDescriptor description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleDescriptor roleDescriptor = (RoleDescriptor) o;
    return Objects.equals(this.name, roleDescriptor.name) &&
        Objects.equals(this.inherits, roleDescriptor.inherits) &&
        Objects.equals(this.defaultPermissions, roleDescriptor.defaultPermissions) &&
        Objects.equals(this.description, roleDescriptor.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, inherits, defaultPermissions, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleDescriptor {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    inherits: ").append(toIndentedString(inherits)).append("\n");
    sb.append("    defaultPermissions: ").append(toIndentedString(defaultPermissions)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

