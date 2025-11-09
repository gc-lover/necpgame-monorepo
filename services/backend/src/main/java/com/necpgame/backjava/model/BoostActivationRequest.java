package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BoostActivationRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class BoostActivationRequest {

  private String boostId;

  /**
   * Gets or Sets activationSource
   */
  public enum ActivationSourceEnum {
    TOKEN("TOKEN"),
    
    EVENT("EVENT"),
    
    ADMIN("ADMIN");

    private final String value;

    ActivationSourceEnum(String value) {
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
    public static ActivationSourceEnum fromValue(String value) {
      for (ActivationSourceEnum b : ActivationSourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ActivationSourceEnum activationSource;

  private @Nullable Integer cost;

  public BoostActivationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BoostActivationRequest(String boostId) {
    this.boostId = boostId;
  }

  public BoostActivationRequest boostId(String boostId) {
    this.boostId = boostId;
    return this;
  }

  /**
   * Get boostId
   * @return boostId
   */
  @NotNull 
  @Schema(name = "boostId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("boostId")
  public String getBoostId() {
    return boostId;
  }

  public void setBoostId(String boostId) {
    this.boostId = boostId;
  }

  public BoostActivationRequest activationSource(@Nullable ActivationSourceEnum activationSource) {
    this.activationSource = activationSource;
    return this;
  }

  /**
   * Get activationSource
   * @return activationSource
   */
  
  @Schema(name = "activationSource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activationSource")
  public @Nullable ActivationSourceEnum getActivationSource() {
    return activationSource;
  }

  public void setActivationSource(@Nullable ActivationSourceEnum activationSource) {
    this.activationSource = activationSource;
  }

  public BoostActivationRequest cost(@Nullable Integer cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable Integer getCost() {
    return cost;
  }

  public void setCost(@Nullable Integer cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoostActivationRequest boostActivationRequest = (BoostActivationRequest) o;
    return Objects.equals(this.boostId, boostActivationRequest.boostId) &&
        Objects.equals(this.activationSource, boostActivationRequest.activationSource) &&
        Objects.equals(this.cost, boostActivationRequest.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(boostId, activationSource, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoostActivationRequest {\n");
    sb.append("    boostId: ").append(toIndentedString(boostId)).append("\n");
    sb.append("    activationSource: ").append(toIndentedString(activationSource)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

