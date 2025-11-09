package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.RiskIncident;
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
 * GetPlayerRiskProfile200Response
 */

@JsonTypeName("getPlayerRiskProfile_200_response")

public class GetPlayerRiskProfile200Response {

  private UUID playerId;

  private Float averageScore;

  /**
   * Gets or Sets riskGrade
   */
  public enum RiskGradeEnum {
    LOW("low"),
    
    MODERATE("moderate"),
    
    ELEVATED("elevated"),
    
    HIGH("high"),
    
    SEVERE("severe");

    private final String value;

    RiskGradeEnum(String value) {
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
    public static RiskGradeEnum fromValue(String value) {
      for (RiskGradeEnum b : RiskGradeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RiskGradeEnum riskGrade;

  @Valid
  private List<@Valid RiskIncident> incidents = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastEvaluatedAt;

  public GetPlayerRiskProfile200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetPlayerRiskProfile200Response(UUID playerId, Float averageScore, OffsetDateTime lastEvaluatedAt) {
    this.playerId = playerId;
    this.averageScore = averageScore;
    this.lastEvaluatedAt = lastEvaluatedAt;
  }

  public GetPlayerRiskProfile200Response playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public GetPlayerRiskProfile200Response averageScore(Float averageScore) {
    this.averageScore = averageScore;
    return this;
  }

  /**
   * Get averageScore
   * @return averageScore
   */
  @NotNull 
  @Schema(name = "averageScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("averageScore")
  public Float getAverageScore() {
    return averageScore;
  }

  public void setAverageScore(Float averageScore) {
    this.averageScore = averageScore;
  }

  public GetPlayerRiskProfile200Response riskGrade(@Nullable RiskGradeEnum riskGrade) {
    this.riskGrade = riskGrade;
    return this;
  }

  /**
   * Get riskGrade
   * @return riskGrade
   */
  
  @Schema(name = "riskGrade", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("riskGrade")
  public @Nullable RiskGradeEnum getRiskGrade() {
    return riskGrade;
  }

  public void setRiskGrade(@Nullable RiskGradeEnum riskGrade) {
    this.riskGrade = riskGrade;
  }

  public GetPlayerRiskProfile200Response incidents(List<@Valid RiskIncident> incidents) {
    this.incidents = incidents;
    return this;
  }

  public GetPlayerRiskProfile200Response addIncidentsItem(RiskIncident incidentsItem) {
    if (this.incidents == null) {
      this.incidents = new ArrayList<>();
    }
    this.incidents.add(incidentsItem);
    return this;
  }

  /**
   * Get incidents
   * @return incidents
   */
  @Valid 
  @Schema(name = "incidents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incidents")
  public List<@Valid RiskIncident> getIncidents() {
    return incidents;
  }

  public void setIncidents(List<@Valid RiskIncident> incidents) {
    this.incidents = incidents;
  }

  public GetPlayerRiskProfile200Response lastEvaluatedAt(OffsetDateTime lastEvaluatedAt) {
    this.lastEvaluatedAt = lastEvaluatedAt;
    return this;
  }

  /**
   * Get lastEvaluatedAt
   * @return lastEvaluatedAt
   */
  @NotNull @Valid 
  @Schema(name = "lastEvaluatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastEvaluatedAt")
  public OffsetDateTime getLastEvaluatedAt() {
    return lastEvaluatedAt;
  }

  public void setLastEvaluatedAt(OffsetDateTime lastEvaluatedAt) {
    this.lastEvaluatedAt = lastEvaluatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPlayerRiskProfile200Response getPlayerRiskProfile200Response = (GetPlayerRiskProfile200Response) o;
    return Objects.equals(this.playerId, getPlayerRiskProfile200Response.playerId) &&
        Objects.equals(this.averageScore, getPlayerRiskProfile200Response.averageScore) &&
        Objects.equals(this.riskGrade, getPlayerRiskProfile200Response.riskGrade) &&
        Objects.equals(this.incidents, getPlayerRiskProfile200Response.incidents) &&
        Objects.equals(this.lastEvaluatedAt, getPlayerRiskProfile200Response.lastEvaluatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, averageScore, riskGrade, incidents, lastEvaluatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPlayerRiskProfile200Response {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    averageScore: ").append(toIndentedString(averageScore)).append("\n");
    sb.append("    riskGrade: ").append(toIndentedString(riskGrade)).append("\n");
    sb.append("    incidents: ").append(toIndentedString(incidents)).append("\n");
    sb.append("    lastEvaluatedAt: ").append(toIndentedString(lastEvaluatedAt)).append("\n");
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

