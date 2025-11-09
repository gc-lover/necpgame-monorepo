package com.necpgame.backjava.model;

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
 * RegisterRequest
 */


public class RegisterRequest {

  private String email;

  private String username;

  private String password;

  private String passwordConfirm;

  private Boolean termsAccepted;

  public RegisterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegisterRequest(String email, String username, String password, String passwordConfirm, Boolean termsAccepted) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.passwordConfirm = passwordConfirm;
    this.termsAccepted = termsAccepted;
  }

  public RegisterRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Email Р°РґСЂРµСЃ (РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ СѓРЅРёРєР°Р»СЊРЅС‹Рј)
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", example = "player@example.com", description = "Email Р°РґСЂРµСЃ (РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ СѓРЅРёРєР°Р»СЊРЅС‹Рј)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public RegisterRequest username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Р›РѕРіРёРЅ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ (3-20 СЃРёРјРІРѕР»РѕРІ, Р±СѓРєРІС‹, С†РёС„СЂС‹, РїРѕРґС‡РµСЂРєРёРІР°РЅРёСЏ, СѓРЅРёРєР°Р»СЊРЅС‹Р№)
   * @return username
   */
  @NotNull @Pattern(regexp = "^[a-zA-Z0-9_]+$") @Size(min = 3, max = 20) 
  @Schema(name = "username", example = "player123", description = "Р›РѕРіРёРЅ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ (3-20 СЃРёРјРІРѕР»РѕРІ, Р±СѓРєРІС‹, С†РёС„СЂС‹, РїРѕРґС‡РµСЂРєРёРІР°РЅРёСЏ, СѓРЅРёРєР°Р»СЊРЅС‹Р№)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public RegisterRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * РџР°СЂРѕР»СЊ (РјРёРЅРёРјСѓРј 8 СЃРёРјРІРѕР»РѕРІ, Р±СѓРєРІС‹, С†РёС„СЂС‹, СЃРїРµС†СЃРёРјРІРѕР»С‹)
   * @return password
   */
  @NotNull @Size(min = 8) 
  @Schema(name = "password", example = "SecurePass123!", description = "РџР°СЂРѕР»СЊ (РјРёРЅРёРјСѓРј 8 СЃРёРјРІРѕР»РѕРІ, Р±СѓРєРІС‹, С†РёС„СЂС‹, СЃРїРµС†СЃРёРјРІРѕР»С‹)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public RegisterRequest passwordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
    return this;
  }

  /**
   * РџРѕРґС‚РІРµСЂР¶РґРµРЅРёРµ РїР°СЂРѕР»СЏ (РґРѕР»Р¶РЅРѕ СЃРѕРІРїР°РґР°С‚СЊ СЃ password)
   * @return passwordConfirm
   */
  @NotNull 
  @Schema(name = "password_confirm", example = "SecurePass123!", description = "РџРѕРґС‚РІРµСЂР¶РґРµРЅРёРµ РїР°СЂРѕР»СЏ (РґРѕР»Р¶РЅРѕ СЃРѕРІРїР°РґР°С‚СЊ СЃ password)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password_confirm")
  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public RegisterRequest termsAccepted(Boolean termsAccepted) {
    this.termsAccepted = termsAccepted;
    return this;
  }

  /**
   * РЎРѕРіР»Р°СЃРёРµ СЃ СѓСЃР»РѕРІРёСЏРјРё РёСЃРїРѕР»СЊР·РѕРІР°РЅРёСЏ (РѕР±СЏР·Р°С‚РµР»СЊРЅРѕ true)
   * @return termsAccepted
   */
  @NotNull 
  @Schema(name = "terms_accepted", example = "true", description = "РЎРѕРіР»Р°СЃРёРµ СЃ СѓСЃР»РѕРІРёСЏРјРё РёСЃРїРѕР»СЊР·РѕРІР°РЅРёСЏ (РѕР±СЏР·Р°С‚РµР»СЊРЅРѕ true)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("terms_accepted")
  public Boolean getTermsAccepted() {
    return termsAccepted;
  }

  public void setTermsAccepted(Boolean termsAccepted) {
    this.termsAccepted = termsAccepted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterRequest registerRequest = (RegisterRequest) o;
    return Objects.equals(this.email, registerRequest.email) &&
        Objects.equals(this.username, registerRequest.username) &&
        Objects.equals(this.password, registerRequest.password) &&
        Objects.equals(this.passwordConfirm, registerRequest.passwordConfirm) &&
        Objects.equals(this.termsAccepted, registerRequest.termsAccepted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, username, password, passwordConfirm, termsAccepted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterRequest {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append("*").append("\n");
    sb.append("    passwordConfirm: ").append("*").append("\n");
    sb.append("    termsAccepted: ").append(toIndentedString(termsAccepted)).append("\n");
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

