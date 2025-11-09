package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * VersionInfo
 */


public class VersionInfo {

  private @Nullable String version;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime releaseDate;

  @Valid
  private List<String> changelog = new ArrayList<>();

  private @Nullable Integer deploymentCount;

  public VersionInfo version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  public VersionInfo releaseDate(@Nullable OffsetDateTime releaseDate) {
    this.releaseDate = releaseDate;
    return this;
  }

  /**
   * Get releaseDate
   * @return releaseDate
   */
  @Valid 
  @Schema(name = "release_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("release_date")
  public @Nullable OffsetDateTime getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(@Nullable OffsetDateTime releaseDate) {
    this.releaseDate = releaseDate;
  }

  public VersionInfo changelog(List<String> changelog) {
    this.changelog = changelog;
    return this;
  }

  public VersionInfo addChangelogItem(String changelogItem) {
    if (this.changelog == null) {
      this.changelog = new ArrayList<>();
    }
    this.changelog.add(changelogItem);
    return this;
  }

  /**
   * Get changelog
   * @return changelog
   */
  
  @Schema(name = "changelog", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("changelog")
  public List<String> getChangelog() {
    return changelog;
  }

  public void setChangelog(List<String> changelog) {
    this.changelog = changelog;
  }

  public VersionInfo deploymentCount(@Nullable Integer deploymentCount) {
    this.deploymentCount = deploymentCount;
    return this;
  }

  /**
   * Get deploymentCount
   * @return deploymentCount
   */
  
  @Schema(name = "deployment_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deployment_count")
  public @Nullable Integer getDeploymentCount() {
    return deploymentCount;
  }

  public void setDeploymentCount(@Nullable Integer deploymentCount) {
    this.deploymentCount = deploymentCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersionInfo versionInfo = (VersionInfo) o;
    return Objects.equals(this.version, versionInfo.version) &&
        Objects.equals(this.releaseDate, versionInfo.releaseDate) &&
        Objects.equals(this.changelog, versionInfo.changelog) &&
        Objects.equals(this.deploymentCount, versionInfo.deploymentCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, releaseDate, changelog, deploymentCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VersionInfo {\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    releaseDate: ").append(toIndentedString(releaseDate)).append("\n");
    sb.append("    changelog: ").append(toIndentedString(changelog)).append("\n");
    sb.append("    deploymentCount: ").append(toIndentedString(deploymentCount)).append("\n");
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

