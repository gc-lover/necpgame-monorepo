package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.PopStatus;
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
 * GetCdnStatus200Response
 */

@JsonTypeName("getCdnStatus_200_response")

public class GetCdnStatus200Response {

  @Valid
  private List<@Valid PopStatus> pops = new ArrayList<>();

  /**
   * Gets or Sets overallStatus
   */
  public enum OverallStatusEnum {
    OPERATIONAL("operational"),
    
    DEGRADED("degraded"),
    
    DOWN("down");

    private final String value;

    OverallStatusEnum(String value) {
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
    public static OverallStatusEnum fromValue(String value) {
      for (OverallStatusEnum b : OverallStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OverallStatusEnum overallStatus;

  public GetCdnStatus200Response pops(List<@Valid PopStatus> pops) {
    this.pops = pops;
    return this;
  }

  public GetCdnStatus200Response addPopsItem(PopStatus popsItem) {
    if (this.pops == null) {
      this.pops = new ArrayList<>();
    }
    this.pops.add(popsItem);
    return this;
  }

  /**
   * Get pops
   * @return pops
   */
  @Valid 
  @Schema(name = "pops", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pops")
  public List<@Valid PopStatus> getPops() {
    return pops;
  }

  public void setPops(List<@Valid PopStatus> pops) {
    this.pops = pops;
  }

  public GetCdnStatus200Response overallStatus(@Nullable OverallStatusEnum overallStatus) {
    this.overallStatus = overallStatus;
    return this;
  }

  /**
   * Get overallStatus
   * @return overallStatus
   */
  
  @Schema(name = "overall_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overall_status")
  public @Nullable OverallStatusEnum getOverallStatus() {
    return overallStatus;
  }

  public void setOverallStatus(@Nullable OverallStatusEnum overallStatus) {
    this.overallStatus = overallStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCdnStatus200Response getCdnStatus200Response = (GetCdnStatus200Response) o;
    return Objects.equals(this.pops, getCdnStatus200Response.pops) &&
        Objects.equals(this.overallStatus, getCdnStatus200Response.overallStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pops, overallStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCdnStatus200Response {\n");
    sb.append("    pops: ").append(toIndentedString(pops)).append("\n");
    sb.append("    overallStatus: ").append(toIndentedString(overallStatus)).append("\n");
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

