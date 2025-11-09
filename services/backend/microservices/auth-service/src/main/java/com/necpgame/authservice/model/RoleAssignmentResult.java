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
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RoleAssignmentResult
 */


public class RoleAssignmentResult {

  private @Nullable UUID accountId;

  @Valid
  private List<String> activeRoles = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> expiresAt = JsonNullable.<OffsetDateTime>undefined();

  public RoleAssignmentResult accountId(@Nullable UUID accountId) {
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

  public RoleAssignmentResult activeRoles(List<String> activeRoles) {
    this.activeRoles = activeRoles;
    return this;
  }

  public RoleAssignmentResult addActiveRolesItem(String activeRolesItem) {
    if (this.activeRoles == null) {
      this.activeRoles = new ArrayList<>();
    }
    this.activeRoles.add(activeRolesItem);
    return this;
  }

  /**
   * Get activeRoles
   * @return activeRoles
   */
  
  @Schema(name = "active_roles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_roles")
  public List<String> getActiveRoles() {
    return activeRoles;
  }

  public void setActiveRoles(List<String> activeRoles) {
    this.activeRoles = activeRoles;
  }

  public RoleAssignmentResult expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = JsonNullable.of(expiresAt);
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public JsonNullable<OffsetDateTime> getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(JsonNullable<OffsetDateTime> expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleAssignmentResult roleAssignmentResult = (RoleAssignmentResult) o;
    return Objects.equals(this.accountId, roleAssignmentResult.accountId) &&
        Objects.equals(this.activeRoles, roleAssignmentResult.activeRoles) &&
        equalsNullable(this.expiresAt, roleAssignmentResult.expiresAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, activeRoles, hashCodeNullable(expiresAt));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleAssignmentResult {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    activeRoles: ").append(toIndentedString(activeRoles)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

