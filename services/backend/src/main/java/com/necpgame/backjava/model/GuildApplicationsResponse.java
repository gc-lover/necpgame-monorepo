package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GuildApplication;
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
 * GuildApplicationsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildApplicationsResponse {

  @Valid
  private List<@Valid GuildApplication> applications = new ArrayList<>();

  public GuildApplicationsResponse applications(List<@Valid GuildApplication> applications) {
    this.applications = applications;
    return this;
  }

  public GuildApplicationsResponse addApplicationsItem(GuildApplication applicationsItem) {
    if (this.applications == null) {
      this.applications = new ArrayList<>();
    }
    this.applications.add(applicationsItem);
    return this;
  }

  /**
   * Get applications
   * @return applications
   */
  @Valid 
  @Schema(name = "applications", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("applications")
  public List<@Valid GuildApplication> getApplications() {
    return applications;
  }

  public void setApplications(List<@Valid GuildApplication> applications) {
    this.applications = applications;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildApplicationsResponse guildApplicationsResponse = (GuildApplicationsResponse) o;
    return Objects.equals(this.applications, guildApplicationsResponse.applications);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applications);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildApplicationsResponse {\n");
    sb.append("    applications: ").append(toIndentedString(applications)).append("\n");
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

