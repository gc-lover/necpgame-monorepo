package com.necpgame.adminservice.model;

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
 * ResolveTicketRequest
 */


public class ResolveTicketRequest {

  private String resolutionNote;

  private Boolean sendSurvey = true;

  public ResolveTicketRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResolveTicketRequest(String resolutionNote) {
    this.resolutionNote = resolutionNote;
  }

  public ResolveTicketRequest resolutionNote(String resolutionNote) {
    this.resolutionNote = resolutionNote;
    return this;
  }

  /**
   * Get resolutionNote
   * @return resolutionNote
   */
  @NotNull 
  @Schema(name = "resolutionNote", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resolutionNote")
  public String getResolutionNote() {
    return resolutionNote;
  }

  public void setResolutionNote(String resolutionNote) {
    this.resolutionNote = resolutionNote;
  }

  public ResolveTicketRequest sendSurvey(Boolean sendSurvey) {
    this.sendSurvey = sendSurvey;
    return this;
  }

  /**
   * Get sendSurvey
   * @return sendSurvey
   */
  
  @Schema(name = "sendSurvey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sendSurvey")
  public Boolean getSendSurvey() {
    return sendSurvey;
  }

  public void setSendSurvey(Boolean sendSurvey) {
    this.sendSurvey = sendSurvey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResolveTicketRequest resolveTicketRequest = (ResolveTicketRequest) o;
    return Objects.equals(this.resolutionNote, resolveTicketRequest.resolutionNote) &&
        Objects.equals(this.sendSurvey, resolveTicketRequest.sendSurvey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resolutionNote, sendSurvey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResolveTicketRequest {\n");
    sb.append("    resolutionNote: ").append(toIndentedString(resolutionNote)).append("\n");
    sb.append("    sendSurvey: ").append(toIndentedString(sendSurvey)).append("\n");
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

