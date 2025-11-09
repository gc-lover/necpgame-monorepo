package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.FeatureFlag;
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
 * GetFeatureFlags200Response
 */

@JsonTypeName("getFeatureFlags_200_response")

public class GetFeatureFlags200Response {

  @Valid
  private List<@Valid FeatureFlag> flags = new ArrayList<>();

  public GetFeatureFlags200Response flags(List<@Valid FeatureFlag> flags) {
    this.flags = flags;
    return this;
  }

  public GetFeatureFlags200Response addFlagsItem(FeatureFlag flagsItem) {
    if (this.flags == null) {
      this.flags = new ArrayList<>();
    }
    this.flags.add(flagsItem);
    return this;
  }

  /**
   * Get flags
   * @return flags
   */
  @Valid 
  @Schema(name = "flags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flags")
  public List<@Valid FeatureFlag> getFlags() {
    return flags;
  }

  public void setFlags(List<@Valid FeatureFlag> flags) {
    this.flags = flags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFeatureFlags200Response getFeatureFlags200Response = (GetFeatureFlags200Response) o;
    return Objects.equals(this.flags, getFeatureFlags200Response.flags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFeatureFlags200Response {\n");
    sb.append("    flags: ").append(toIndentedString(flags)).append("\n");
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

