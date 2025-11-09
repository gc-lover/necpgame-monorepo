package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * PurgeCdnRequest
 */

@JsonTypeName("purgeCdn_request")

public class PurgeCdnRequest {

  @Valid
  private List<String> urls = new ArrayList<>();

  private @Nullable Boolean purgeAll;

  public PurgeCdnRequest urls(List<String> urls) {
    this.urls = urls;
    return this;
  }

  public PurgeCdnRequest addUrlsItem(String urlsItem) {
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

  public PurgeCdnRequest purgeAll(@Nullable Boolean purgeAll) {
    this.purgeAll = purgeAll;
    return this;
  }

  /**
   * Get purgeAll
   * @return purgeAll
   */
  
  @Schema(name = "purge_all", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purge_all")
  public @Nullable Boolean getPurgeAll() {
    return purgeAll;
  }

  public void setPurgeAll(@Nullable Boolean purgeAll) {
    this.purgeAll = purgeAll;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurgeCdnRequest purgeCdnRequest = (PurgeCdnRequest) o;
    return Objects.equals(this.urls, purgeCdnRequest.urls) &&
        Objects.equals(this.purgeAll, purgeCdnRequest.purgeAll);
  }

  @Override
  public int hashCode() {
    return Objects.hash(urls, purgeAll);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurgeCdnRequest {\n");
    sb.append("    urls: ").append(toIndentedString(urls)).append("\n");
    sb.append("    purgeAll: ").append(toIndentedString(purgeAll)).append("\n");
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

