package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
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
 * PermissionCheckRequest
 */


public class PermissionCheckRequest {

  private UUID accountId;

  private String permission;

  @Valid
  private Map<String, String> context = new HashMap<>();

  public PermissionCheckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PermissionCheckRequest(UUID accountId, String permission) {
    this.accountId = accountId;
    this.permission = permission;
  }

  public PermissionCheckRequest accountId(UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @NotNull @Valid 
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("account_id")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public PermissionCheckRequest permission(String permission) {
    this.permission = permission;
    return this;
  }

  /**
   * Get permission
   * @return permission
   */
  @NotNull 
  @Schema(name = "permission", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("permission")
  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public PermissionCheckRequest context(Map<String, String> context) {
    this.context = context;
    return this;
  }

  public PermissionCheckRequest putContextItem(String key, String contextItem) {
    if (this.context == null) {
      this.context = new HashMap<>();
    }
    this.context.put(key, contextItem);
    return this;
  }

  /**
   * Опциональный контекст проверки (guildId, locationId и т.п.)
   * @return context
   */
  
  @Schema(name = "context", description = "Опциональный контекст проверки (guildId, locationId и т.п.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public Map<String, String> getContext() {
    return context;
  }

  public void setContext(Map<String, String> context) {
    this.context = context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PermissionCheckRequest permissionCheckRequest = (PermissionCheckRequest) o;
    return Objects.equals(this.accountId, permissionCheckRequest.accountId) &&
        Objects.equals(this.permission, permissionCheckRequest.permission) &&
        Objects.equals(this.context, permissionCheckRequest.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, permission, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PermissionCheckRequest {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

