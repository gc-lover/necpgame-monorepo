package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildPermission
 */


public class GuildPermission {

  private @Nullable String permission;

  private @Nullable String description;

  public GuildPermission permission(@Nullable String permission) {
    this.permission = permission;
    return this;
  }

  /**
   * Get permission
   * @return permission
   */
  
  @Schema(name = "permission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permission")
  public @Nullable String getPermission() {
    return permission;
  }

  public void setPermission(@Nullable String permission) {
    this.permission = permission;
  }

  public GuildPermission description(@Nullable String description) {
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
    GuildPermission guildPermission = (GuildPermission) o;
    return Objects.equals(this.permission, guildPermission.permission) &&
        Objects.equals(this.description, guildPermission.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permission, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildPermission {\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
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

