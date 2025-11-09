package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.EndpointDetailsAllOfErrorCodes;
import com.necpgame.adminservice.model.EndpointDetailsAllOfExamples;
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
 * EndpointDetails
 */


public class EndpointDetails {

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

  private @Nullable Object requestSchema;

  private @Nullable Object responseSchema;

  private @Nullable EndpointDetailsAllOfExamples examples;

  @Valid
  private List<@Valid EndpointDetailsAllOfErrorCodes> errorCodes = new ArrayList<>();

  @Valid
  private List<String> dependencies = new ArrayList<>();

  public EndpointDetails() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EndpointDetails(String endpointId, String path, MethodEnum method) {
    this.endpointId = endpointId;
    this.path = path;
    this.method = method;
  }

  public EndpointDetails endpointId(String endpointId) {
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

  public EndpointDetails path(String path) {
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

  public EndpointDetails method(MethodEnum method) {
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

  public EndpointDetails category(@Nullable String category) {
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

  public EndpointDetails service(@Nullable String service) {
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

  public EndpointDetails description(@Nullable String description) {
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

  public EndpointDetails parameters(List<@Valid EndpointInfoParametersInner> parameters) {
    this.parameters = parameters;
    return this;
  }

  public EndpointDetails addParametersItem(EndpointInfoParametersInner parametersItem) {
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

  public EndpointDetails responses(Map<String, Object> responses) {
    this.responses = responses;
    return this;
  }

  public EndpointDetails putResponsesItem(String key, Object responsesItem) {
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

  public EndpointDetails rateLimit(@Nullable EndpointInfoRateLimit rateLimit) {
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

  public EndpointDetails authenticationRequired(@Nullable Boolean authenticationRequired) {
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

  public EndpointDetails rolesRequired(List<String> rolesRequired) {
    this.rolesRequired = rolesRequired;
    return this;
  }

  public EndpointDetails addRolesRequiredItem(String rolesRequiredItem) {
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

  public EndpointDetails requestSchema(@Nullable Object requestSchema) {
    this.requestSchema = requestSchema;
    return this;
  }

  /**
   * Full JSON schema
   * @return requestSchema
   */
  
  @Schema(name = "request_schema", description = "Full JSON schema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("request_schema")
  public @Nullable Object getRequestSchema() {
    return requestSchema;
  }

  public void setRequestSchema(@Nullable Object requestSchema) {
    this.requestSchema = requestSchema;
  }

  public EndpointDetails responseSchema(@Nullable Object responseSchema) {
    this.responseSchema = responseSchema;
    return this;
  }

  /**
   * Full JSON schema
   * @return responseSchema
   */
  
  @Schema(name = "response_schema", description = "Full JSON schema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("response_schema")
  public @Nullable Object getResponseSchema() {
    return responseSchema;
  }

  public void setResponseSchema(@Nullable Object responseSchema) {
    this.responseSchema = responseSchema;
  }

  public EndpointDetails examples(@Nullable EndpointDetailsAllOfExamples examples) {
    this.examples = examples;
    return this;
  }

  /**
   * Get examples
   * @return examples
   */
  @Valid 
  @Schema(name = "examples", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("examples")
  public @Nullable EndpointDetailsAllOfExamples getExamples() {
    return examples;
  }

  public void setExamples(@Nullable EndpointDetailsAllOfExamples examples) {
    this.examples = examples;
  }

  public EndpointDetails errorCodes(List<@Valid EndpointDetailsAllOfErrorCodes> errorCodes) {
    this.errorCodes = errorCodes;
    return this;
  }

  public EndpointDetails addErrorCodesItem(EndpointDetailsAllOfErrorCodes errorCodesItem) {
    if (this.errorCodes == null) {
      this.errorCodes = new ArrayList<>();
    }
    this.errorCodes.add(errorCodesItem);
    return this;
  }

  /**
   * Get errorCodes
   * @return errorCodes
   */
  @Valid 
  @Schema(name = "error_codes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error_codes")
  public List<@Valid EndpointDetailsAllOfErrorCodes> getErrorCodes() {
    return errorCodes;
  }

  public void setErrorCodes(List<@Valid EndpointDetailsAllOfErrorCodes> errorCodes) {
    this.errorCodes = errorCodes;
  }

  public EndpointDetails dependencies(List<String> dependencies) {
    this.dependencies = dependencies;
    return this;
  }

  public EndpointDetails addDependenciesItem(String dependenciesItem) {
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<>();
    }
    this.dependencies.add(dependenciesItem);
    return this;
  }

  /**
   * Зависимости от других сервисов
   * @return dependencies
   */
  
  @Schema(name = "dependencies", description = "Зависимости от других сервисов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dependencies")
  public List<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointDetails endpointDetails = (EndpointDetails) o;
    return Objects.equals(this.endpointId, endpointDetails.endpointId) &&
        Objects.equals(this.path, endpointDetails.path) &&
        Objects.equals(this.method, endpointDetails.method) &&
        Objects.equals(this.category, endpointDetails.category) &&
        Objects.equals(this.service, endpointDetails.service) &&
        Objects.equals(this.description, endpointDetails.description) &&
        Objects.equals(this.parameters, endpointDetails.parameters) &&
        Objects.equals(this.responses, endpointDetails.responses) &&
        Objects.equals(this.rateLimit, endpointDetails.rateLimit) &&
        Objects.equals(this.authenticationRequired, endpointDetails.authenticationRequired) &&
        Objects.equals(this.rolesRequired, endpointDetails.rolesRequired) &&
        Objects.equals(this.requestSchema, endpointDetails.requestSchema) &&
        Objects.equals(this.responseSchema, endpointDetails.responseSchema) &&
        Objects.equals(this.examples, endpointDetails.examples) &&
        Objects.equals(this.errorCodes, endpointDetails.errorCodes) &&
        Objects.equals(this.dependencies, endpointDetails.dependencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpointId, path, method, category, service, description, parameters, responses, rateLimit, authenticationRequired, rolesRequired, requestSchema, responseSchema, examples, errorCodes, dependencies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointDetails {\n");
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
    sb.append("    requestSchema: ").append(toIndentedString(requestSchema)).append("\n");
    sb.append("    responseSchema: ").append(toIndentedString(responseSchema)).append("\n");
    sb.append("    examples: ").append(toIndentedString(examples)).append("\n");
    sb.append("    errorCodes: ").append(toIndentedString(errorCodes)).append("\n");
    sb.append("    dependencies: ").append(toIndentedString(dependencies)).append("\n");
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

