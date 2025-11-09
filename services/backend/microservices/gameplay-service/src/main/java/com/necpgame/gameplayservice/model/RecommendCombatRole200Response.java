package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RecommendCombatRole200Response
 */

@JsonTypeName("recommendCombatRole_200_response")

public class RecommendCombatRole200Response {

  private @Nullable String recommendedRole;

  private @Nullable BigDecimal matchPercentage;

  public RecommendCombatRole200Response recommendedRole(@Nullable String recommendedRole) {
    this.recommendedRole = recommendedRole;
    return this;
  }

  /**
   * Get recommendedRole
   * @return recommendedRole
   */
  
  @Schema(name = "recommended_role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommended_role")
  public @Nullable String getRecommendedRole() {
    return recommendedRole;
  }

  public void setRecommendedRole(@Nullable String recommendedRole) {
    this.recommendedRole = recommendedRole;
  }

  public RecommendCombatRole200Response matchPercentage(@Nullable BigDecimal matchPercentage) {
    this.matchPercentage = matchPercentage;
    return this;
  }

  /**
   * Get matchPercentage
   * @return matchPercentage
   */
  @Valid 
  @Schema(name = "match_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("match_percentage")
  public @Nullable BigDecimal getMatchPercentage() {
    return matchPercentage;
  }

  public void setMatchPercentage(@Nullable BigDecimal matchPercentage) {
    this.matchPercentage = matchPercentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RecommendCombatRole200Response recommendCombatRole200Response = (RecommendCombatRole200Response) o;
    return Objects.equals(this.recommendedRole, recommendCombatRole200Response.recommendedRole) &&
        Objects.equals(this.matchPercentage, recommendCombatRole200Response.matchPercentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recommendedRole, matchPercentage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RecommendCombatRole200Response {\n");
    sb.append("    recommendedRole: ").append(toIndentedString(recommendedRole)).append("\n");
    sb.append("    matchPercentage: ").append(toIndentedString(matchPercentage)).append("\n");
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

