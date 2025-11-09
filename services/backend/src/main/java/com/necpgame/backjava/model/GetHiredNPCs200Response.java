package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.HiredNPC;
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
 * GetHiredNPCs200Response
 */

@JsonTypeName("getHiredNPCs_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetHiredNPCs200Response {

  @Valid
  private List<@Valid HiredNPC> hiredNpcs = new ArrayList<>();

  private @Nullable Integer totalCostPerDay;

  private @Nullable Integer slotsUsed;

  private @Nullable Integer maxSlots;

  public GetHiredNPCs200Response hiredNpcs(List<@Valid HiredNPC> hiredNpcs) {
    this.hiredNpcs = hiredNpcs;
    return this;
  }

  public GetHiredNPCs200Response addHiredNpcsItem(HiredNPC hiredNpcsItem) {
    if (this.hiredNpcs == null) {
      this.hiredNpcs = new ArrayList<>();
    }
    this.hiredNpcs.add(hiredNpcsItem);
    return this;
  }

  /**
   * Get hiredNpcs
   * @return hiredNpcs
   */
  @Valid 
  @Schema(name = "hired_npcs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hired_npcs")
  public List<@Valid HiredNPC> getHiredNpcs() {
    return hiredNpcs;
  }

  public void setHiredNpcs(List<@Valid HiredNPC> hiredNpcs) {
    this.hiredNpcs = hiredNpcs;
  }

  public GetHiredNPCs200Response totalCostPerDay(@Nullable Integer totalCostPerDay) {
    this.totalCostPerDay = totalCostPerDay;
    return this;
  }

  /**
   * Get totalCostPerDay
   * @return totalCostPerDay
   */
  
  @Schema(name = "total_cost_per_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_cost_per_day")
  public @Nullable Integer getTotalCostPerDay() {
    return totalCostPerDay;
  }

  public void setTotalCostPerDay(@Nullable Integer totalCostPerDay) {
    this.totalCostPerDay = totalCostPerDay;
  }

  public GetHiredNPCs200Response slotsUsed(@Nullable Integer slotsUsed) {
    this.slotsUsed = slotsUsed;
    return this;
  }

  /**
   * Get slotsUsed
   * @return slotsUsed
   */
  
  @Schema(name = "slots_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots_used")
  public @Nullable Integer getSlotsUsed() {
    return slotsUsed;
  }

  public void setSlotsUsed(@Nullable Integer slotsUsed) {
    this.slotsUsed = slotsUsed;
  }

  public GetHiredNPCs200Response maxSlots(@Nullable Integer maxSlots) {
    this.maxSlots = maxSlots;
    return this;
  }

  /**
   * Get maxSlots
   * @return maxSlots
   */
  
  @Schema(name = "max_slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_slots")
  public @Nullable Integer getMaxSlots() {
    return maxSlots;
  }

  public void setMaxSlots(@Nullable Integer maxSlots) {
    this.maxSlots = maxSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetHiredNPCs200Response getHiredNPCs200Response = (GetHiredNPCs200Response) o;
    return Objects.equals(this.hiredNpcs, getHiredNPCs200Response.hiredNpcs) &&
        Objects.equals(this.totalCostPerDay, getHiredNPCs200Response.totalCostPerDay) &&
        Objects.equals(this.slotsUsed, getHiredNPCs200Response.slotsUsed) &&
        Objects.equals(this.maxSlots, getHiredNPCs200Response.maxSlots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hiredNpcs, totalCostPerDay, slotsUsed, maxSlots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetHiredNPCs200Response {\n");
    sb.append("    hiredNpcs: ").append(toIndentedString(hiredNpcs)).append("\n");
    sb.append("    totalCostPerDay: ").append(toIndentedString(totalCostPerDay)).append("\n");
    sb.append("    slotsUsed: ").append(toIndentedString(slotsUsed)).append("\n");
    sb.append("    maxSlots: ").append(toIndentedString(maxSlots)).append("\n");
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

