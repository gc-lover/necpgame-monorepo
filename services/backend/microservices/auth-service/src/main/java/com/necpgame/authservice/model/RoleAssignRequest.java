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
 * RoleAssignRequest
 */


public class RoleAssignRequest {

  private UUID accountId;

  private String role;

  @Valid
  private List<String> permissions = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime grantedUntil;

  private @Nullable String reason;

  public RoleAssignRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RoleAssignRequest(UUID accountId, String role) {
    this.accountId = accountId;
    this.role = role;
  }

  public RoleAssignRequest accountId(UUID accountId) {
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

  public RoleAssignRequest role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Имя роли, например PLAYER, MODERATOR, ADMIN, SUPER_ADMIN
   * @return role
   */
  @NotNull 
  @Schema(name = "role", description = "Имя роли, например PLAYER, MODERATOR, ADMIN, SUPER_ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public RoleAssignRequest permissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public RoleAssignRequest addPermissionsItem(String permissionsItem) {
    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    this.permissions.add(permissionsItem);
    return this;
  }

  /**
   * Дополнительные granular permissions
   * @return permissions
   */
  
  @Schema(name = "permissions", description = "Дополнительные granular permissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public RoleAssignRequest grantedUntil(@Nullable OffsetDateTime grantedUntil) {
    this.grantedUntil = grantedUntil;
    return this;
  }

  /**
   * Время автоматического истечения роли (опционально)
   * @return grantedUntil
   */
  @Valid 
  @Schema(name = "granted_until", description = "Время автоматического истечения роли (опционально)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("granted_until")
  public @Nullable OffsetDateTime getGrantedUntil() {
    return grantedUntil;
  }

  public void setGrantedUntil(@Nullable OffsetDateTime grantedUntil) {
    this.grantedUntil = grantedUntil;
  }

  public RoleAssignRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Заметка для аудита
   * @return reason
   */
  
  @Schema(name = "reason", description = "Заметка для аудита", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleAssignRequest roleAssignRequest = (RoleAssignRequest) o;
    return Objects.equals(this.accountId, roleAssignRequest.accountId) &&
        Objects.equals(this.role, roleAssignRequest.role) &&
        Objects.equals(this.permissions, roleAssignRequest.permissions) &&
        Objects.equals(this.grantedUntil, roleAssignRequest.grantedUntil) &&
        Objects.equals(this.reason, roleAssignRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, role, permissions, grantedUntil, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleAssignRequest {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    grantedUntil: ").append(toIndentedString(grantedUntil)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

