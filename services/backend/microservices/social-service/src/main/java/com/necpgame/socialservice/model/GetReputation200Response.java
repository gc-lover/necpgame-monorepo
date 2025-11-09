package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.GetReputation200ResponseReputationsInner;
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
 * GetReputation200Response
 */

@JsonTypeName("getReputation_200_response")

public class GetReputation200Response {

  @Valid
  private List<@Valid GetReputation200ResponseReputationsInner> reputations = new ArrayList<>();

  public GetReputation200Response reputations(List<@Valid GetReputation200ResponseReputationsInner> reputations) {
    this.reputations = reputations;
    return this;
  }

  public GetReputation200Response addReputationsItem(GetReputation200ResponseReputationsInner reputationsItem) {
    if (this.reputations == null) {
      this.reputations = new ArrayList<>();
    }
    this.reputations.add(reputationsItem);
    return this;
  }

  /**
   * Get reputations
   * @return reputations
   */
  @Valid 
  @Schema(name = "reputations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputations")
  public List<@Valid GetReputation200ResponseReputationsInner> getReputations() {
    return reputations;
  }

  public void setReputations(List<@Valid GetReputation200ResponseReputationsInner> reputations) {
    this.reputations = reputations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetReputation200Response getReputation200Response = (GetReputation200Response) o;
    return Objects.equals(this.reputations, getReputation200Response.reputations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetReputation200Response {\n");
    sb.append("    reputations: ").append(toIndentedString(reputations)).append("\n");
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

