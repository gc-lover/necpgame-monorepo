package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.CityPopulationProfile;
import com.necpgame.worldservice.model.PopulationRecalcJob;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PopulationSummaryResponse
 */


public class PopulationSummaryResponse {

  private UUID cityId;

  private CityPopulationProfile profile;

  @Valid
  private List<@Valid PopulationRecalcJob> jobs = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public PopulationSummaryResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PopulationSummaryResponse(UUID cityId, CityPopulationProfile profile) {
    this.cityId = cityId;
    this.profile = profile;
  }

  public PopulationSummaryResponse cityId(UUID cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @NotNull @Valid 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cityId")
  public UUID getCityId() {
    return cityId;
  }

  public void setCityId(UUID cityId) {
    this.cityId = cityId;
  }

  public PopulationSummaryResponse profile(CityPopulationProfile profile) {
    this.profile = profile;
    return this;
  }

  /**
   * Get profile
   * @return profile
   */
  @NotNull @Valid 
  @Schema(name = "profile", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("profile")
  public CityPopulationProfile getProfile() {
    return profile;
  }

  public void setProfile(CityPopulationProfile profile) {
    this.profile = profile;
  }

  public PopulationSummaryResponse jobs(List<@Valid PopulationRecalcJob> jobs) {
    this.jobs = jobs;
    return this;
  }

  public PopulationSummaryResponse addJobsItem(PopulationRecalcJob jobsItem) {
    if (this.jobs == null) {
      this.jobs = new ArrayList<>();
    }
    this.jobs.add(jobsItem);
    return this;
  }

  /**
   * Get jobs
   * @return jobs
   */
  @Valid 
  @Schema(name = "jobs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("jobs")
  public List<@Valid PopulationRecalcJob> getJobs() {
    return jobs;
  }

  public void setJobs(List<@Valid PopulationRecalcJob> jobs) {
    this.jobs = jobs;
  }

  public PopulationSummaryResponse updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PopulationSummaryResponse populationSummaryResponse = (PopulationSummaryResponse) o;
    return Objects.equals(this.cityId, populationSummaryResponse.cityId) &&
        Objects.equals(this.profile, populationSummaryResponse.profile) &&
        Objects.equals(this.jobs, populationSummaryResponse.jobs) &&
        Objects.equals(this.updatedAt, populationSummaryResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cityId, profile, jobs, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PopulationSummaryResponse {\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
    sb.append("    jobs: ").append(toIndentedString(jobs)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

