package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.TechnicalSecretsGet200ResponseSecretsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TechnicalSecretsGet200Response
 */

@JsonTypeName("_technical_secrets_get_200_response")

public class TechnicalSecretsGet200Response {

  @Valid
  private List<@Valid TechnicalSecretsGet200ResponseSecretsInner> secrets = new ArrayList<>();

  public TechnicalSecretsGet200Response secrets(List<@Valid TechnicalSecretsGet200ResponseSecretsInner> secrets) {
    this.secrets = secrets;
    return this;
  }

  public TechnicalSecretsGet200Response addSecretsItem(TechnicalSecretsGet200ResponseSecretsInner secretsItem) {
    if (this.secrets == null) {
      this.secrets = new ArrayList<>();
    }
    this.secrets.add(secretsItem);
    return this;
  }

  /**
   * Get secrets
   * @return secrets
   */
  @Valid 
  @Schema(name = "secrets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("secrets")
  public List<@Valid TechnicalSecretsGet200ResponseSecretsInner> getSecrets() {
    return secrets;
  }

  public void setSecrets(List<@Valid TechnicalSecretsGet200ResponseSecretsInner> secrets) {
    this.secrets = secrets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalSecretsGet200Response technicalSecretsGet200Response = (TechnicalSecretsGet200Response) o;
    return Objects.equals(this.secrets, technicalSecretsGet200Response.secrets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(secrets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalSecretsGet200Response {\n");
    sb.append("    secrets: ").append(toIndentedString(secrets)).append("\n");
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

