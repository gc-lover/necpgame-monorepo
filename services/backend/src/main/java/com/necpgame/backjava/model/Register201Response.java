package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Register201Response
 */

@JsonTypeName("register_201_response")

public class Register201Response {

  private UUID accountId;

  private String message;

  public Register201Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Register201Response(UUID accountId, String message) {
    this.accountId = accountId;
    this.message = message;
  }

  public Register201Response accountId(UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃРѕР·РґР°РЅРЅРѕРіРѕ Р°РєРєР°СѓРЅС‚Р°
   * @return accountId
   */
  @NotNull @Valid 
  @Schema(name = "account_id", example = "550e8400-e29b-41d4-a716-446655440000", description = "РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ СЃРѕР·РґР°РЅРЅРѕРіРѕ Р°РєРєР°СѓРЅС‚Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("account_id")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public Register201Response message(String message) {
    this.message = message;
    return this;
  }

  /**
   * РЎРѕРѕР±С‰РµРЅРёРµ РѕР± СѓСЃРїРµС€РЅРѕР№ СЂРµРіРёСЃС‚СЂР°С†РёРё
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "Account created successfully", description = "РЎРѕРѕР±С‰РµРЅРёРµ РѕР± СѓСЃРїРµС€РЅРѕР№ СЂРµРіРёСЃС‚СЂР°С†РёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Register201Response register201Response = (Register201Response) o;
    return Objects.equals(this.accountId, register201Response.accountId) &&
        Objects.equals(this.message, register201Response.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Register201Response {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

