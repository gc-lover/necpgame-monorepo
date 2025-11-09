package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AccountProfile
 */


public class AccountProfile {

  private @Nullable UUID accountId;

  private @Nullable String email;

  private @Nullable String username;

  private @Nullable String displayName;

  @Valid
  private List<String> roles = new ArrayList<>();

  @Valid
  private List<String> permissions = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastLoginAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public AccountProfile accountId(@Nullable UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @Valid 
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable UUID accountId) {
    this.accountId = accountId;
  }

  public AccountProfile email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public AccountProfile username(@Nullable String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  
  @Schema(name = "username", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public @Nullable String getUsername() {
    return username;
  }

  public void setUsername(@Nullable String username) {
    this.username = username;
  }

  public AccountProfile displayName(@Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "display_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("display_name")
  public @Nullable String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@Nullable String displayName) {
    this.displayName = displayName;
  }

  public AccountProfile roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public AccountProfile addRolesItem(String rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
   */
  
  @Schema(name = "roles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roles")
  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public AccountProfile permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public AccountProfile addPermissionsItem(String permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * Get permissions
   * @return permissions
   */
  
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public AccountProfile lastLoginAt(@Nullable OffsetDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
    return this;
  }

  /**
   * Get lastLoginAt
   * @return lastLoginAt
   */
  @Valid 
  @Schema(name = "last_login_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_login_at")
  public @Nullable OffsetDateTime getLastLoginAt() {
    return lastLoginAt;
  }

  public void setLastLoginAt(@Nullable OffsetDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }

  public AccountProfile createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountProfile accountProfile = (AccountProfile) o;
    return Objects.equals(this.accountId, accountProfile.accountId) &&
        Objects.equals(this.email, accountProfile.email) &&
        Objects.equals(this.username, accountProfile.username) &&
        Objects.equals(this.displayName, accountProfile.displayName) &&
        Objects.equals(this.roles, accountProfile.roles) &&
        Objects.equals(this.permissions, accountProfile.permissions) &&
        Objects.equals(this.lastLoginAt, accountProfile.lastLoginAt) &&
        Objects.equals(this.createdAt, accountProfile.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, email, username, displayName, roles, permissions, lastLoginAt, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountProfile {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    lastLoginAt: ").append(toIndentedString(lastLoginAt)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

