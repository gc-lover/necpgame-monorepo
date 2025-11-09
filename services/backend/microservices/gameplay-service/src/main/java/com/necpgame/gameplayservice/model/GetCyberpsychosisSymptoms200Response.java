package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.CyberpsychosisSymptom;
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
 * GetCyberpsychosisSymptoms200Response
 */

@JsonTypeName("getCyberpsychosisSymptoms_200_response")

public class GetCyberpsychosisSymptoms200Response {

  @Valid
  private List<@Valid CyberpsychosisSymptom> symptoms = new ArrayList<>();

  public GetCyberpsychosisSymptoms200Response symptoms(List<@Valid CyberpsychosisSymptom> symptoms) {
    this.symptoms = symptoms;
    return this;
  }

  public GetCyberpsychosisSymptoms200Response addSymptomsItem(CyberpsychosisSymptom symptomsItem) {
    if (this.symptoms == null) {
      this.symptoms = new ArrayList<>();
    }
    this.symptoms.add(symptomsItem);
    return this;
  }

  /**
   * Get symptoms
   * @return symptoms
   */
  @Valid 
  @Schema(name = "symptoms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symptoms")
  public List<@Valid CyberpsychosisSymptom> getSymptoms() {
    return symptoms;
  }

  public void setSymptoms(List<@Valid CyberpsychosisSymptom> symptoms) {
    this.symptoms = symptoms;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCyberpsychosisSymptoms200Response getCyberpsychosisSymptoms200Response = (GetCyberpsychosisSymptoms200Response) o;
    return Objects.equals(this.symptoms, getCyberpsychosisSymptoms200Response.symptoms);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symptoms);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCyberpsychosisSymptoms200Response {\n");
    sb.append("    symptoms: ").append(toIndentedString(symptoms)).append("\n");
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

