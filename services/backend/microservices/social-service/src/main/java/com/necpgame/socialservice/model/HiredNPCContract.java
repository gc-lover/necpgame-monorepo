package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * HiredNPCContract
 */

@JsonTypeName("HiredNPC_contract")

public class HiredNPCContract {

  private @Nullable Integer durationDays;

  private @Nullable Integer salaryPerDay;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endDate;

  public HiredNPCContract durationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
    return this;
  }

  /**
   * Get durationDays
   * @return durationDays
   */
  
  @Schema(name = "duration_days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public @Nullable Integer getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
  }

  public HiredNPCContract salaryPerDay(@Nullable Integer salaryPerDay) {
    this.salaryPerDay = salaryPerDay;
    return this;
  }

  /**
   * Get salaryPerDay
   * @return salaryPerDay
   */
  
  @Schema(name = "salary_per_day", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("salary_per_day")
  public @Nullable Integer getSalaryPerDay() {
    return salaryPerDay;
  }

  public void setSalaryPerDay(@Nullable Integer salaryPerDay) {
    this.salaryPerDay = salaryPerDay;
  }

  public HiredNPCContract startDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @Valid 
  @Schema(name = "start_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start_date")
  public @Nullable OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public HiredNPCContract endDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @Valid 
  @Schema(name = "end_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end_date")
  public @Nullable OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HiredNPCContract hiredNPCContract = (HiredNPCContract) o;
    return Objects.equals(this.durationDays, hiredNPCContract.durationDays) &&
        Objects.equals(this.salaryPerDay, hiredNPCContract.salaryPerDay) &&
        Objects.equals(this.startDate, hiredNPCContract.startDate) &&
        Objects.equals(this.endDate, hiredNPCContract.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(durationDays, salaryPerDay, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HiredNPCContract {\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
    sb.append("    salaryPerDay: ").append(toIndentedString(salaryPerDay)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

