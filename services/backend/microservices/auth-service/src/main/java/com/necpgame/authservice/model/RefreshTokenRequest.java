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
 * RefreshTokenRequest
 */


public class RefreshTokenRequest {

  private String refreshToken;

  public RefreshTokenRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RefreshTokenRequest(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public RefreshTokenRequest refreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  /**
   * Refresh token в формате JWT/opaque
   * @return refreshToken
   */
  @NotNull 
  @Schema(name = "refresh_token", description = "Refresh token в формате JWT/opaque", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("refresh_token")
  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RefreshTokenRequest refreshTokenRequest = (RefreshTokenRequest) o;
    return Objects.equals(this.refreshToken, refreshTokenRequest.refreshToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(refreshToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RefreshTokenRequest {\n");
    sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
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

