package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetMentorshipWorldImpact200Response
 */

@JsonTypeName("getMentorshipWorldImpact_200_response")

public class GetMentorshipWorldImpact200Response {

  private @Nullable Integer totalActiveMentorships;

  private @Nullable Integer totalGraduated;

  private @Nullable Object skillDistribution;

  @Valid
  private List<Object> legendaryMentors = new ArrayList<>();

  public GetMentorshipWorldImpact200Response totalActiveMentorships(@Nullable Integer totalActiveMentorships) {
    this.totalActiveMentorships = totalActiveMentorships;
    return this;
  }

  /**
   * Get totalActiveMentorships
   * @return totalActiveMentorships
   */
  
  @Schema(name = "total_active_mentorships", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_active_mentorships")
  public @Nullable Integer getTotalActiveMentorships() {
    return totalActiveMentorships;
  }

  public void setTotalActiveMentorships(@Nullable Integer totalActiveMentorships) {
    this.totalActiveMentorships = totalActiveMentorships;
  }

  public GetMentorshipWorldImpact200Response totalGraduated(@Nullable Integer totalGraduated) {
    this.totalGraduated = totalGraduated;
    return this;
  }

  /**
   * Get totalGraduated
   * @return totalGraduated
   */
  
  @Schema(name = "total_graduated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_graduated")
  public @Nullable Integer getTotalGraduated() {
    return totalGraduated;
  }

  public void setTotalGraduated(@Nullable Integer totalGraduated) {
    this.totalGraduated = totalGraduated;
  }

  public GetMentorshipWorldImpact200Response skillDistribution(@Nullable Object skillDistribution) {
    this.skillDistribution = skillDistribution;
    return this;
  }

  /**
   * Распределение навыков в мире
   * @return skillDistribution
   */
  
  @Schema(name = "skill_distribution", description = "Распределение навыков в мире", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_distribution")
  public @Nullable Object getSkillDistribution() {
    return skillDistribution;
  }

  public void setSkillDistribution(@Nullable Object skillDistribution) {
    this.skillDistribution = skillDistribution;
  }

  public GetMentorshipWorldImpact200Response legendaryMentors(List<Object> legendaryMentors) {
    this.legendaryMentors = legendaryMentors;
    return this;
  }

  public GetMentorshipWorldImpact200Response addLegendaryMentorsItem(Object legendaryMentorsItem) {
    if (this.legendaryMentors == null) {
      this.legendaryMentors = new ArrayList<>();
    }
    this.legendaryMentors.add(legendaryMentorsItem);
    return this;
  }

  /**
   * Get legendaryMentors
   * @return legendaryMentors
   */
  
  @Schema(name = "legendary_mentors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("legendary_mentors")
  public List<Object> getLegendaryMentors() {
    return legendaryMentors;
  }

  public void setLegendaryMentors(List<Object> legendaryMentors) {
    this.legendaryMentors = legendaryMentors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMentorshipWorldImpact200Response getMentorshipWorldImpact200Response = (GetMentorshipWorldImpact200Response) o;
    return Objects.equals(this.totalActiveMentorships, getMentorshipWorldImpact200Response.totalActiveMentorships) &&
        Objects.equals(this.totalGraduated, getMentorshipWorldImpact200Response.totalGraduated) &&
        Objects.equals(this.skillDistribution, getMentorshipWorldImpact200Response.skillDistribution) &&
        Objects.equals(this.legendaryMentors, getMentorshipWorldImpact200Response.legendaryMentors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalActiveMentorships, totalGraduated, skillDistribution, legendaryMentors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMentorshipWorldImpact200Response {\n");
    sb.append("    totalActiveMentorships: ").append(toIndentedString(totalActiveMentorships)).append("\n");
    sb.append("    totalGraduated: ").append(toIndentedString(totalGraduated)).append("\n");
    sb.append("    skillDistribution: ").append(toIndentedString(skillDistribution)).append("\n");
    sb.append("    legendaryMentors: ").append(toIndentedString(legendaryMentors)).append("\n");
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

