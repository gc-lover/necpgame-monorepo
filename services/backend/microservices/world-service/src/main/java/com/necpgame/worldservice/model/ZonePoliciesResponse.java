package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.TerritoryRestriction;
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
 * ZonePoliciesResponse
 */


public class ZonePoliciesResponse {

  private String policyVersion;

  @Valid
  private List<@Valid TerritoryRestriction> restrictions = new ArrayList<>();

  public ZonePoliciesResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ZonePoliciesResponse(String policyVersion, List<@Valid TerritoryRestriction> restrictions) {
    this.policyVersion = policyVersion;
    this.restrictions = restrictions;
  }

  public ZonePoliciesResponse policyVersion(String policyVersion) {
    this.policyVersion = policyVersion;
    return this;
  }

  /**
   * Get policyVersion
   * @return policyVersion
   */
  @NotNull 
  @Schema(name = "policyVersion", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("policyVersion")
  public String getPolicyVersion() {
    return policyVersion;
  }

  public void setPolicyVersion(String policyVersion) {
    this.policyVersion = policyVersion;
  }

  public ZonePoliciesResponse restrictions(List<@Valid TerritoryRestriction> restrictions) {
    this.restrictions = restrictions;
    return this;
  }

  public ZonePoliciesResponse addRestrictionsItem(TerritoryRestriction restrictionsItem) {
    if (this.restrictions == null) {
      this.restrictions = new ArrayList<>();
    }
    this.restrictions.add(restrictionsItem);
    return this;
  }

  /**
   * Get restrictions
   * @return restrictions
   */
  @NotNull @Valid 
  @Schema(name = "restrictions", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("restrictions")
  public List<@Valid TerritoryRestriction> getRestrictions() {
    return restrictions;
  }

  public void setRestrictions(List<@Valid TerritoryRestriction> restrictions) {
    this.restrictions = restrictions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZonePoliciesResponse zonePoliciesResponse = (ZonePoliciesResponse) o;
    return Objects.equals(this.policyVersion, zonePoliciesResponse.policyVersion) &&
        Objects.equals(this.restrictions, zonePoliciesResponse.restrictions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policyVersion, restrictions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZonePoliciesResponse {\n");
    sb.append("    policyVersion: ").append(toIndentedString(policyVersion)).append("\n");
    sb.append("    restrictions: ").append(toIndentedString(restrictions)).append("\n");
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

