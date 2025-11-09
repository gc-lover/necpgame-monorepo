package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TechnicalSecretsGet200ResponseSecretsInner
 */

@JsonTypeName("_technical_secrets_get_200_response_secrets_inner")

public class TechnicalSecretsGet200ResponseSecretsInner {

  private @Nullable String secretName;

  private @Nullable String createdAt;

  private @Nullable String updatedAt;

  public TechnicalSecretsGet200ResponseSecretsInner secretName(@Nullable String secretName) {
    this.secretName = secretName;
    return this;
  }

  /**
   * Get secretName
   * @return secretName
   */
  
  @Schema(name = "secret_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("secret_name")
  public @Nullable String getSecretName() {
    return secretName;
  }

  public void setSecretName(@Nullable String secretName) {
    this.secretName = secretName;
  }

  public TechnicalSecretsGet200ResponseSecretsInner createdAt(@Nullable String createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable String createdAt) {
    this.createdAt = createdAt;
  }

  public TechnicalSecretsGet200ResponseSecretsInner updatedAt(@Nullable String updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  
  @Schema(name = "updated_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated_at")
  public @Nullable String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable String updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalSecretsGet200ResponseSecretsInner technicalSecretsGet200ResponseSecretsInner = (TechnicalSecretsGet200ResponseSecretsInner) o;
    return Objects.equals(this.secretName, technicalSecretsGet200ResponseSecretsInner.secretName) &&
        Objects.equals(this.createdAt, technicalSecretsGet200ResponseSecretsInner.createdAt) &&
        Objects.equals(this.updatedAt, technicalSecretsGet200ResponseSecretsInner.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(secretName, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalSecretsGet200ResponseSecretsInner {\n");
    sb.append("    secretName: ").append(toIndentedString(secretName)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

