package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.DistributeProfits200ResponseDistributionsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DistributeProfitsRequest
 */


public class DistributeProfitsRequest {

  /**
   * Gets or Sets distributionType
   */
  public enum DistributionTypeEnum {
    EQUAL("EQUAL"),
    
    BY_CONTRIBUTION("BY_CONTRIBUTION"),
    
    BY_ROLE("BY_ROLE"),
    
    CUSTOM("CUSTOM");

    private final String value;

    DistributionTypeEnum(String value) {
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
    public static DistributionTypeEnum fromValue(String value) {
      for (DistributionTypeEnum b : DistributionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DistributionTypeEnum distributionType;

  private @Nullable Integer totalAmount;

  @Valid
  private JsonNullable<List<@Valid DistributeProfits200ResponseDistributionsInner>> customDistributions = JsonNullable.<List<@Valid DistributeProfits200ResponseDistributionsInner>>undefined();

  public DistributeProfitsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DistributeProfitsRequest(DistributionTypeEnum distributionType) {
    this.distributionType = distributionType;
  }

  public DistributeProfitsRequest distributionType(DistributionTypeEnum distributionType) {
    this.distributionType = distributionType;
    return this;
  }

  /**
   * Get distributionType
   * @return distributionType
   */
  @NotNull 
  @Schema(name = "distribution_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("distribution_type")
  public DistributionTypeEnum getDistributionType() {
    return distributionType;
  }

  public void setDistributionType(DistributionTypeEnum distributionType) {
    this.distributionType = distributionType;
  }

  public DistributeProfitsRequest totalAmount(@Nullable Integer totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  /**
   * Get totalAmount
   * @return totalAmount
   */
  
  @Schema(name = "total_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_amount")
  public @Nullable Integer getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(@Nullable Integer totalAmount) {
    this.totalAmount = totalAmount;
  }

  public DistributeProfitsRequest customDistributions(List<@Valid DistributeProfits200ResponseDistributionsInner> customDistributions) {
    this.customDistributions = JsonNullable.of(customDistributions);
    return this;
  }

  public DistributeProfitsRequest addCustomDistributionsItem(DistributeProfits200ResponseDistributionsInner customDistributionsItem) {
    if (this.customDistributions == null || !this.customDistributions.isPresent()) {
      this.customDistributions = JsonNullable.of(new ArrayList<>());
    }
    this.customDistributions.get().add(customDistributionsItem);
    return this;
  }

  /**
   * Get customDistributions
   * @return customDistributions
   */
  @Valid 
  @Schema(name = "custom_distributions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("custom_distributions")
  public JsonNullable<List<@Valid DistributeProfits200ResponseDistributionsInner>> getCustomDistributions() {
    return customDistributions;
  }

  public void setCustomDistributions(JsonNullable<List<@Valid DistributeProfits200ResponseDistributionsInner>> customDistributions) {
    this.customDistributions = customDistributions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistributeProfitsRequest distributeProfitsRequest = (DistributeProfitsRequest) o;
    return Objects.equals(this.distributionType, distributeProfitsRequest.distributionType) &&
        Objects.equals(this.totalAmount, distributeProfitsRequest.totalAmount) &&
        equalsNullable(this.customDistributions, distributeProfitsRequest.customDistributions);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(distributionType, totalAmount, hashCodeNullable(customDistributions));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistributeProfitsRequest {\n");
    sb.append("    distributionType: ").append(toIndentedString(distributionType)).append("\n");
    sb.append("    totalAmount: ").append(toIndentedString(totalAmount)).append("\n");
    sb.append("    customDistributions: ").append(toIndentedString(customDistributions)).append("\n");
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

