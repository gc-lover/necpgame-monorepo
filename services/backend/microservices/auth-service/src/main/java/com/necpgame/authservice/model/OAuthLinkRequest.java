package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.authservice.model.OAuthProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OAuthLinkRequest
 */


public class OAuthLinkRequest {

  private OAuthProvider provider;

  private String oauthToken;

  @Valid
  private Map<String, String> metadata = new HashMap<>();

  public OAuthLinkRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OAuthLinkRequest(OAuthProvider provider, String oauthToken) {
    this.provider = provider;
    this.oauthToken = oauthToken;
  }

  public OAuthLinkRequest provider(OAuthProvider provider) {
    this.provider = provider;
    return this;
  }

  /**
   * Get provider
   * @return provider
   */
  @NotNull @Valid 
  @Schema(name = "provider", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("provider")
  public OAuthProvider getProvider() {
    return provider;
  }

  public void setProvider(OAuthProvider provider) {
    this.provider = provider;
  }

  public OAuthLinkRequest oauthToken(String oauthToken) {
    this.oauthToken = oauthToken;
    return this;
  }

  /**
   * Get oauthToken
   * @return oauthToken
   */
  @NotNull 
  @Schema(name = "oauth_token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("oauth_token")
  public String getOauthToken() {
    return oauthToken;
  }

  public void setOauthToken(String oauthToken) {
    this.oauthToken = oauthToken;
  }

  public OAuthLinkRequest metadata(Map<String, String> metadata) {
    this.metadata = metadata;
    return this;
  }

  public OAuthLinkRequest putMetadataItem(String key, String metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Дополнительные данные от провайдера (displayName и т.п.)
   * @return metadata
   */
  
  @Schema(name = "metadata", description = "Дополнительные данные от провайдера (displayName и т.п.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, String> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OAuthLinkRequest oauthLinkRequest = (OAuthLinkRequest) o;
    return Objects.equals(this.provider, oauthLinkRequest.provider) &&
        Objects.equals(this.oauthToken, oauthLinkRequest.oauthToken) &&
        Objects.equals(this.metadata, oauthLinkRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(provider, oauthToken, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuthLinkRequest {\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    oauthToken: ").append(toIndentedString(oauthToken)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

