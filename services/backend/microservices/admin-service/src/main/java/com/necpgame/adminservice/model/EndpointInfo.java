package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.EndpointInfoParametersInner;
import com.necpgame.adminservice.model.EndpointInfoRateLimit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EndpointInfo
 */


public class EndpointInfo {

  private String endpointId;

  private String path;

  /**
   * Gets or Sets method
   */
  public enum MethodEnum {
    GET("GET"),
    
    POST("POST"),
    
    PUT("PUT"),
    
    PATCH("PATCH"),
    
    DELETE("DELETE");

    private final String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String value) {
      for (MethodEnum b : MethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MethodEnum method;

  private @Nullable String category;

  private @Nullable String service;

  private @Nullable String description;

  @Valid
  private List<@Valid EndpointInfoParametersInner> parameters = new ArrayList<>();

  @Valid
  private Map<String, Object> responses = new HashMap<>();

  private @Nullable EndpointInfoRateLimit rateLimit;

  private @Nullable Boolean authenticationRequired;

  @Valid
  private List<String> rolesRequired = new ArrayList<>();

  public EndpointInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EndpointInfo(String endpointId, String path, MethodEnum method) {
    this.endpointId = endpointId;
    this.path = path;
    this.method = method;
  }

  public EndpointInfo endpointId(String endpointId) {
    this.endpointId = endpointId;
    return this;
  }

  /**
   * Get endpointId
   * @return endpointId
   */
  @NotNull 
  @Schema(name = "endpoint_id", example = "auth_login", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endpoint_id")
  public String getEndpointId() {
    return endpointId;
  }

  public void setEndpointId(String endpointId) {
    this.endpointId = endpointId;
  }

  public EndpointInfo path(String path) {
    this.path = path;
    return this;
  }

  /**
   * Get path
   * @return path
   */
  @NotNull 
  @Schema(name = "path", example = "/api/v1/auth/login", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public EndpointInfo method(MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * Get method
   * @return method
   */
  @NotNull 
  @Schema(name = "method", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("method")
  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  public EndpointInfo category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", example = "authentication", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public EndpointInfo service(@Nullable String service) {
    this.service = service;
    return this;
  }

  /**
   * Get service
   * @return service
   */
  
  @Schema(name = "service", example = "auth-service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service")
  public @Nullable String getService() {
    return service;
  }

  public void setService(@Nullable String service) {
    this.service = service;
  }

  public EndpointInfo description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", example = "User login endpoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public EndpointInfo parameters(List<@Valid EndpointInfoParametersInner> parameters) {
    this.parameters = parameters;
    return this;
  }

  public EndpointInfo addParametersItem(EndpointInfoParametersInner parametersItem) {
    if (this.parameters == null) {
      this.parameters = new ArrayList<>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

  /**
   * Get parameters
   * @return parameters
   */
  @Valid 
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public List<@Valid EndpointInfoParametersInner> getParameters() {
    return parameters;
  }

  public void setParameters(List<@Valid EndpointInfoParametersInner> parameters) {
    this.parameters = parameters;
  }

  public EndpointInfo responses(Map<String, Object> responses) {
    this.responses = responses;
    return this;
  }

  public EndpointInfo putResponsesItem(String key, Object responsesItem) {
    if (this.responses == null) {
      this.responses = new HashMap<>();
    }
    this.responses.put(key, responsesItem);
    return this;
  }

  /**
   * Get responses
   * @return responses
   */
  
  @Schema(name = "responses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("responses")
  public Map<String, Object> getResponses() {
    return responses;
  }

  public void setResponses(Map<String, Object> responses) {
    this.responses = responses;
  }

  public EndpointInfo rateLimit(@Nullable EndpointInfoRateLimit rateLimit) {
    this.rateLimit = rateLimit;
    return this;
  }

  /**
   * Get rateLimit
   * @return rateLimit
   */
  @Valid 
  @Schema(name = "rate_limit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rate_limit")
  public @Nullable EndpointInfoRateLimit getRateLimit() {
    return rateLimit;
  }

  public void setRateLimit(@Nullable EndpointInfoRateLimit rateLimit) {
    this.rateLimit = rateLimit;
  }

  public EndpointInfo authenticationRequired(@Nullable Boolean authenticationRequired) {
    this.authenticationRequired = authenticationRequired;
    return this;
  }

  /**
   * Get authenticationRequired
   * @return authenticationRequired
   */
  
  @Schema(name = "authentication_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("authentication_required")
  public @Nullable Boolean getAuthenticationRequired() {
    return authenticationRequired;
  }

  public void setAuthenticationRequired(@Nullable Boolean authenticationRequired) {
    this.authenticationRequired = authenticationRequired;
  }

  public EndpointInfo rolesRequired(List<String> rolesRequired) {
    this.rolesRequired = rolesRequired;
    return this;
  }

  public EndpointInfo addRolesRequiredItem(String rolesRequiredItem) {
    if (this.rolesRequired == null) {
      this.rolesRequired = new ArrayList<>();
    }
    this.rolesRequired.add(rolesRequiredItem);
    return this;
  }

  /**
   * Get rolesRequired
   * @return rolesRequired
   */
  
  @Schema(name = "roles_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roles_required")
  public List<String> getRolesRequired() {
    return rolesRequired;
  }

  public void setRolesRequired(List<String> rolesRequired) {
    this.rolesRequired = rolesRequired;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointInfo endpointInfo = (EndpointInfo) o;
    return Objects.equals(this.endpointId, endpointInfo.endpointId) &&
        Objects.equals(this.path, endpointInfo.path) &&
        Objects.equals(this.method, endpointInfo.method) &&
        Objects.equals(this.category, endpointInfo.category) &&
        Objects.equals(this.service, endpointInfo.service) &&
        Objects.equals(this.description, endpointInfo.description) &&
        Objects.equals(this.parameters, endpointInfo.parameters) &&
        Objects.equals(this.responses, endpointInfo.responses) &&
        Objects.equals(this.rateLimit, endpointInfo.rateLimit) &&
        Objects.equals(this.authenticationRequired, endpointInfo.authenticationRequired) &&
        Objects.equals(this.rolesRequired, endpointInfo.rolesRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpointId, path, method, category, service, description, parameters, responses, rateLimit, authenticationRequired, rolesRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointInfo {\n");
    sb.append("    endpointId: ").append(toIndentedString(endpointId)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    responses: ").append(toIndentedString(responses)).append("\n");
    sb.append("    rateLimit: ").append(toIndentedString(rateLimit)).append("\n");
    sb.append("    authenticationRequired: ").append(toIndentedString(authenticationRequired)).append("\n");
    sb.append("    rolesRequired: ").append(toIndentedString(rolesRequired)).append("\n");
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

