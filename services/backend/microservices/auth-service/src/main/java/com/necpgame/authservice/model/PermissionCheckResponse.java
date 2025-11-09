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
 * PermissionCheckResponse
 */


public class PermissionCheckResponse {

  private @Nullable Boolean hasPermission;

  @Valid
  private List<String> evaluatedRoles = new ArrayList<>();

  private @Nullable String grantedBy;

  public PermissionCheckResponse hasPermission(@Nullable Boolean hasPermission) {
    this.hasPermission = hasPermission;
    return this;
  }

  /**
   * Get hasPermission
   * @return hasPermission
   */
  
  @Schema(name = "has_permission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("has_permission")
  public @Nullable Boolean getHasPermission() {
    return hasPermission;
  }

  public void setHasPermission(@Nullable Boolean hasPermission) {
    this.hasPermission = hasPermission;
  }

  public PermissionCheckResponse evaluatedRoles(List<String> evaluatedRoles) {
    this.evaluatedRoles = evaluatedRoles;
    return this;
  }

  public PermissionCheckResponse addEvaluatedRolesItem(String evaluatedRolesItem) {
    if (this.evaluatedRoles == null) {
      this.evaluatedRoles = new ArrayList<>();
    }
    this.evaluatedRoles.add(evaluatedRolesItem);
    return this;
  }

  /**
   * Get evaluatedRoles
   * @return evaluatedRoles
   */
  
  @Schema(name = "evaluated_roles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evaluated_roles")
  public List<String> getEvaluatedRoles() {
    return evaluatedRoles;
  }

  public void setEvaluatedRoles(List<String> evaluatedRoles) {
    this.evaluatedRoles = evaluatedRoles;
  }

  public PermissionCheckResponse grantedBy(@Nullable String grantedBy) {
    this.grantedBy = grantedBy;
    return this;
  }

  /**
   * Имя роли или явного разрешения, которое предоставило доступ
   * @return grantedBy
   */
  
  @Schema(name = "granted_by", description = "Имя роли или явного разрешения, которое предоставило доступ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("granted_by")
  public @Nullable String getGrantedBy() {
    return grantedBy;
  }

  public void setGrantedBy(@Nullable String grantedBy) {
    this.grantedBy = grantedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PermissionCheckResponse permissionCheckResponse = (PermissionCheckResponse) o;
    return Objects.equals(this.hasPermission, permissionCheckResponse.hasPermission) &&
        Objects.equals(this.evaluatedRoles, permissionCheckResponse.evaluatedRoles) &&
        Objects.equals(this.grantedBy, permissionCheckResponse.grantedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hasPermission, evaluatedRoles, grantedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PermissionCheckResponse {\n");
    sb.append("    hasPermission: ").append(toIndentedString(hasPermission)).append("\n");
    sb.append("    evaluatedRoles: ").append(toIndentedString(evaluatedRoles)).append("\n");
    sb.append("    grantedBy: ").append(toIndentedString(grantedBy)).append("\n");
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

