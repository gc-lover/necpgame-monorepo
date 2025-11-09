package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.time.OffsetDateTime;
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
 * OAuthAuthorizeResponse
 */


public class OAuthAuthorizeResponse {

  private @Nullable URI authorizeUrl;

  private @Nullable String state;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public OAuthAuthorizeResponse authorizeUrl(@Nullable URI authorizeUrl) {
    this.authorizeUrl = authorizeUrl;
    return this;
  }

  /**
   * Get authorizeUrl
   * @return authorizeUrl
   */
  @Valid 
  @Schema(name = "authorize_url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authorize_url")
  public @Nullable URI getAuthorizeUrl() {
    return authorizeUrl;
  }

  public void setAuthorizeUrl(@Nullable URI authorizeUrl) {
    this.authorizeUrl = authorizeUrl;
  }

  public OAuthAuthorizeResponse state(@Nullable String state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable String getState() {
    return state;
  }

  public void setState(@Nullable String state) {
    this.state = state;
  }

  public OAuthAuthorizeResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
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
    OAuthAuthorizeResponse oauthAuthorizeResponse = (OAuthAuthorizeResponse) o;
    return Objects.equals(this.authorizeUrl, oauthAuthorizeResponse.authorizeUrl) &&
        Objects.equals(this.state, oauthAuthorizeResponse.state) &&
        Objects.equals(this.expiresAt, oauthAuthorizeResponse.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorizeUrl, state, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuthAuthorizeResponse {\n");
    sb.append("    authorizeUrl: ").append(toIndentedString(authorizeUrl)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
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

