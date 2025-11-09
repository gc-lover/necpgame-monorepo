package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ContractTermsPenalties
 */

@JsonTypeName("ContractTerms_penalties")

public class ContractTermsPenalties {

  private @Nullable Integer lateCompletion;

  private @Nullable Integer nonCompletion;

  public ContractTermsPenalties lateCompletion(@Nullable Integer lateCompletion) {
    this.lateCompletion = lateCompletion;
    return this;
  }

  /**
   * Get lateCompletion
   * @return lateCompletion
   */
  
  @Schema(name = "late_completion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("late_completion")
  public @Nullable Integer getLateCompletion() {
    return lateCompletion;
  }

  public void setLateCompletion(@Nullable Integer lateCompletion) {
    this.lateCompletion = lateCompletion;
  }

  public ContractTermsPenalties nonCompletion(@Nullable Integer nonCompletion) {
    this.nonCompletion = nonCompletion;
    return this;
  }

  /**
   * Get nonCompletion
   * @return nonCompletion
   */
  
  @Schema(name = "non_completion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("non_completion")
  public @Nullable Integer getNonCompletion() {
    return nonCompletion;
  }

  public void setNonCompletion(@Nullable Integer nonCompletion) {
    this.nonCompletion = nonCompletion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractTermsPenalties contractTermsPenalties = (ContractTermsPenalties) o;
    return Objects.equals(this.lateCompletion, contractTermsPenalties.lateCompletion) &&
        Objects.equals(this.nonCompletion, contractTermsPenalties.nonCompletion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lateCompletion, nonCompletion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractTermsPenalties {\n");
    sb.append("    lateCompletion: ").append(toIndentedString(lateCompletion)).append("\n");
    sb.append("    nonCompletion: ").append(toIndentedString(nonCompletion)).append("\n");
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

