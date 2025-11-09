package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.systemservice.model.IntegrationHook;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * IntegrationHooks
 */


public class IntegrationHooks {

  private @Nullable IntegrationHook deployment;

  private @Nullable IntegrationHook incident;

  private @Nullable IntegrationHook statusPage;

  public IntegrationHooks deployment(@Nullable IntegrationHook deployment) {
    this.deployment = deployment;
    return this;
  }

  /**
   * Get deployment
   * @return deployment
   */
  @Valid 
  @Schema(name = "deployment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deployment")
  public @Nullable IntegrationHook getDeployment() {
    return deployment;
  }

  public void setDeployment(@Nullable IntegrationHook deployment) {
    this.deployment = deployment;
  }

  public IntegrationHooks incident(@Nullable IntegrationHook incident) {
    this.incident = incident;
    return this;
  }

  /**
   * Get incident
   * @return incident
   */
  @Valid 
  @Schema(name = "incident", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incident")
  public @Nullable IntegrationHook getIncident() {
    return incident;
  }

  public void setIncident(@Nullable IntegrationHook incident) {
    this.incident = incident;
  }

  public IntegrationHooks statusPage(@Nullable IntegrationHook statusPage) {
    this.statusPage = statusPage;
    return this;
  }

  /**
   * Get statusPage
   * @return statusPage
   */
  @Valid 
  @Schema(name = "statusPage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusPage")
  public @Nullable IntegrationHook getStatusPage() {
    return statusPage;
  }

  public void setStatusPage(@Nullable IntegrationHook statusPage) {
    this.statusPage = statusPage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntegrationHooks integrationHooks = (IntegrationHooks) o;
    return Objects.equals(this.deployment, integrationHooks.deployment) &&
        Objects.equals(this.incident, integrationHooks.incident) &&
        Objects.equals(this.statusPage, integrationHooks.statusPage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deployment, incident, statusPage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IntegrationHooks {\n");
    sb.append("    deployment: ").append(toIndentedString(deployment)).append("\n");
    sb.append("    incident: ").append(toIndentedString(incident)).append("\n");
    sb.append("    statusPage: ").append(toIndentedString(statusPage)).append("\n");
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

