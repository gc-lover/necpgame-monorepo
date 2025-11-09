package com.necpgame.authservice.model;

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
 * TokenRefreshResponse
 */


public class TokenRefreshResponse {

  private @Nullable String accessToken;

  private @Nullable String refreshToken;

  private @Nullable String tokenType;

  private @Nullable Integer expiresIn;

  public TokenRefreshResponse accessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * Get accessToken
   * @return accessToken
   */
  
  @Schema(name = "access_token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_token")
  public @Nullable String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
  }

  public TokenRefreshResponse refreshToken(@Nullable String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  /**
   * Get refreshToken
   * @return refreshToken
   */
  
  @Schema(name = "refresh_token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refresh_token")
  public @Nullable String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(@Nullable String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public TokenRefreshResponse tokenType(@Nullable String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * Get tokenType
   * @return tokenType
   */
  
  @Schema(name = "token_type", example = "Bearer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token_type")
  public @Nullable String getTokenType() {
    return tokenType;
  }

  public void setTokenType(@Nullable String tokenType) {
    this.tokenType = tokenType;
  }

  public TokenRefreshResponse expiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  /**
   * Время жизни нового access token (сек)
   * @return expiresIn
   */
  
  @Schema(name = "expires_in", description = "Время жизни нового access token (сек)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_in")
  public @Nullable Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenRefreshResponse tokenRefreshResponse = (TokenRefreshResponse) o;
    return Objects.equals(this.accessToken, tokenRefreshResponse.accessToken) &&
        Objects.equals(this.refreshToken, tokenRefreshResponse.refreshToken) &&
        Objects.equals(this.tokenType, tokenRefreshResponse.tokenType) &&
        Objects.equals(this.expiresIn, tokenRefreshResponse.expiresIn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, refreshToken, tokenType, expiresIn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TokenRefreshResponse {\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
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

