package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.SecurityCategoryScore;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SecurityAuditReportCategories
 */

@JsonTypeName("SecurityAuditReport_categories")

public class SecurityAuditReportCategories {

  private @Nullable SecurityCategoryScore authentication;

  private @Nullable SecurityCategoryScore authorization;

  private @Nullable SecurityCategoryScore dataProtection;

  private @Nullable SecurityCategoryScore apiSecurity;

  public SecurityAuditReportCategories authentication(@Nullable SecurityCategoryScore authentication) {
    this.authentication = authentication;
    return this;
  }

  /**
   * Get authentication
   * @return authentication
   */
  @Valid 
  @Schema(name = "authentication", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authentication")
  public @Nullable SecurityCategoryScore getAuthentication() {
    return authentication;
  }

  public void setAuthentication(@Nullable SecurityCategoryScore authentication) {
    this.authentication = authentication;
  }

  public SecurityAuditReportCategories authorization(@Nullable SecurityCategoryScore authorization) {
    this.authorization = authorization;
    return this;
  }

  /**
   * Get authorization
   * @return authorization
   */
  @Valid 
  @Schema(name = "authorization", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authorization")
  public @Nullable SecurityCategoryScore getAuthorization() {
    return authorization;
  }

  public void setAuthorization(@Nullable SecurityCategoryScore authorization) {
    this.authorization = authorization;
  }

  public SecurityAuditReportCategories dataProtection(@Nullable SecurityCategoryScore dataProtection) {
    this.dataProtection = dataProtection;
    return this;
  }

  /**
   * Get dataProtection
   * @return dataProtection
   */
  @Valid 
  @Schema(name = "data_protection", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data_protection")
  public @Nullable SecurityCategoryScore getDataProtection() {
    return dataProtection;
  }

  public void setDataProtection(@Nullable SecurityCategoryScore dataProtection) {
    this.dataProtection = dataProtection;
  }

  public SecurityAuditReportCategories apiSecurity(@Nullable SecurityCategoryScore apiSecurity) {
    this.apiSecurity = apiSecurity;
    return this;
  }

  /**
   * Get apiSecurity
   * @return apiSecurity
   */
  @Valid 
  @Schema(name = "api_security", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("api_security")
  public @Nullable SecurityCategoryScore getApiSecurity() {
    return apiSecurity;
  }

  public void setApiSecurity(@Nullable SecurityCategoryScore apiSecurity) {
    this.apiSecurity = apiSecurity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityAuditReportCategories securityAuditReportCategories = (SecurityAuditReportCategories) o;
    return Objects.equals(this.authentication, securityAuditReportCategories.authentication) &&
        Objects.equals(this.authorization, securityAuditReportCategories.authorization) &&
        Objects.equals(this.dataProtection, securityAuditReportCategories.dataProtection) &&
        Objects.equals(this.apiSecurity, securityAuditReportCategories.apiSecurity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authentication, authorization, dataProtection, apiSecurity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecurityAuditReportCategories {\n");
    sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
    sb.append("    authorization: ").append(toIndentedString(authorization)).append("\n");
    sb.append("    dataProtection: ").append(toIndentedString(dataProtection)).append("\n");
    sb.append("    apiSecurity: ").append(toIndentedString(apiSecurity)).append("\n");
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

