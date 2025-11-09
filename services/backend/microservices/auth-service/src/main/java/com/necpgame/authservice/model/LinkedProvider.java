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
 * LinkedProvider
 */


public class LinkedProvider {

  private @Nullable OAuthProvider provider;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime linkedAt;

  private @Nullable String externalId;

  public LinkedProvider provider(@Nullable OAuthProvider provider) {
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

  public LinkedProvider linkedAt(@Nullable OffsetDateTime linkedAt) {
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

  public LinkedProvider externalId(@Nullable String externalId) {
    this.externalId = externalId;
    return this;
  }

  /**
   * Get externalId
   * @return externalId
   */
  
  @Schema(name = "external_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("external_id")
  public @Nullable String getExternalId() {
    return externalId;
  }

  public void setExternalId(@Nullable String externalId) {
    this.externalId = externalId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkedProvider linkedProvider = (LinkedProvider) o;
    return Objects.equals(this.provider, linkedProvider.provider) &&
        Objects.equals(this.linkedAt, linkedProvider.linkedAt) &&
        Objects.equals(this.externalId, linkedProvider.externalId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(provider, linkedAt, externalId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkedProvider {\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    linkedAt: ").append(toIndentedString(linkedAt)).append("\n");
    sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
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

