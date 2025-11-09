package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.mailservice.model.Attachment;
import com.necpgame.mailservice.model.CODInfo;
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
 * CODPaymentResponse
 */


public class CODPaymentResponse {

  private @Nullable String mailId;

  private @Nullable CODInfo codInfo;

  @Valid
  private List<@Valid Attachment> attachments = new ArrayList<>();

  public CODPaymentResponse mailId(@Nullable String mailId) {
    this.mailId = mailId;
    return this;
  }

  /**
   * Get mailId
   * @return mailId
   */
  
  @Schema(name = "mailId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mailId")
  public @Nullable String getMailId() {
    return mailId;
  }

  public void setMailId(@Nullable String mailId) {
    this.mailId = mailId;
  }

  public CODPaymentResponse codInfo(@Nullable CODInfo codInfo) {
    this.codInfo = codInfo;
    return this;
  }

  /**
   * Get codInfo
   * @return codInfo
   */
  @Valid 
  @Schema(name = "codInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("codInfo")
  public @Nullable CODInfo getCodInfo() {
    return codInfo;
  }

  public void setCodInfo(@Nullable CODInfo codInfo) {
    this.codInfo = codInfo;
  }

  public CODPaymentResponse attachments(List<@Valid Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public CODPaymentResponse addAttachmentsItem(Attachment attachmentsItem) {
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
  public List<@Valid Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid Attachment> attachments) {
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
    CODPaymentResponse coDPaymentResponse = (CODPaymentResponse) o;
    return Objects.equals(this.mailId, coDPaymentResponse.mailId) &&
        Objects.equals(this.codInfo, coDPaymentResponse.codInfo) &&
        Objects.equals(this.attachments, coDPaymentResponse.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailId, codInfo, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CODPaymentResponse {\n");
    sb.append("    mailId: ").append(toIndentedString(mailId)).append("\n");
    sb.append("    codInfo: ").append(toIndentedString(codInfo)).append("\n");
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

