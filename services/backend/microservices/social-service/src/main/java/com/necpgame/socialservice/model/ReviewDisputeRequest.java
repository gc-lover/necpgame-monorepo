package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
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
 * ReviewDisputeRequest
 */


public class ReviewDisputeRequest {

  /**
   * Gets or Sets complaintType
   */
  public enum ComplaintTypeEnum {
    PAYMENT_ISSUE("payment_issue"),
    
    HARASSMENT("harassment"),
    
    FRAUD("fraud"),
    
    SPAM("spam"),
    
    OTHER("other");

    private final String value;

    ComplaintTypeEnum(String value) {
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
    public static ComplaintTypeEnum fromValue(String value) {
      for (ComplaintTypeEnum b : ComplaintTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ComplaintTypeEnum complaintType;

  private String description;

  @Valid
  private List<URI> attachments = new ArrayList<>();

  public ReviewDisputeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewDisputeRequest(ComplaintTypeEnum complaintType, String description) {
    this.complaintType = complaintType;
    this.description = description;
  }

  public ReviewDisputeRequest complaintType(ComplaintTypeEnum complaintType) {
    this.complaintType = complaintType;
    return this;
  }

  /**
   * Get complaintType
   * @return complaintType
   */
  @NotNull 
  @Schema(name = "complaintType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("complaintType")
  public ComplaintTypeEnum getComplaintType() {
    return complaintType;
  }

  public void setComplaintType(ComplaintTypeEnum complaintType) {
    this.complaintType = complaintType;
  }

  public ReviewDisputeRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull @Size(min = 16, max = 2000) 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ReviewDisputeRequest attachments(List<URI> attachments) {
    this.attachments = attachments;
    return this;
  }

  public ReviewDisputeRequest addAttachmentsItem(URI attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<URI> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<URI> attachments) {
    this.attachments = attachments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewDisputeRequest reviewDisputeRequest = (ReviewDisputeRequest) o;
    return Objects.equals(this.complaintType, reviewDisputeRequest.complaintType) &&
        Objects.equals(this.description, reviewDisputeRequest.description) &&
        Objects.equals(this.attachments, reviewDisputeRequest.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complaintType, description, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewDisputeRequest {\n");
    sb.append("    complaintType: ").append(toIndentedString(complaintType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
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

