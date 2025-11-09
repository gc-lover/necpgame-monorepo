package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.VersionInfo;
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
 * GetVersions200Response
 */

@JsonTypeName("getVersions_200_response")

public class GetVersions200Response {

  private @Nullable String currentVersion;

  @Valid
  private List<@Valid VersionInfo> previousVersions = new ArrayList<>();

  public GetVersions200Response currentVersion(@Nullable String currentVersion) {
    this.currentVersion = currentVersion;
    return this;
  }

  /**
   * Get currentVersion
   * @return currentVersion
   */
  
  @Schema(name = "current_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_version")
  public @Nullable String getCurrentVersion() {
    return currentVersion;
  }

  public void setCurrentVersion(@Nullable String currentVersion) {
    this.currentVersion = currentVersion;
  }

  public GetVersions200Response previousVersions(List<@Valid VersionInfo> previousVersions) {
    this.previousVersions = previousVersions;
    return this;
  }

  public GetVersions200Response addPreviousVersionsItem(VersionInfo previousVersionsItem) {
    if (this.previousVersions == null) {
      this.previousVersions = new ArrayList<>();
    }
    this.previousVersions.add(previousVersionsItem);
    return this;
  }

  /**
   * Get previousVersions
   * @return previousVersions
   */
  @Valid 
  @Schema(name = "previous_versions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_versions")
  public List<@Valid VersionInfo> getPreviousVersions() {
    return previousVersions;
  }

  public void setPreviousVersions(List<@Valid VersionInfo> previousVersions) {
    this.previousVersions = previousVersions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetVersions200Response getVersions200Response = (GetVersions200Response) o;
    return Objects.equals(this.currentVersion, getVersions200Response.currentVersion) &&
        Objects.equals(this.previousVersions, getVersions200Response.previousVersions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentVersion, previousVersions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetVersions200Response {\n");
    sb.append("    currentVersion: ").append(toIndentedString(currentVersion)).append("\n");
    sb.append("    previousVersions: ").append(toIndentedString(previousVersions)).append("\n");
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

