package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ValidationSeverity;
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
 * ValidationIssue
 */


public class ValidationIssue {

  private String code;

  private ValidationSeverity severity;

  private String messageKey;

  private @Nullable String defaultMessage;

  private Boolean blocking = false;

  @Valid
  private List<String> affectedFields = new ArrayList<>();

  private @Nullable String recommendation;

  @Valid
  private List<String> links = new ArrayList<>();

  public ValidationIssue() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidationIssue(String code, ValidationSeverity severity, String messageKey) {
    this.code = code;
    this.severity = severity;
    this.messageKey = messageKey;
  }

  public ValidationIssue code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Код правила или нарушения.
   * @return code
   */
  @NotNull 
  @Schema(name = "code", description = "Код правила или нарушения.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ValidationIssue severity(ValidationSeverity severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull @Valid 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public ValidationSeverity getSeverity() {
    return severity;
  }

  public void setSeverity(ValidationSeverity severity) {
    this.severity = severity;
  }

  public ValidationIssue messageKey(String messageKey) {
    this.messageKey = messageKey;
    return this;
  }

  /**
   * Get messageKey
   * @return messageKey
   */
  @NotNull @Size(min = 3, max = 128) 
  @Schema(name = "messageKey", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("messageKey")
  public String getMessageKey() {
    return messageKey;
  }

  public void setMessageKey(String messageKey) {
    this.messageKey = messageKey;
  }

  public ValidationIssue defaultMessage(@Nullable String defaultMessage) {
    this.defaultMessage = defaultMessage;
    return this;
  }

  /**
   * Get defaultMessage
   * @return defaultMessage
   */
  
  @Schema(name = "defaultMessage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("defaultMessage")
  public @Nullable String getDefaultMessage() {
    return defaultMessage;
  }

  public void setDefaultMessage(@Nullable String defaultMessage) {
    this.defaultMessage = defaultMessage;
  }

  public ValidationIssue blocking(Boolean blocking) {
    this.blocking = blocking;
    return this;
  }

  /**
   * Get blocking
   * @return blocking
   */
  
  @Schema(name = "blocking", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blocking")
  public Boolean getBlocking() {
    return blocking;
  }

  public void setBlocking(Boolean blocking) {
    this.blocking = blocking;
  }

  public ValidationIssue affectedFields(List<String> affectedFields) {
    this.affectedFields = affectedFields;
    return this;
  }

  public ValidationIssue addAffectedFieldsItem(String affectedFieldsItem) {
    if (this.affectedFields == null) {
      this.affectedFields = new ArrayList<>();
    }
    this.affectedFields.add(affectedFieldsItem);
    return this;
  }

  /**
   * Get affectedFields
   * @return affectedFields
   */
  
  @Schema(name = "affectedFields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedFields")
  public List<String> getAffectedFields() {
    return affectedFields;
  }

  public void setAffectedFields(List<String> affectedFields) {
    this.affectedFields = affectedFields;
  }

  public ValidationIssue recommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
    return this;
  }

  /**
   * Get recommendation
   * @return recommendation
   */
  
  @Schema(name = "recommendation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendation")
  public @Nullable String getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
  }

  public ValidationIssue links(List<String> links) {
    this.links = links;
    return this;
  }

  public ValidationIssue addLinksItem(String linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links
   * @return links
   */
  
  @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("links")
  public List<String> getLinks() {
    return links;
  }

  public void setLinks(List<String> links) {
    this.links = links;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationIssue validationIssue = (ValidationIssue) o;
    return Objects.equals(this.code, validationIssue.code) &&
        Objects.equals(this.severity, validationIssue.severity) &&
        Objects.equals(this.messageKey, validationIssue.messageKey) &&
        Objects.equals(this.defaultMessage, validationIssue.defaultMessage) &&
        Objects.equals(this.blocking, validationIssue.blocking) &&
        Objects.equals(this.affectedFields, validationIssue.affectedFields) &&
        Objects.equals(this.recommendation, validationIssue.recommendation) &&
        Objects.equals(this.links, validationIssue.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, severity, messageKey, defaultMessage, blocking, affectedFields, recommendation, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationIssue {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    messageKey: ").append(toIndentedString(messageKey)).append("\n");
    sb.append("    defaultMessage: ").append(toIndentedString(defaultMessage)).append("\n");
    sb.append("    blocking: ").append(toIndentedString(blocking)).append("\n");
    sb.append("    affectedFields: ").append(toIndentedString(affectedFields)).append("\n");
    sb.append("    recommendation: ").append(toIndentedString(recommendation)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

