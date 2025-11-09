package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.LootGenerationError;
import com.necpgame.backjava.model.LootItem;
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
 * DistributionResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DistributionResult {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    QUEUED("QUEUED"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @Valid
  private List<@Valid LootItem> grantedItems = new ArrayList<>();

  private @Nullable String grantReference;

  @Valid
  private List<@Valid LootGenerationError> errors = new ArrayList<>();

  public DistributionResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DistributionResult(StatusEnum status) {
    this.status = status;
  }

  public DistributionResult status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public DistributionResult grantedItems(List<@Valid LootItem> grantedItems) {
    this.grantedItems = grantedItems;
    return this;
  }

  public DistributionResult addGrantedItemsItem(LootItem grantedItemsItem) {
    if (this.grantedItems == null) {
      this.grantedItems = new ArrayList<>();
    }
    this.grantedItems.add(grantedItemsItem);
    return this;
  }

  /**
   * Get grantedItems
   * @return grantedItems
   */
  @Valid 
  @Schema(name = "grantedItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grantedItems")
  public List<@Valid LootItem> getGrantedItems() {
    return grantedItems;
  }

  public void setGrantedItems(List<@Valid LootItem> grantedItems) {
    this.grantedItems = grantedItems;
  }

  public DistributionResult grantReference(@Nullable String grantReference) {
    this.grantReference = grantReference;
    return this;
  }

  /**
   * Get grantReference
   * @return grantReference
   */
  
  @Schema(name = "grantReference", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grantReference")
  public @Nullable String getGrantReference() {
    return grantReference;
  }

  public void setGrantReference(@Nullable String grantReference) {
    this.grantReference = grantReference;
  }

  public DistributionResult errors(List<@Valid LootGenerationError> errors) {
    this.errors = errors;
    return this;
  }

  public DistributionResult addErrorsItem(LootGenerationError errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Get errors
   * @return errors
   */
  @Valid 
  @Schema(name = "errors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors")
  public List<@Valid LootGenerationError> getErrors() {
    return errors;
  }

  public void setErrors(List<@Valid LootGenerationError> errors) {
    this.errors = errors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DistributionResult distributionResult = (DistributionResult) o;
    return Objects.equals(this.status, distributionResult.status) &&
        Objects.equals(this.grantedItems, distributionResult.grantedItems) &&
        Objects.equals(this.grantReference, distributionResult.grantReference) &&
        Objects.equals(this.errors, distributionResult.errors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, grantedItems, grantReference, errors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistributionResult {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    grantedItems: ").append(toIndentedString(grantedItems)).append("\n");
    sb.append("    grantReference: ").append(toIndentedString(grantReference)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
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

