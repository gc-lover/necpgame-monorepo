package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OAuthCallbackRequest
 */


public class OAuthCallbackRequest {

  private String code;

  private @Nullable String state;

  private @Nullable URI redirectUri;

  public OAuthCallbackRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OAuthCallbackRequest(String code) {
    this.code = code;
  }

  public OAuthCallbackRequest code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public OAuthCallbackRequest state(@Nullable String state) {
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

  public OAuthCallbackRequest redirectUri(@Nullable URI redirectUri) {
    this.redirectUri = redirectUri;
    return this;
  }

  /**
   * Get redirectUri
   * @return redirectUri
   */
  @Valid 
  @Schema(name = "redirect_uri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("redirect_uri")
  public @Nullable URI getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(@Nullable URI redirectUri) {
    this.redirectUri = redirectUri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OAuthCallbackRequest oauthCallbackRequest = (OAuthCallbackRequest) o;
    return Objects.equals(this.code, oauthCallbackRequest.code) &&
        Objects.equals(this.state, oauthCallbackRequest.state) &&
        Objects.equals(this.redirectUri, oauthCallbackRequest.redirectUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, state, redirectUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuthCallbackRequest {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    redirectUri: ").append(toIndentedString(redirectUri)).append("\n");
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

