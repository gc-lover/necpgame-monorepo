package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
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
 * IntegrationHook
 */


public class IntegrationHook {

  private @Nullable UUID hookId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    DEPLOYMENT("DEPLOYMENT"),
    
    INCIDENT("INCIDENT"),
    
    STATUS_PAGE("STATUS_PAGE");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private URI url;

  private @Nullable String secret;

  private Boolean enabled = true;

  public IntegrationHook() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IntegrationHook(TypeEnum type, URI url) {
    this.type = type;
    this.url = url;
  }

  public IntegrationHook hookId(@Nullable UUID hookId) {
    this.hookId = hookId;
    return this;
  }

  /**
   * Get hookId
   * @return hookId
   */
  @Valid 
  @Schema(name = "hookId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hookId")
  public @Nullable UUID getHookId() {
    return hookId;
  }

  public void setHookId(@Nullable UUID hookId) {
    this.hookId = hookId;
  }

  public IntegrationHook type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public IntegrationHook url(URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   */
  @NotNull @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("url")
  public URI getUrl() {
    return url;
  }

  public void setUrl(URI url) {
    this.url = url;
  }

  public IntegrationHook secret(@Nullable String secret) {
    this.secret = secret;
    return this;
  }

  /**
   * Get secret
   * @return secret
   */
  
  @Schema(name = "secret", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("secret")
  public @Nullable String getSecret() {
    return secret;
  }

  public void setSecret(@Nullable String secret) {
    this.secret = secret;
  }

  public IntegrationHook enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntegrationHook integrationHook = (IntegrationHook) o;
    return Objects.equals(this.hookId, integrationHook.hookId) &&
        Objects.equals(this.type, integrationHook.type) &&
        Objects.equals(this.url, integrationHook.url) &&
        Objects.equals(this.secret, integrationHook.secret) &&
        Objects.equals(this.enabled, integrationHook.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hookId, type, url, secret, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntegrationHook {\n");
    sb.append("    hookId: ").append(toIndentedString(hookId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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

