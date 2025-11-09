package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.InsuranceTier;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InsuranceTierList
 */


public class InsuranceTierList {

  @Valid
  private List<@Valid InsuranceTier> data = new ArrayList<>();

  private @Nullable String currency;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public InsuranceTierList() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InsuranceTierList(List<@Valid InsuranceTier> data) {
    this.data = data;
  }

  public InsuranceTierList data(List<@Valid InsuranceTier> data) {
    this.data = data;
    return this;
  }

  public InsuranceTierList addDataItem(InsuranceTier dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Перечень страховых планов для UI.
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", description = "Перечень страховых планов для UI.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid InsuranceTier> getData() {
    return data;
  }

  public void setData(List<@Valid InsuranceTier> data) {
    this.data = data;
  }

  public InsuranceTierList currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Pattern(regexp = "^[A-Z]{3}$") 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public InsuranceTierList updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsuranceTierList insuranceTierList = (InsuranceTierList) o;
    return Objects.equals(this.data, insuranceTierList.data) &&
        Objects.equals(this.currency, insuranceTierList.currency) &&
        Objects.equals(this.updatedAt, insuranceTierList.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, currency, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsuranceTierList {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

