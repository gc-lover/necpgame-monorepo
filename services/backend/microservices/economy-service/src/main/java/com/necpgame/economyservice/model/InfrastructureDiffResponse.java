package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.InfrastructureDiff;
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
 * InfrastructureDiffResponse
 */


public class InfrastructureDiffResponse {

  private UUID districtId;

  private InfrastructureDiff diff;

  public InfrastructureDiffResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InfrastructureDiffResponse(UUID districtId, InfrastructureDiff diff) {
    this.districtId = districtId;
    this.diff = diff;
  }

  public InfrastructureDiffResponse districtId(UUID districtId) {
    this.districtId = districtId;
    return this;
  }

  /**
   * Get districtId
   * @return districtId
   */
  @NotNull @Valid 
  @Schema(name = "districtId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("districtId")
  public UUID getDistrictId() {
    return districtId;
  }

  public void setDistrictId(UUID districtId) {
    this.districtId = districtId;
  }

  public InfrastructureDiffResponse diff(InfrastructureDiff diff) {
    this.diff = diff;
    return this;
  }

  /**
   * Get diff
   * @return diff
   */
  @NotNull @Valid 
  @Schema(name = "diff", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("diff")
  public InfrastructureDiff getDiff() {
    return diff;
  }

  public void setDiff(InfrastructureDiff diff) {
    this.diff = diff;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureDiffResponse infrastructureDiffResponse = (InfrastructureDiffResponse) o;
    return Objects.equals(this.districtId, infrastructureDiffResponse.districtId) &&
        Objects.equals(this.diff, infrastructureDiffResponse.diff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(districtId, diff);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureDiffResponse {\n");
    sb.append("    districtId: ").append(toIndentedString(districtId)).append("\n");
    sb.append("    diff: ").append(toIndentedString(diff)).append("\n");
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

