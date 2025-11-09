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
 * ContractTermsBonuses
 */

@JsonTypeName("ContractTerms_bonuses")

public class ContractTermsBonuses {

  private @Nullable Integer earlyCompletion;

  private @Nullable Integer qualityBonus;

  public ContractTermsBonuses earlyCompletion(@Nullable Integer earlyCompletion) {
    this.earlyCompletion = earlyCompletion;
    return this;
  }

  /**
   * Get earlyCompletion
   * @return earlyCompletion
   */
  
  @Schema(name = "early_completion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("early_completion")
  public @Nullable Integer getEarlyCompletion() {
    return earlyCompletion;
  }

  public void setEarlyCompletion(@Nullable Integer earlyCompletion) {
    this.earlyCompletion = earlyCompletion;
  }

  public ContractTermsBonuses qualityBonus(@Nullable Integer qualityBonus) {
    this.qualityBonus = qualityBonus;
    return this;
  }

  /**
   * Get qualityBonus
   * @return qualityBonus
   */
  
  @Schema(name = "quality_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_bonus")
  public @Nullable Integer getQualityBonus() {
    return qualityBonus;
  }

  public void setQualityBonus(@Nullable Integer qualityBonus) {
    this.qualityBonus = qualityBonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractTermsBonuses contractTermsBonuses = (ContractTermsBonuses) o;
    return Objects.equals(this.earlyCompletion, contractTermsBonuses.earlyCompletion) &&
        Objects.equals(this.qualityBonus, contractTermsBonuses.qualityBonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(earlyCompletion, qualityBonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractTermsBonuses {\n");
    sb.append("    earlyCompletion: ").append(toIndentedString(earlyCompletion)).append("\n");
    sb.append("    qualityBonus: ").append(toIndentedString(qualityBonus)).append("\n");
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

