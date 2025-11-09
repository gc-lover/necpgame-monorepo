package com.necpgame.worldservice.model;

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
 * EraSettingsTechnologies
 */

@JsonTypeName("EraSettings_technologies")

public class EraSettingsTechnologies {

  private @Nullable String implantsLevel;

  private @Nullable String networkStatus;

  public EraSettingsTechnologies implantsLevel(@Nullable String implantsLevel) {
    this.implantsLevel = implantsLevel;
    return this;
  }

  /**
   * Get implantsLevel
   * @return implantsLevel
   */
  
  @Schema(name = "implants_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implants_level")
  public @Nullable String getImplantsLevel() {
    return implantsLevel;
  }

  public void setImplantsLevel(@Nullable String implantsLevel) {
    this.implantsLevel = implantsLevel;
  }

  public EraSettingsTechnologies networkStatus(@Nullable String networkStatus) {
    this.networkStatus = networkStatus;
    return this;
  }

  /**
   * Get networkStatus
   * @return networkStatus
   */
  
  @Schema(name = "network_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("network_status")
  public @Nullable String getNetworkStatus() {
    return networkStatus;
  }

  public void setNetworkStatus(@Nullable String networkStatus) {
    this.networkStatus = networkStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraSettingsTechnologies eraSettingsTechnologies = (EraSettingsTechnologies) o;
    return Objects.equals(this.implantsLevel, eraSettingsTechnologies.implantsLevel) &&
        Objects.equals(this.networkStatus, eraSettingsTechnologies.networkStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantsLevel, networkStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraSettingsTechnologies {\n");
    sb.append("    implantsLevel: ").append(toIndentedString(implantsLevel)).append("\n");
    sb.append("    networkStatus: ").append(toIndentedString(networkStatus)).append("\n");
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

