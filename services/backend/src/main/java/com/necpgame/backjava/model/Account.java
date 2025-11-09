package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.Arrays;
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
 * Account
 */


public class Account {

  private UUID id;

  private String email;

  private String username;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> lastLogin = JsonNullable.<OffsetDateTime>undefined();

  private Boolean isActive;

  public Account() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Account(UUID id, String email, String username, OffsetDateTime createdAt, Boolean isActive) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.createdAt = createdAt;
    this.isActive = isActive;
  }

  public Account id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ Р°РєРєР°СѓРЅС‚Р°
   * @return id
   */
  @NotNull @Valid 
  @Schema(name = "id", example = "550e8400-e29b-41d4-a716-446655440000", description = "РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ Р°РєРєР°СѓРЅС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Account email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Email Р°РґСЂРµСЃ
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "player@example.com", description = "Email Р°РґСЂРµСЃ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Account username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Р›РѕРіРёРЅ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ
   * @return username
   */
  @NotNull 
  @Schema(name = "username", example = "player123", description = "Р›РѕРіРёРЅ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Account createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Р”Р°С‚Р° СЃРѕР·РґР°РЅРёСЏ Р°РєРєР°СѓРЅС‚Р°
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "created_at", example = "2025-01-27T12:00Z", description = "Р”Р°С‚Р° СЃРѕР·РґР°РЅРёСЏ Р°РєРєР°СѓРЅС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("created_at")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Account lastLogin(OffsetDateTime lastLogin) {
    this.lastLogin = JsonNullable.of(lastLogin);
    return this;
  }

  /**
   * Р”Р°С‚Р° РїРѕСЃР»РµРґРЅРµРіРѕ РІС…РѕРґР°
   * @return lastLogin
   */
  @Valid 
  @Schema(name = "last_login", example = "2025-01-27T12:00Z", description = "Р”Р°С‚Р° РїРѕСЃР»РµРґРЅРµРіРѕ РІС…РѕРґР°", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_login")
  public JsonNullable<OffsetDateTime> getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(JsonNullable<OffsetDateTime> lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Account isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * РђРєС‚РёРІРµРЅ Р»Рё Р°РєРєР°СѓРЅС‚
   * @return isActive
   */
  @NotNull 
  @Schema(name = "is_active", example = "true", description = "РђРєС‚РёРІРµРЅ Р»Рё Р°РєРєР°СѓРЅС‚", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("is_active")
  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.id, account.id) &&
        Objects.equals(this.email, account.email) &&
        Objects.equals(this.username, account.username) &&
        Objects.equals(this.createdAt, account.createdAt) &&
        equalsNullable(this.lastLogin, account.lastLogin) &&
        Objects.equals(this.isActive, account.isActive);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, username, createdAt, hashCodeNullable(lastLogin), isActive);
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
    sb.append("class Account {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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

