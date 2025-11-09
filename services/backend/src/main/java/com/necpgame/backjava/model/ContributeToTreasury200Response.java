package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ContributeToTreasury200Response
 */

@JsonTypeName("contributeToTreasury_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ContributeToTreasury200Response {

  private @Nullable UUID contributionId;

  private @Nullable Integer newBalance;

  public ContributeToTreasury200Response contributionId(@Nullable UUID contributionId) {
    this.contributionId = contributionId;
    return this;
  }

  /**
   * Get contributionId
   * @return contributionId
   */
  @Valid 
  @Schema(name = "contribution_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contribution_id")
  public @Nullable UUID getContributionId() {
    return contributionId;
  }

  public void setContributionId(@Nullable UUID contributionId) {
    this.contributionId = contributionId;
  }

  public ContributeToTreasury200Response newBalance(@Nullable Integer newBalance) {
    this.newBalance = newBalance;
    return this;
  }

  /**
   * Get newBalance
   * @return newBalance
   */
  
  @Schema(name = "new_balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_balance")
  public @Nullable Integer getNewBalance() {
    return newBalance;
  }

  public void setNewBalance(@Nullable Integer newBalance) {
    this.newBalance = newBalance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContributeToTreasury200Response contributeToTreasury200Response = (ContributeToTreasury200Response) o;
    return Objects.equals(this.contributionId, contributeToTreasury200Response.contributionId) &&
        Objects.equals(this.newBalance, contributeToTreasury200Response.newBalance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contributionId, newBalance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContributeToTreasury200Response {\n");
    sb.append("    contributionId: ").append(toIndentedString(contributionId)).append("\n");
    sb.append("    newBalance: ").append(toIndentedString(newBalance)).append("\n");
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

