package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.RiskContext;
import com.necpgame.economyservice.model.RiskFactorOverride;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RiskEvaluationRequest
 */


public class RiskEvaluationRequest {

  private UUID orderId;

  private UUID requesterId;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client"),
    
    OPERATOR("operator");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RoleEnum role;

  private RiskContext context;

  @Valid
  private List<@Valid RiskFactorOverride> overrideFactors = new ArrayList<>();

  public RiskEvaluationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskEvaluationRequest(UUID orderId, UUID requesterId, RiskContext context) {
    this.orderId = orderId;
    this.requesterId = requesterId;
    this.context = context;
  }

  public RiskEvaluationRequest orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public RiskEvaluationRequest requesterId(UUID requesterId) {
    this.requesterId = requesterId;
    return this;
  }

  /**
   * Get requesterId
   * @return requesterId
   */
  @NotNull @Valid 
  @Schema(name = "requesterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requesterId")
  public UUID getRequesterId() {
    return requesterId;
  }

  public void setRequesterId(UUID requesterId) {
    this.requesterId = requesterId;
  }

  public RiskEvaluationRequest role(@Nullable RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable RoleEnum getRole() {
    return role;
  }

  public void setRole(@Nullable RoleEnum role) {
    this.role = role;
  }

  public RiskEvaluationRequest context(RiskContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @NotNull @Valid 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("context")
  public RiskContext getContext() {
    return context;
  }

  public void setContext(RiskContext context) {
    this.context = context;
  }

  public RiskEvaluationRequest overrideFactors(List<@Valid RiskFactorOverride> overrideFactors) {
    this.overrideFactors = overrideFactors;
    return this;
  }

  public RiskEvaluationRequest addOverrideFactorsItem(RiskFactorOverride overrideFactorsItem) {
    if (this.overrideFactors == null) {
      this.overrideFactors = new ArrayList<>();
    }
    this.overrideFactors.add(overrideFactorsItem);
    return this;
  }

  /**
   * Get overrideFactors
   * @return overrideFactors
   */
  @Valid 
  @Schema(name = "overrideFactors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overrideFactors")
  public List<@Valid RiskFactorOverride> getOverrideFactors() {
    return overrideFactors;
  }

  public void setOverrideFactors(List<@Valid RiskFactorOverride> overrideFactors) {
    this.overrideFactors = overrideFactors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskEvaluationRequest riskEvaluationRequest = (RiskEvaluationRequest) o;
    return Objects.equals(this.orderId, riskEvaluationRequest.orderId) &&
        Objects.equals(this.requesterId, riskEvaluationRequest.requesterId) &&
        Objects.equals(this.role, riskEvaluationRequest.role) &&
        Objects.equals(this.context, riskEvaluationRequest.context) &&
        Objects.equals(this.overrideFactors, riskEvaluationRequest.overrideFactors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, requesterId, role, context, overrideFactors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskEvaluationRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    requesterId: ").append(toIndentedString(requesterId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    overrideFactors: ").append(toIndentedString(overrideFactors)).append("\n");
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

