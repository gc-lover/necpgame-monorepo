package com.necpgame.gameplayservice.model;

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
 * ApartmentUpgradeRequest
 */


public class ApartmentUpgradeRequest {

  /**
   * Gets or Sets upgradeType
   */
  public enum UpgradeTypeEnum {
    ROOM("ROOM"),
    
    STORAGE("STORAGE"),
    
    FUNCTIONAL("FUNCTIONAL");

    private final String value;

    UpgradeTypeEnum(String value) {
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
    public static UpgradeTypeEnum fromValue(String value) {
      for (UpgradeTypeEnum b : UpgradeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private UpgradeTypeEnum upgradeType;

  private @Nullable Integer targetLevel;

  private @Nullable String paymentMethod;

  public ApartmentUpgradeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApartmentUpgradeRequest(UpgradeTypeEnum upgradeType) {
    this.upgradeType = upgradeType;
  }

  public ApartmentUpgradeRequest upgradeType(UpgradeTypeEnum upgradeType) {
    this.upgradeType = upgradeType;
    return this;
  }

  /**
   * Get upgradeType
   * @return upgradeType
   */
  @NotNull 
  @Schema(name = "upgradeType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("upgradeType")
  public UpgradeTypeEnum getUpgradeType() {
    return upgradeType;
  }

  public void setUpgradeType(UpgradeTypeEnum upgradeType) {
    this.upgradeType = upgradeType;
  }

  public ApartmentUpgradeRequest targetLevel(@Nullable Integer targetLevel) {
    this.targetLevel = targetLevel;
    return this;
  }

  /**
   * Get targetLevel
   * minimum: 1
   * @return targetLevel
   */
  @Min(value = 1) 
  @Schema(name = "targetLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetLevel")
  public @Nullable Integer getTargetLevel() {
    return targetLevel;
  }

  public void setTargetLevel(@Nullable Integer targetLevel) {
    this.targetLevel = targetLevel;
  }

  public ApartmentUpgradeRequest paymentMethod(@Nullable String paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  /**
   * Get paymentMethod
   * @return paymentMethod
   */
  
  @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paymentMethod")
  public @Nullable String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(@Nullable String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentUpgradeRequest apartmentUpgradeRequest = (ApartmentUpgradeRequest) o;
    return Objects.equals(this.upgradeType, apartmentUpgradeRequest.upgradeType) &&
        Objects.equals(this.targetLevel, apartmentUpgradeRequest.targetLevel) &&
        Objects.equals(this.paymentMethod, apartmentUpgradeRequest.paymentMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(upgradeType, targetLevel, paymentMethod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentUpgradeRequest {\n");
    sb.append("    upgradeType: ").append(toIndentedString(upgradeType)).append("\n");
    sb.append("    targetLevel: ").append(toIndentedString(targetLevel)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
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

