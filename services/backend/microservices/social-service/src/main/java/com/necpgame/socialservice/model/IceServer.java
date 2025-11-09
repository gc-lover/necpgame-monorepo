package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * IceServer
 */


public class IceServer {

  @Valid
  private List<String> urls = new ArrayList<>();

  private @Nullable String username;

  private @Nullable String credential;

  public IceServer urls(List<String> urls) {
    this.urls = urls;
    return this;
  }

  public IceServer addUrlsItem(String urlsItem) {
    if (this.urls == null) {
      this.urls = new ArrayList<>();
    }
    this.urls.add(urlsItem);
    return this;
  }

  /**
   * Get urls
   * @return urls
   */
  
  @Schema(name = "urls", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("urls")
  public List<String> getUrls() {
    return urls;
  }

  public void setUrls(List<String> urls) {
    this.urls = urls;
  }

  public IceServer username(@Nullable String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  
  @Schema(name = "username", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public @Nullable String getUsername() {
    return username;
  }

  public void setUsername(@Nullable String username) {
    this.username = username;
  }

  public IceServer credential(@Nullable String credential) {
    this.credential = credential;
    return this;
  }

  /**
   * Get credential
   * @return credential
   */
  
  @Schema(name = "credential", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("credential")
  public @Nullable String getCredential() {
    return credential;
  }

  public void setCredential(@Nullable String credential) {
    this.credential = credential;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IceServer iceServer = (IceServer) o;
    return Objects.equals(this.urls, iceServer.urls) &&
        Objects.equals(this.username, iceServer.username) &&
        Objects.equals(this.credential, iceServer.credential);
  }

  @Override
  public int hashCode() {
    return Objects.hash(urls, username, credential);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IceServer {\n");
    sb.append("    urls: ").append(toIndentedString(urls)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    credential: ").append(toIndentedString(credential)).append("\n");
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

