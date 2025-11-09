package com.necpgame.narrativeservice.model;

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
 * ResolveOptionRequestClient
 */

@JsonTypeName("ResolveOptionRequest_client")

public class ResolveOptionRequestClient {

  private @Nullable String locale;

  private @Nullable String platform;

  private @Nullable String buildVersion;

  public ResolveOptionRequestClient locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public ResolveOptionRequestClient platform(@Nullable String platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  
  @Schema(name = "platform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("platform")
  public @Nullable String getPlatform() {
    return platform;
  }

  public void setPlatform(@Nullable String platform) {
    this.platform = platform;
  }

  public ResolveOptionRequestClient buildVersion(@Nullable String buildVersion) {
    this.buildVersion = buildVersion;
    return this;
  }

  /**
   * Get buildVersion
   * @return buildVersion
   */
  
  @Schema(name = "buildVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buildVersion")
  public @Nullable String getBuildVersion() {
    return buildVersion;
  }

  public void setBuildVersion(@Nullable String buildVersion) {
    this.buildVersion = buildVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResolveOptionRequestClient resolveOptionRequestClient = (ResolveOptionRequestClient) o;
    return Objects.equals(this.locale, resolveOptionRequestClient.locale) &&
        Objects.equals(this.platform, resolveOptionRequestClient.platform) &&
        Objects.equals(this.buildVersion, resolveOptionRequestClient.buildVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locale, platform, buildVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResolveOptionRequestClient {\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    buildVersion: ").append(toIndentedString(buildVersion)).append("\n");
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

