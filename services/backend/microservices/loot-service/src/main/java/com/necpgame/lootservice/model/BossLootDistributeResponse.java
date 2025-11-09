package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.LootAssignment;
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
 * BossLootDistributeResponse
 */


public class BossLootDistributeResponse {

  @Valid
  private List<@Valid LootAssignment> assigned = new ArrayList<>();

  private @Nullable Boolean guaranteedAwarded;

  public BossLootDistributeResponse assigned(List<@Valid LootAssignment> assigned) {
    this.assigned = assigned;
    return this;
  }

  public BossLootDistributeResponse addAssignedItem(LootAssignment assignedItem) {
    if (this.assigned == null) {
      this.assigned = new ArrayList<>();
    }
    this.assigned.add(assignedItem);
    return this;
  }

  /**
   * Get assigned
   * @return assigned
   */
  @Valid 
  @Schema(name = "assigned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assigned")
  public List<@Valid LootAssignment> getAssigned() {
    return assigned;
  }

  public void setAssigned(List<@Valid LootAssignment> assigned) {
    this.assigned = assigned;
  }

  public BossLootDistributeResponse guaranteedAwarded(@Nullable Boolean guaranteedAwarded) {
    this.guaranteedAwarded = guaranteedAwarded;
    return this;
  }

  /**
   * Get guaranteedAwarded
   * @return guaranteedAwarded
   */
  
  @Schema(name = "guaranteedAwarded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteedAwarded")
  public @Nullable Boolean getGuaranteedAwarded() {
    return guaranteedAwarded;
  }

  public void setGuaranteedAwarded(@Nullable Boolean guaranteedAwarded) {
    this.guaranteedAwarded = guaranteedAwarded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BossLootDistributeResponse bossLootDistributeResponse = (BossLootDistributeResponse) o;
    return Objects.equals(this.assigned, bossLootDistributeResponse.assigned) &&
        Objects.equals(this.guaranteedAwarded, bossLootDistributeResponse.guaranteedAwarded);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assigned, guaranteedAwarded);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BossLootDistributeResponse {\n");
    sb.append("    assigned: ").append(toIndentedString(assigned)).append("\n");
    sb.append("    guaranteedAwarded: ").append(toIndentedString(guaranteedAwarded)).append("\n");
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

