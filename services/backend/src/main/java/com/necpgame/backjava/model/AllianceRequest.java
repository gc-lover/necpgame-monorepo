package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AllianceRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AllianceRequest {

  private String allyGuildId;

  private @Nullable String terms;

  public AllianceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AllianceRequest(String allyGuildId) {
    this.allyGuildId = allyGuildId;
  }

  public AllianceRequest allyGuildId(String allyGuildId) {
    this.allyGuildId = allyGuildId;
    return this;
  }

  /**
   * Get allyGuildId
   * @return allyGuildId
   */
  @NotNull 
  @Schema(name = "allyGuildId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("allyGuildId")
  public String getAllyGuildId() {
    return allyGuildId;
  }

  public void setAllyGuildId(String allyGuildId) {
    this.allyGuildId = allyGuildId;
  }

  public AllianceRequest terms(@Nullable String terms) {
    this.terms = terms;
    return this;
  }

  /**
   * Get terms
   * @return terms
   */
  
  @Schema(name = "terms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("terms")
  public @Nullable String getTerms() {
    return terms;
  }

  public void setTerms(@Nullable String terms) {
    this.terms = terms;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllianceRequest allianceRequest = (AllianceRequest) o;
    return Objects.equals(this.allyGuildId, allianceRequest.allyGuildId) &&
        Objects.equals(this.terms, allianceRequest.terms);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allyGuildId, terms);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllianceRequest {\n");
    sb.append("    allyGuildId: ").append(toIndentedString(allyGuildId)).append("\n");
    sb.append("    terms: ").append(toIndentedString(terms)).append("\n");
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

