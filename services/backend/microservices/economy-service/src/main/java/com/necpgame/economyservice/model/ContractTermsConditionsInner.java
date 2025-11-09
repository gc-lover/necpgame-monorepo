package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ContractTermsConditionsInner
 */

@JsonTypeName("ContractTerms_conditions_inner")

public class ContractTermsConditionsInner {

  private @Nullable String conditionType;

  private @Nullable String description;

  /**
   * Gets or Sets verificationMethod
   */
  public enum VerificationMethodEnum {
    MANUAL("MANUAL"),
    
    AUTO("AUTO"),
    
    ESCROW("ESCROW");

    private final String value;

    VerificationMethodEnum(String value) {
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
    public static VerificationMethodEnum fromValue(String value) {
      for (VerificationMethodEnum b : VerificationMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VerificationMethodEnum verificationMethod;

  public ContractTermsConditionsInner conditionType(@Nullable String conditionType) {
    this.conditionType = conditionType;
    return this;
  }

  /**
   * Get conditionType
   * @return conditionType
   */
  
  @Schema(name = "condition_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("condition_type")
  public @Nullable String getConditionType() {
    return conditionType;
  }

  public void setConditionType(@Nullable String conditionType) {
    this.conditionType = conditionType;
  }

  public ContractTermsConditionsInner description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ContractTermsConditionsInner verificationMethod(@Nullable VerificationMethodEnum verificationMethod) {
    this.verificationMethod = verificationMethod;
    return this;
  }

  /**
   * Get verificationMethod
   * @return verificationMethod
   */
  
  @Schema(name = "verification_method", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("verification_method")
  public @Nullable VerificationMethodEnum getVerificationMethod() {
    return verificationMethod;
  }

  public void setVerificationMethod(@Nullable VerificationMethodEnum verificationMethod) {
    this.verificationMethod = verificationMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractTermsConditionsInner contractTermsConditionsInner = (ContractTermsConditionsInner) o;
    return Objects.equals(this.conditionType, contractTermsConditionsInner.conditionType) &&
        Objects.equals(this.description, contractTermsConditionsInner.description) &&
        Objects.equals(this.verificationMethod, contractTermsConditionsInner.verificationMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conditionType, description, verificationMethod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractTermsConditionsInner {\n");
    sb.append("    conditionType: ").append(toIndentedString(conditionType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    verificationMethod: ").append(toIndentedString(verificationMethod)).append("\n");
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

