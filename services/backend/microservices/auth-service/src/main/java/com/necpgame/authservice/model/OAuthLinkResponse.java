package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.authservice.model.OAuthProvider;
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
 * OAuthLinkResponse
 */


public class OAuthLinkResponse {

  private @Nullable OAuthProvider provider;

  private @Nullable Boolean linked;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime linkedAt;

  public OAuthLinkResponse provider(@Nullable OAuthProvider provider) {
    this.provider = provider;
    return this;
  }

  /**
   * Get provider
   * @return provider
   */
  @Valid 
  @Schema(name = "provider", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("provider")
  public @Nullable OAuthProvider getProvider() {
    return provider;
  }

  public void setProvider(@Nullable OAuthProvider provider) {
    this.provider = provider;
  }

  public OAuthLinkResponse linked(@Nullable Boolean linked) {
    this.linked = linked;
    return this;
  }

  /**
   * Get linked
   * @return linked
   */
  
  @Schema(name = "linked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("linked")
  public @Nullable Boolean getLinked() {
    return linked;
  }

  public void setLinked(@Nullable Boolean linked) {
    this.linked = linked;
  }

  public OAuthLinkResponse linkedAt(@Nullable OffsetDateTime linkedAt) {
    this.linkedAt = linkedAt;
    return this;
  }

  /**
   * Get linkedAt
   * @return linkedAt
   */
  @Valid 
  @Schema(name = "linked_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("linked_at")
  public @Nullable OffsetDateTime getLinkedAt() {
    return linkedAt;
  }

  public void setLinkedAt(@Nullable OffsetDateTime linkedAt) {
    this.linkedAt = linkedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OAuthLinkResponse oauthLinkResponse = (OAuthLinkResponse) o;
    return Objects.equals(this.provider, oauthLinkResponse.provider) &&
        Objects.equals(this.linked, oauthLinkResponse.linked) &&
        Objects.equals(this.linkedAt, oauthLinkResponse.linkedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(provider, linked, linkedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuthLinkResponse {\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    linked: ").append(toIndentedString(linked)).append("\n");
    sb.append("    linkedAt: ").append(toIndentedString(linkedAt)).append("\n");
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

